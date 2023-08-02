package me.hyewon.jpa.userchannel;

import me.hyewon.jpa.channel.Channel;
import me.hyewon.jpa.channel.ChannelRepository;
import me.hyewon.jpa.common.PageDTO;
import me.hyewon.jpa.user.User;
import me.hyewon.jpa.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserChannelRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  void userJoinChannelTest() {
    // given
    var newChannel = Channel.builder().name("new-channel").build();
    var newUser = User.builder().username("new_user").password("new-pass").build();
    userRepository.save(newUser);
    channelRepository.save(newChannel);
    var newUserChannel = newChannel.joinUser(newUser);

    // when
    // 각각의 entity 저장(영속성 상태)
    var savedChannel = channelRepository.save(newChannel);
    var savedUser = userRepository.save(newUser);

    // then
    var foundChannel = channelRepository.findById(savedChannel.getId());
    // 저장한 테이블의 UserChannels 에 우리가 설정한 채널의 이름과 같은 것이 있는지 확인
    assert foundChannel.get().getUserChannels().stream()
        .map(UserChannel::getChannel)
        .map(Channel::getName)
        .anyMatch(name -> name.equals(newChannel.getName()));
  }

  @Test
  void userJoinChannelWithCascadeTest() {
    // given
    var newChannel = Channel.builder().name("new-channel").build();
    var newUser = User.builder().username("new_user").password("new-pass").build();
    userRepository.save(newUser);
    channelRepository.save(newChannel);
    newChannel.joinUser(
        newUser); // userChannels cascade = CascadeType.ALL -> joinUser 만 하더라도 실제 저장이 된다!

    // when
    var savedChannel = channelRepository.save(newChannel);
    var savedUser = userRepository.save(newUser);

    // then
    var foundChannel = channelRepository.findById(savedChannel.getId());
    assert foundChannel.get().getUserChannels().stream()
        .map(UserChannel::getChannel)
        .map(Channel::getName)
        .anyMatch(name -> name.equals(newChannel.getName()));
  }

  @Test
  void userCustomFieldSortingTest() {
    // given
    var newUser1 = User.builder().username("new_user").password("new-pass1").build();
    var newUser2 = User.builder().username("new_user").password("new-pass2").build();
    userRepository.save(newUser1);
    userRepository.save(newUser2);

    // when
    var users = userRepository.findByUsernameWithCustomField("new_user", Sort.by("customField"));

    // then
    assert users.stream().map(User::getPassword)
        .anyMatch(password -> password.equals(newUser1.getPassword()));

    // when
    users = userRepository.findByUsernameWithCustomField("new_user",
        Sort.by("customField").descending());

    // then
    assert users.get(0).getPassword().equals(newUser2.getPassword());

    // JpaSort 를 사용해서 쿼리 함수를 기준으로 정렬
    var newUser3 = User.builder().username("new_user").password("3").build();
    userRepository.save(newUser3);

    // when
    users = userRepository.findByUsername("new_user",
        JpaSort.unsafe("LENGTH(password)"));

    // then
    assert users.get(0).getPassword().equals(newUser3.getPassword());
  }

  @Test
  void pageDTOTest() {
    // given
    var newUser1 = User.builder().username("new_user").password("new-pass1").build();
    var newUser2 = User.builder().username("new_user").password("new-pass2").build();
    var newUser3 = User.builder().username("new_user").password("new-pass3").build();
    userRepository.save(newUser1);
    userRepository.save(newUser2);
    userRepository.save(newUser3);
    var pageDTO = new PageDTO(1, 2, "password");

    // when
    var page = userRepository.findAll(pageDTO.toPageable());

    // then
    assert page.getContent().size() == 2;
  }
}
