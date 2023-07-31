package me.hyewon.jpa.my;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

// Repository 기능을 제한
@NoRepositoryBean // 상위 인터페이스 개념을 하나 더 만들어서 열어줄 메소드만 선언해준다.
public interface MyRepository<User, ID extends Serializable> extends Repository<User, ID> {

  Optional<User> findByUsername(String username);
}
