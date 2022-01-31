package jwcbook.jwcshop.Service;

import jwcbook.jwcshop.Repository.ItemRepository;
import jwcbook.jwcshop.domain.item.Book;
import jwcbook.jwcshop.domain.item.Item;
import jwcbook.jwcshop.exception.NotEnoughStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 기본 test용 Rollback 수행
class ItemServiceTest {

    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;


    @Test

    void 상품_등록() {

        //given
        Book book = new Book();
        book.setAuthor("jwc");

        //when
        itemService.saveItem(book);

        //then
       // System.out.println(itemRepository.findOne(1L).toString());
        assertThat(itemService.findOne(1L)).isEqualTo(book); // ==> 영속성 컨텍스트에 의해 persist된 객체와 find된 객체가 일치(1차 캐시)
    }

    @Test
    void 상품_재고_증가() {
        //given
        Book book = new Book();
        book.setStockQuantity(50);

        //when
        book.addStock(20);

        //then
        assertThat(book.getStockQuantity()).isEqualTo(70);
    }

    @Test
    void 상품_재고_감소() {
        //given
        Book book = new Book();
        book.setStockQuantity(50);

        //when
        try{
        book.removeStock(60);}
        catch (NotEnoughStockException n){
            n.printStackTrace();// 등록한 에러 메세지가 잘 출력되나 확인!
            return;
        } // ==> 이 곳에서 에러가 발생하고 끝나야함

        //then
        Assertions.fail("fail"); // ==>여기까지 코드가 오면 fail
    }

}