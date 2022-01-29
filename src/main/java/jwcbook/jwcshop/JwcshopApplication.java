package jwcbook.jwcshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwcshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwcshopApplication.class, args);
		Hello hello = new Hello();
		hello.setData("thymeleaf success");
		System.out.println(hello.getData());


	}

}
