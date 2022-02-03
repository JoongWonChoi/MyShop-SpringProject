package jwcbook.jwcshop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberForm {

    @NotEmpty(message =  "회원 이름은 필수 입니다 . .")
    private String name;

    private Long id;
    private String city;
    private String street;
    private String zipcode;

    //==정보 저장 메서드==//
    public void createMemberForm(Long id, String name, String city, String street, String zipcode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
