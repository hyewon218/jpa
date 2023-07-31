package me.hyewon.jpa.user;

import me.hyewon.jpa.my.MyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, MyRepository<User> {

}