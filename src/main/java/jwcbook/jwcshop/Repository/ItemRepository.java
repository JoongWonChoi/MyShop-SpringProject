package jwcbook.jwcshop.Repository;


import jwcbook.jwcshop.domain.item.Book;
import jwcbook.jwcshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    //@PersistContext 어노테이션을 사용하지 않고 EntityManager를 호출하는 방법 ==> @RequiredArgsConstructor
    private final EntityManager em;

    public void save(Item item){
        // jpa에 저장되지 않으면 id값이 없음 ==> 완전히 새로 생성된 객체라는 뜻!
        // em.persist로 먼저 신규로 영속성 컨텍스트에 등록하는 것.
        if (item.getId() == null) {
             em.persist(item);
        }
        //그렇지 않다면(id가 존재), 이미 어딘가에 생성되어 관리되고 있는 것이기 때문에, update를 하는 맥락
        else{
            em.merge(item); //parameter로 넘어온 item이라는 객체는 준영속 컨텍스트 상태. merge를 하여도 영속성 컨텍스트로 변하는 것이 아님.
            //merge는 넘어온 update된 객체의 모~~든 필드 값으로 변경시킨다. 그렇담녀 null값이 넘어오면??(update이기때믄에)
            //따라서 가급적 merge는 실무에서 사용하지 않는게 나음.
            //변경 감지로 변경이 된 필드만 선택하여 교체하게끔 해주는 전략이 훨씬 좋은 전략
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    //영속성 컨텍스트에 있는 엔티티 객체를 찾아와서, 변경할 데이터들을 직접 변경해주는 방식.
    public Item createUpdateForm(Long itemId, String name, int stockQuantity, int price) {
        Item findItem = findOne(itemId); // 조회
        findItem.change(name, stockQuantity, price); // 저장(변경 데이터 저장)

        /*id식별자로 영속성 컨텍스트에 있는 엔티티 객체를 조회하여 갖고 온 후,
        * 해당 엔티티 객체에 접근하여 원하는 수정 로직을 발생
        * Repository 차원에서는 DB에 접근하여(엔티티 객체 반환),
        * 수정 및 저장 하는 임무(해당 엔티티 객체에 접근하여 데이터 처리)
        * 그렇다면 Service에서는 이러한 기능을 수행 해달라는 비즈니스 로직의 기능 수행!?*/

        return findItem;
    }
}
