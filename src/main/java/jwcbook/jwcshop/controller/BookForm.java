package jwcbook.jwcshop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BookForm {
//web 계층(controller)에서만 사용
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;



}
