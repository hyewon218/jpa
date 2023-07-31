package me.hyewon.jpa.my;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

// Repository 기능을 추가
@NoRepositoryBean
public interface MyRepository<T> {

  void delete(T entity);

  List<String> findNameAll();
}
