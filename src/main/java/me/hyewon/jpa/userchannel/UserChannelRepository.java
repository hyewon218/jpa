package me.hyewon.jpa.userchannel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserChannelRepository {

    @PersistenceContext
    EntityManager entityManager;


    public UserChannel insertUserChannel(UserChannel userChannel) {
        entityManager.persist(userChannel);
        return userChannel;
    }

    public UserChannel selectUserChannel(Long id) {
        return entityManager.find(UserChannel.class, id);
    }
}
