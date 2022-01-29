package jwcbook.jwcshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { //Model : 이 객체에 데이털르 싣고 view에 전달 가능능
       model.addAttribute("data", "hello!");
        return "hello";
    }

}
