package me.hyewon.jpa.channel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelRepository {

    @PersistenceContext // 스프링이 만들어둔 EntityManager 를 주입받을 때 사용
    EntityManager entityManager; // persist 명령 메소드를 통해 영속성 상태로 만들어서 저장, find 로 조회

    public Channel insertChannel(Channel channel) {
        entityManager.persist(channel);
        return channel;
    }

    public Channel selectChannel(Long id) {
        return entityManager.find(Channel.class, id);
    }
}
