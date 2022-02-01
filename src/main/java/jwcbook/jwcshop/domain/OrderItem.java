package jwcbook.jwcshop.domain;

import jwcbook.jwcshop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch= LAZY) //현재의 OrderItem 입장에선 Item과 n:1 관계 ==> 하나의 아이템(1)에 대해 여러 '주문상품'(n) 생성 가능
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch= LAZY) //현재의 OrderItem 입장에선 Order과 n:1 관계 ==> 하나의 '주문'(1) 에 대해 여러 '주문상품'(n) 생성 가능
    @JoinColumn(name="order_id")
    //Order와 양방향 연관관계 생성됨.
    //order_id 는 Fk로 등록
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량

    protected OrderItem() {

    }

    //==생성 메서드==// ==> 이 메서드 하나로 상품 주문 가능하게끔 설계
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        //주문상품 정보 생성
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //주문한 수량만큼 재고에서 까기
        item.removeStock(count);
        return orderItem;
    }


    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//
    //주문 상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
