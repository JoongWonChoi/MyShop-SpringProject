package jwcbook.jwcshop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") //싱글테이블 내에 저장될 때 DB에게 구분을 해주는 방법
@Getter
@Setter
public class Movie extends Item {
    private String director;
    private String actor;
}
