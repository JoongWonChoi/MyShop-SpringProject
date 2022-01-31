package jwcbook.jwcshop.Service;


import jwcbook.jwcshop.Repository.ItemRepository;
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

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
