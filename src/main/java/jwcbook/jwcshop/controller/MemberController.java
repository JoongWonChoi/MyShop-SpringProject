package jwcbook.jwcshop.controller;


import com.sun.source.tree.MemberSelectTree;
import jwcbook.jwcshop.Service.MemberService;
import jwcbook.jwcshop.domain.Address;
import jwcbook.jwcshop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        //굳이 아무것도 없는 MemberForm을 새로운 객체로 Model을 통해 Form으로 넘기는 이유는?
        //thymeleaf 의 object와 같은 문법을 사용하여 MemberForm에 있는 정보(타입) 등을 사용하기 위함?

        /*게시판 구현 시 model객체를 쓰는 것은 좋지 않다고 생각. 뭔가 DB에 접근하여 데이터를 처리해야 한다고 생각했음
        그런데, DB접근은 JPA차원에서 가져오는 것이므로 이런 저런 로직들을 통해 데이터를 반환해와서, 최종적으로 Model에 정보를 담고
                view로 넘기기 위한 수단으로 보임.*/
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        /*Member Entyty를 넣지 않는 이유?
         * 기존의 Entity와 속성은 유사할 수 있어도,
         * 현재처럼 폼에 사용할 정보는 따로 만들어 두는게 좋음
         * 실무에선 Member(Entity)와 MemberForm(Form data) 간의 차이가 커질 수 있고, 필요한 데이터만 생성하여 폼에 사용하는 방식이 더욱 효율적*/

        //error validation 하는 방법!
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }//에러가 발생하면, 해당 에러 정보를 갖고 return하는 form으로 이동 및 전달
        //이게 가능한 이유 ==> thymeleaf가 java만이 아닌 spring과의 통합도 상당히 잘 되어있기 때문?

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        /*이러한 엔티티 접근 로직이 Controller에서 가능하다는 사실을 처음 알게됨.
         * 기존에는
         * Controller는 오직 form에서 받아온 데이터 Service로 전송
         * -> Service는 단순 컨트롤러에서 넘어온 데이터를 repository에 전달하기 위한, 사용자 차원의 기능
         * -> 레포지토리에서 최종 데이터 처리 및 저장
         * 의 개념인 줄 알았다. 하지만 Entity 클래스에 비즈니스 로직 및 조회 로직을 짜서 사용하는게 효율적인 경우가 있고.
         * 이처럼 controller에서 정보를 처리하는 과정이 있어도 괜찮을 수 있다는 것이 새로웠다.*/

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); // 사실 이런식으로 Entity를 반환하는 것은 좋은 방식이 아님.
        //특히 API를 사용할 땐 절대 Entity정보를 외부에 반환하면 안됨!
        //따라서 Member DTO나 화면에 맞는 폼 객체를 생성하는것이 더 좋음!
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/members/{id}/edit")
    public String editMember(@PathVariable("id") Long id, Model model, MemberForm form) {
        // Member member = memberService.findOne(id); ==> entity자체를 form에 넘기는 것은 좋지 않은 방식.
        /*Member member = memberService.findOne(id);
        form.setName(member.getName());
        form.setCity(member.getAddress().getCity());
        form.setStreet(member.getAddress().getStreet());
        form.setZipcode(member.getAddress().getZipcode());*/
        /*이렇듯 web계층에서의 목적으로 사용하는 form 객체는 setter를 사용하는것이 맞을까? 아마 NO!
         * 그래서 MemberForm에 메서드를 생성*/
        Member member = memberService.findOne(id);
        //웹 계층 MemberForm 객체에 영속성 컨텍스트에 저장되었던 Member 엔티티 정보를 덮어쓰기. Entity에 직접 접근하는 것이 아닌, form에 데이터 값만을 전해주는 방식
        //또한 MemberForm 객체에도 최대한 setter를 사용하지 않도록 정보 저장 로직 메서드 생성.
        form.createMemberForm(
                member.getId(),
                member.getName(),
                member.getAddress().getCity(),
                member.getAddress().getStreet(),
                member.getAddress().getZipcode());
        model.addAttribute("form", form);
        return "members/updateMemberForm";
    }
    @PostMapping("members/{id}/edit")
    public String updateMember(@PathVariable("id")Long id, @ModelAttribute("form")MemberForm form) {
        System.out.println("id = " + id);
        System.out.println(form.getName());
        System.out.println(form.getCity());
        System.out.println(form.getStreet());
        memberService.updateMember(id, form.getName(), form.getCity(), form.getStreet(), form.getZipcode());
        return "redirect:/members";
    }
}
