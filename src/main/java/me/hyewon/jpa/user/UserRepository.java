package me.hyewon.jpa.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    public User insertUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public User selectUser(Long id) {
        return entityManager.find(User.class, id);
    }
}