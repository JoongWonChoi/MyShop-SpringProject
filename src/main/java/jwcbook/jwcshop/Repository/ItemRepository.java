package jwcbook.jwcshop.Repository;


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
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
