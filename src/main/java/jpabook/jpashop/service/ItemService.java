package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    // 변경감지(dirty check)기능 사용(추천)
    @Transactional
    public void updateItem(Long itemId, Book param){ //itemParam: 파라미터로 넘어온 준영속 상태의 엔티티
        Item findItem = itemRepository.findOne(itemId);  // 같은 엔티티 조회, findItem은 영속상태
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        // 수정만 하면 @Transactional에 의해서 transaction이 commit이 됨
    }

    @Transactional
    public void updateItemReal(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }



//    @Transactional
//    public Item updateItem(Long itemId, Book param){
//        Item findItem = itemRepository.findOne(itemId);           // merge코드랑 완전히 같음
//        findItem.setPrice(param.getPrice());                      // merge주의: 완전히 파라미터로 넘어온것으로 전부 다바뀜(null로 될수도있어..)
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//        return findItem;
//    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
