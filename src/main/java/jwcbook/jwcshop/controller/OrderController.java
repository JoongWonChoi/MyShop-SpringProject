package jwcbook.jwcshop.controller;


import jwcbook.jwcshop.Repository.OrderSearch;
import jwcbook.jwcshop.Service.ItemService;
import jwcbook.jwcshop.Service.MemberService;
import jwcbook.jwcshop.Service.OrderService;
import jwcbook.jwcshop.domain.Member;
import jwcbook.jwcshop.domain.Order;
import jwcbook.jwcshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,//html의 form에서 'name=?' 으로 설정된 값이 넘어와서 바인딩 하는 방식
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        //식별자만 controller를 통해 핵심 비즈니스로 넘기기
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";

    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        /*
        @ModelAttribute("orderSearach") ==> 이 어노테이션으로 아래의 코드와 같은 기능을 해줌
        model.addAttribute("orderSearch", orderSearch);*/

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId")Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }

}
