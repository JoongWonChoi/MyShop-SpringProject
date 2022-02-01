package jwcbook.jwcshop.Service;


import jwcbook.jwcshop.Repository.ItemRepository;
import jwcbook.jwcshop.Repository.MemberRepository;
import jwcbook.jwcshop.Repository.OrderRepository;
import jwcbook.jwcshop.domain.Delivery;
import jwcbook.jwcshop.domain.Member;
import jwcbook.jwcshop.domain.Order;
import jwcbook.jwcshop.domain.OrderItem;
import jwcbook.jwcshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    //order//
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); //회원의 주소를 읽어봐 배송으로 등록

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count); //상품, 상품가격, 수량 ==> 주문상품 생성

        //==>생성한 주문 상품을 '주문'에 입력

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    //cancel
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    /*//search
    public List<Order> findOrders(OrderSearch orderSearch) {
        orderRepository.findAll(orderSearch);
    }*/
}
