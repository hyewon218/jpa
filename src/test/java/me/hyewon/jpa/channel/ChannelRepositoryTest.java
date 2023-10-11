package me.hyewon.jpa.channel;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import me.hyewon.jpa.common.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void insertSelectChannelTest() {
        // given
        var newChannel = Channel.builder().name("new-channel").build();

        // when
        var savedChannel = channelRepository.save(newChannel);

        // then
        var foundChannel = channelRepository.findById(savedChannel.getId());
        assert foundChannel.get().getId().equals(savedChannel.getId());
    }

    @Test
    public void queryDslTest() {
        // given
        var newChannel = Channel.builder().name("hyewon").build();
        channelRepository.save(newChannel);

        Predicate predicate = QChannel.channel
            .name.equalsIgnoreCase("HYEWON");

        // when
        Optional<Channel> optional = channelRepository.findOne(predicate);

        // then
        assert optional.get().getName().equals(newChannel.getName());
    }
}