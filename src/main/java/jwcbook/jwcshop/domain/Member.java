package jwcbook.jwcshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id") //열, 즉 attribute 이름 생성 및 등록
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    //주문 엔티티와의 관계에서 회원 입장이 1, 주문 입장이 n(1:N)이기에 OneToMany 관계
    //연관관계에 있어서 주인이 아님을 표현.
    //order 테이블에 있는 member 에 연관관계 매핑됨.
    private List<Order> orders = new ArrayList<>();

    public void change(String name, String city, String street, String zipcode) {
        this.name = name;
        //와우!!
        this.address = new Address(city, street, zipcode);
    }
}
