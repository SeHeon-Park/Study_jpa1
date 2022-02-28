package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  // 기본적으로 roll back기능 가지고 있음
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        Member m = new Member();
        m.setName("park");

        Long saveId = memberService.join(m);

        assertThat(memberRepository.findOne(saveId)).isEqualTo(m);
    }

    @Test
    public void 중복_회원_예외(){
        Member m1 = new Member();
        m1.setName("park");

        Member m2 = new Member();
        m2.setName("park");

        memberService.join(m1);
        try {
            memberService.join(m2);
        }catch(IllegalStateException e){
            System.out.println("중복 오류");
            return;
        }

        fail("오잉?");
    }

}