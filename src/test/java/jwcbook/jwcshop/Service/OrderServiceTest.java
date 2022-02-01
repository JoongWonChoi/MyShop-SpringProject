package jwcbook.jwcshop.Service;

import jwcbook.jwcshop.Repository.OrderRepository;
import jwcbook.jwcshop.domain.Address;
import jwcbook.jwcshop.domain.Member;
import jwcbook.jwcshop.domain.Order;
import jwcbook.jwcshop.domain.OrderStatus;
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

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    //상품 주문
    @Test
    void 상품_주문() {
        //given
        Member member = new Member();
        member.setName("jwc1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("bookA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        //when

        Long id = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(id);


        //then
        assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus()); //주문이 잘 들어갔는지
        assertThat(1).isEqualTo(getOrder.getOrderItems().size()); //주문 상품의 개수
        assertThat(10000 * orderCount).isEqualTo(getOrder.getTotalPrice()); // 주문 총 가격
        assertThat(8).isEqualTo(book.getStockQuantity()); //주문 수량만큼 재고가 줄어야함
    }

    //재고수량 초과 시
    @Test//(expected = NotEnoughStockException.class)
    void 상품주문_재고수량초과() {
        //given
        Member member = new Member();
        member.setName("jwc1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("bookA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 11;

        //when
        try {
            orderService.order(member.getId(), book.getId(), orderCount);
        }
        //then
        catch (NotEnoughStockException n) {
            n.printStackTrace();
            return;
        }

        //then
        fail(); //여기까지 코드가 오면 테스트 실패!

    }


    //주문 취소
    @Test
    void 주문_취소() {
        //given
        Member member = new Member();
        member.setName("jwc1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("bookA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when

        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
        assertThat(10).isEqualTo(book.getStockQuantity());

    }


}