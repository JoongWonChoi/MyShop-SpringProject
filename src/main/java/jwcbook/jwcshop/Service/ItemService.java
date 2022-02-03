package jwcbook.jwcshop.Service;


import jwcbook.jwcshop.Repository.ItemRepository;
import jwcbook.jwcshop.domain.item.Book;
import jwcbook.jwcshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //읽기만 하는 기능이 아니기 때문에 따로 public annotation과 달리 설정해줘야함 ==> 쓰기 기능 이라는 것을 명시
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    //업데이트 될 필드만 선택적으로 전달하는 방식
    // ex)아이템 아이템 이름, 수량, 아이템 가격을 수정
    @Transactional
    public void updateItem(Long itemId, String name, int stock, int price) {
        //parameter로 넘어온, update된 Book타입의 bookParam은 웹계층을 위한 객체였을 뿐, 영속성 컨텍스트에 관ㄹ리되는 엔티티 객체가 아니다.
        //따라서 변경 감지를 위해서는 넘어온 id값으로 db에 저장된, 영속성 컨텍스트에 있는 엔티티 객체를 불러와야함
        //Item findItem = itemRepository.findOne(itemId); //id를 기반으로 실제 db에 있는 영속상태에 있는 객체 찾아옴
        /*findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
        //==> 영속성 컨텍스트에 있는 객체이기 때문에 변경감지를 함!
        //역시나 setter는 사용하지 말고, 의미있는 메서드를 제작해서 사용하자.*/

        itemRepository.createUpdateForm(itemId, name, stock, price);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
