package jwcbook.jwcshop.domain.item;

import jwcbook.jwcshop.domain.Category;
import jwcbook.jwcshop.exception.NotEnoughStockException;
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

    //==비즈니스 로직==//
    //entity클래스에 핵심 비즈니스 로직을 구현하는 이유는?

    //값을 변경해야할 때 setter로 하는 것 보다, 비즈니스 로직을 구현하고 그것으로 데이터 처리하는게 가장 효과적

    //재고수량(stock) 증가 로직//
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    //재고 수량(stock) 감소 로직//
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock . .");
        }
        this.stockQuantity = restStock;

    }

    //==업데이트를 위한 변경 로직==//
    public void change(String name, int stockQuantity, int price) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


}
