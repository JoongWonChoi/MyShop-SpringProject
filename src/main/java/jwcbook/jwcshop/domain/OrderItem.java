package jwcbook.jwcshop.domain;

import jwcbook.jwcshop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne //현재의 OrderItem 입장에선 Item과 n:1 관계 ==> 하나의 아이템(1)에 대해 여러 '주문상품'(n) 생성 가능
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne //현재의 OrderItem 입장에선 Order과 n:1 관계 ==> 하나의 '주문'(1) 에 대해 여러 '주문상품'(n) 생성 가능
    @JoinColumn(name="order_id")
    //Order와 양방향 연관관계 생성됨.
    //order_id 는 Fk로 등록
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량


}
