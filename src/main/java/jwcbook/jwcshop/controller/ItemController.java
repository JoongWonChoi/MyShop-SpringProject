package jwcbook.jwcshop.controller;


import jwcbook.jwcshop.Service.ItemService;
import jwcbook.jwcshop.domain.item.Book;
import jwcbook.jwcshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String create(BookForm form) {
        Book book = new Book();
        //사실 이렇게 setter를 마구잡이로 사용하는 방식보다, createBook등의 일체화 로직을 짜서 사용하는것이 더 좋음.
        //실무에선 setter는 거의 안쓰도록!

        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setPrice(form.getPrice());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    //상품 목록 조회
    @GetMapping("/items")
    public String itemList(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    //상품 수정 ==> merge vs 변경감지
    @GetMapping("/items/{id}/edit")
    public String updateItemForm(@PathVariable("id") Long itemId, Model model) {
        //전달된 id에 해당하는 item객체 불러오기
        Book item = (Book)itemService.findOne(itemId);

        //새로운 BookForm객체를 생성하고, 반환된 item객체의 정보들을 모두 옮겨담는다.
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setName(item.getName());
        form.setIsbn(item.getIsbn());
        form.setAuthor(item.getAuthor());

        //옮겨담긴 form이라는 그릇을 폼에 전달
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String editItem(@PathVariable("id") Long id, @ModelAttribute("form") BookForm form) {
        //새로운 Book 객체를 생성하여, 수정 후 반환된 'form(BookForm의 객체)'의 정보들을 같은 방식으로 옮겨담는다.
        //그 후 새롭게 update된 book 객체를 다시 저장하면 끝!

        //나는 이전 게시판 CRUD구현 시, id가 바뀌면 안된다는 것에 초점을 맞추고 좀 복잡하게 구현했던 기억이 있음
        //but 이처럼 수정하고자 하는 객체의 id까지 불러와서 아예 새로운 객체에 id마저 전달하면, 같은 기능을 할 수 있다!
        //하지만 이전 게시판 BoardRepository에서는 save가 호출되면 자동으로 id가 ++되고 저장되었는데, 이는 어떻게 해결??
        //maybe jpa 자체적으로 영속성 컨텍스트와 같은 내부 기능올 통해 처리해줄 것이라고 생각이 됨.
        // ==> EntityManager의 Merge와 관련되어 있나?? (id값이 있을때와 없을 때의 기능 차이)

        /*이렇게 웹 계층 controller에서 엔티티 객체를 함부로 다루면 안됨!*//*
        Book book = new Book();
        book.setId(form.getId());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setName(form.getName());
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());*/

        //업데이트 될 필드만 선택적으로 전달하는 방식
        // ex)아이템 아이템 이름, 수량, 아이템 가격을 수정
        itemService.updateItem(id, form.getName(), form.getStockQuantity(), form.getPrice());

        /*그런데 실무에서, 어떠한 필드가 변경될지 모르는데 어떻게 해야할까?
        Entity Manager의 merge 방식은 변경이 시도된 객체 자체를 넘기기 때문에, 변경되지 않은(입력이 되지 않음) 값은 null로 넘어갈 수 있다.
        따라서 변경 감지,dirty checking을 위해서는 변경이 일어난 필드만 넘겨야 하는데, 이름 검증하는 로직이 따로 필요??
        혹은 수정을 꼭 해야하는 필드와 수정을 하면 안되는 필드를 개발 차원에서 제한?*/

        return "redirect:/items";
    }
}
