package jwcbook.jwcshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY) //참조되는 외래키의 기준이 되는 키!
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //Ordinal을 씀녀 안됨!
    private DeliveryStatus status; //Ready, Comp
}
