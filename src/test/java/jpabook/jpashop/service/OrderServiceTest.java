package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문(){
        Member m = createMember("member1");

        Book book = createBook("JPA", 1000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(m.getId(), book.getId(), orderCount);

        Order order = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, order.getStatus());    // 주문 상태 ORDER
        assertEquals(1, order.getOrderItems().size()); // 상품종류수
        assertEquals(1000 * 2, order.getTotalPrice()); // 총 가격: 가격 * 수량
        assertEquals(10 - 2, book.getStockQuantity()); // 재고 수량

    }

    @Test
    public void 상품주문_재고수량초과(){
        Member member = createMember("member1");
        Book item = createBook("JPA", 1000, 10);

        int orderCount = 11;

        Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));

        fail("예외 발생 해야됨");
    }

    @Test
    public void 주문취소(){
        Member member = createMember("member1");
        Item item = createBook("JPA", 1000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, item.getStockQuantity());

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String member) {
        Member m = new Member();
        m.setName(member);
        m.setAddress(new Address("서울", "강가", "123"));
        em.persist(m);
        return m;
    }
}