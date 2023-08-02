package me.hyewon.jpa.user;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QueryByExampleExecutor<User> {

  // @Query 사용시 Alias(쿼리에서 as 로 지정한 문구) 를 기준으로 정렬할 수 있다.
  // 아래와 같이 AS user_password 로 Alias(AS) 를 걸어주면
  @Query("SELECT u, u.password AS customField FROM User u WHERE u.username = ?1")
  List<User> findByUsernameWithCustomField(String username, Sort sort);

  // JpaSort 를 사용해서 쿼리 함수를 기준으로 정렬할 수 있다.
  // 아래와 같이 일반적인 쿼리에서
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  List<User> findByUsername(String username, Sort sort);

  List<UserProfile> findByUsername(String username);

  List<UserInfo> findByPassword(String password);

  <T> List<T> findByProfileImageUrlStartingWith(String profileImageUrlStartWith, Class<T> type);
}