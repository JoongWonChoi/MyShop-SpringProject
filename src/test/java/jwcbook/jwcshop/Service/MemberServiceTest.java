package jwcbook.jwcshop.Service;

import jwcbook.jwcshop.Repository.MemberRepository;
import jwcbook.jwcshop.domain.Member;

//import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@ExtendWith(SpringExtension.class) //junit 실행 시 Spring을 엮어서 실행하겠다.
@SpringBootTest //Spring Boot를 띄운 상태로 테스트를 진행하기 위함. 스프링 컨테이너 내에서 테스트 진행
@Transactional //기본적으로 RollBack을 수행
//Test case에서와 다른 비즈니스 수행 로직에서 동작하는 것이 다름. 테스트에서만 롤백 기능 수행!
//RollBack : DB에 있는 모든 것을 버린다는 의미.
public class MemberServiceTest {
    //test를 위한 의존관계 주입 ==> 기본 단위 테스트만을 진행하기 때문에 필드 주입을 사용해도 무관할 것임!
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //RollBack을 하면 영속성 컨텍스트에 담긴 것들이 DB에 들어가지 않음.
    @Rollback(value = false) //RollBack을 false로 하면 실제 커밋을 하여 insert문까지 생성 => DB에 날림
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setName("jwc");

        //when
        Long saveId = memberService.join(member); //Repository의 em.persist(member)를 통해 member객체가 JPA가 관리하는 영속성 컨텍스트의 관리 하에 들어가게 된다.

        //then
        /*em.flush(); // 영속성 컨텍스트를 넘어 데이터의 변화 양상이 실제 DB에 반영되는 것까지 확인하고 싶다면 flush 수행!*/
        Assertions.assertEquals(member, memberRepository.findOne(saveId));

    } //이 테스트가 끝나면 @Transactional을 통해, public RollBack 수행!

    @Test//(expected = IllegalStateException.class)
    public void 중복_회원_예제() {
        //given
        Member member1 = new Member();
        member1.setName("jwc1");

        Member member2 = new Member();
        member2.setName("jwc1");

        //when
        memberService.join(member1);
        try{
        memberService.join(member2);}
        catch(IllegalStateException e){
            return;
        }// ==> 예외가 발생해야함!

        //then
        Assertions.fail(); //==>code가 여기까지 오면 안됨!

    }

}
