package jwcbook.jwcshop.Repository;


import jwcbook.jwcshop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    //jpa를 사용!

    @PersistenceContext // 영속석 컨텍스트! JPA의 Entity Manager(Spring것이 아님.)를 스프링이 생성한 Entity Manger에 주입해주는 기능
    private EntityManager em;
    //영속성 컨텍스트의 key는 보통 ID(@Id)값이 됨! (PK)

    public void save(Member member) {
        em.persist(member); //persist : 영속성 컨텍스트에 member 객체 넣어줌.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); //단건 조회. (조회 타입, PK)
    }

    public List<Member> findAll() { //JPQL 문법 사용! from의 대상이 Table이 아닌 Entity가 됨.
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();// JPQL :: Table이 아닌entity 객체를 대상으로 query함.
        return result;
    }

    //회원을 이름으로 조회하기
    public List<Member> findByName(String name) {
        //데이터를 검색할 때 항상 정적인, 지정된 데이터만을 검색하지 않을 수 있다.
        //사용자가 원하는 검색어를 사용하여 데이터를 검색할 때, 동적으로 바인딩을 해야한다.
        //JPQL 쿼리에, 검색을 원하는 부분(데이터가 동적으로 추가될 부분)을 ':' 으로 지정하고, 그곳에 접근 가능하도록 변수를 지정해 놓는다.
        //그 후 setParameter() 메서드로 JQPL쿼리에서 지정해놓았던 변수를 표기하고, 전달받은(사용자가 원하는) 파라미터를 바인딩 시켜줌!
        //setParameter("JQPL에 지정해놓았던 변수 이름", "사용자가 검색할 데이터(키워드)")
        return em.createQuery("select m from Member m where m.name = :name", Member.class) // (query문, 조회 타입(엔티티 클래스타입))
                .setParameter("name", name) //파라미터 바인딩 하기
                .getResultList(); // getResultList() ==> 작성한 JQPL 쿼리로 데이터를 검색, List형태로 반환한다.
    }

/*    createQuery() : JPQL 쿼리 문 작성 ==> ("JQPL쿼리", 조회할 엔티티 클래스 타입)
    getResultList() : 작성된 JPQL로 데이터 검색 후 List형태로 반환
    setParameter() : 동적으로 키워드를 JPQL에 지정하고자 할 때 사용. ==> ("JPQL에서 지정한 변수명", "동적으로 전달된 parameter")
    */
}
