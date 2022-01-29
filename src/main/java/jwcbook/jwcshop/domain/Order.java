package jwcbook.jwcshop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity //데이터 베이스가 취급할 하나의 개체, 즉 DB에서 말하는 테이블임을 명시해주는 어노테이션
@Table(name="orders") //테이블 명 잡아주기
@Getter @Setter
public class Order {
    //연관관계 맵핑 시, Order(주문)에 입장에서 1:n, n:1, n:n 을 생각해보아야 한다.
    //만약 회원 엔티티와 관계는 하나의 회원에 대해서 여러 주문이 생성될 수 있는 1(회원):n(주문)의 관계이다.
    //이런 상황에, 주문에 해닿아는 Order 클래스에는 @ManyToOne, 즉 Order에 해당하는 부분이 앞에 오게끔.
    //반대로 1에 해당하는 Member 클래스에는 @OneToMany 로 매핑해야할 것이다.

    //@Column 애노테이션은, 별다른 속성이 필요 없으면 생략 가능.

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    //여러개의 주문(n)은 하나의 회원(1)에 의해서 생성 가능.
    @ManyToOne // 연관관계
    @JoinColumn(name = "member_id") //Member 테이블의 member_id라는 이름을 갖는 속성에 조인하여 외래키로 매핑
    //맵핑을 무엇으로 할거냐. (foreign key 이름)
    //연관관계 주인은 foreign키가 가까운 곳!!으로 설정.
    private Member member;

    //한 '주문'(1)에 대하여 여러개의 '주문상품'(n) 생성 가능.
    @OneToMany(mappedBy = "order") //order에 의해서 mapping이 됨. ==> 외래키의 기준이 되는 키!
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    //private Date date;
    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [Order , Cancel]
}
