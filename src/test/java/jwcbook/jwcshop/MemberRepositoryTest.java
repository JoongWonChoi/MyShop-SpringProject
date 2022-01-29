package jwcbook.jwcshop;

import org.apache.el.parser.AstFalse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false) //Test case 에서는 본 어플리케이션에 영향을 주지 않도록 데이터 관련 처리를 하고 바로 롤백을 진행하여 실제 DB에는 반영되지 않음.
    //실제로 DB에 찍히는걸 눈으로 확인하고싶다면 Rollback 어노테이션 사용!
    public void testMember() throws Exception {

        //given
        Member member = new Member();
        member.setUsername("jwc");

        //when
        Long save = memberRepository.save(member);
        Member findMember = memberRepository.find(save);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        //같은 영속성 컨텍스트 내에서는 식별자가 같으면 같은 객체로 취급함!
        //1차 캐시, 영속성 컨텍스트
        System.out.println("findmember == member" + (findMember==member));

    }

}