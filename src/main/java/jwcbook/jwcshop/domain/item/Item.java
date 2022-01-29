package jwcbook.jwcshop.domain.item;

import jwcbook.jwcshop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //전략은 부모 클래스에서 잡아줘야함. 여기선 한 테이블에 모두 몰아넣는 SINGLETABLE 전략 사용
@DiscriminatorColumn(name="dtype")
@Getter @Setter
public abstract class Item { //상속관계 매핑 --  ㅎㅏ위에 아이템들 생성
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
