package me.hyewon.jpa.thread;

import java.util.List;
import me.hyewon.jpa.channel.Channel;
import me.hyewon.jpa.channel.Channel.Type;
import me.hyewon.jpa.channel.ChannelRepository;
import me.hyewon.jpa.comment.Comment;
import me.hyewon.jpa.comment.CommentRepository;
import me.hyewon.jpa.common.PageDTO;
import me.hyewon.jpa.mention.ThreadMention;
import me.hyewon.jpa.user.User;
import me.hyewon.jpa.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThreadServiceImplTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  ThreadService threadService;

  @Autowired
  CommentRepository commentRepository;

  @Test
  void getMentionedThreadList() {
    // given
    User savedUser = getTestUser("1", "2");
    var newThread = Thread.builder().message("message").build();
    newThread.addMention(savedUser);
    threadService.insert(newThread);

    var newThread2 = Thread.builder().message("message2").build();
    newThread2.addMention(savedUser);
    threadService.insert(newThread2);

    // when
    // 모든 채널에서 내가 멘션된 쓰레드 목록 조회 기능
    var mentionedThreads = savedUser.getThreadMentions().stream().map(ThreadMention::getThread)
        .toList();

    // then
    assert mentionedThreads.containsAll(List.of(newThread, newThread2));
  }

  @Test
  void getNotEmptyThreadList() {
    // given
    var newChannel = Channel.builder().name("c1").type(Type.PUBLIC).build();
    var savedChannel = channelRepository.save(newChannel);
    getTestThread("message", savedChannel);

    Thread newThread2 = getTestThread("", savedChannel);

    // when
    var notEmptyThreads = threadService.selectNotEmptyThreadList(savedChannel);

    // then
    assert !notEmptyThreads.contains(newThread2); // newThread2 가 있으면 안 됨!
  }

  @Test
  @DisplayName("전체 채널에서 내가 멘션된 쓰레드 상세정보 목록 테스트")
  void selectMentionedThreadListTest() {
    // given
    var user = getTestUser("1", "1"); // 멘션된 user
    var user2 = getTestUser("2", "2"); // 이모지 단 user
    var user3 = getTestUser("3", "3"); // 댓글 단 user
    var user4 = getTestUser("4", "4"); // 댓글에 이모지 단 user
    var newChannel = Channel.builder().name("c1").type(Type.PUBLIC).build();
    var savedChannel = channelRepository.save(newChannel);
    var thread1 = getTestThread("message", savedChannel, user
        , user2, "e1", user3, "c1", user4, "ce1");
    var thread2 = getTestThread("", savedChannel, user
        , user2, "e2", user3, "c2", user4, "ce2");

    // when
    var pageDTO = PageDTO.builder().currentPage(1).size(100).build();
    var mentionedThreadList = threadService.selectMentionedThreadList(user.getId(), pageDTO);

    // then
    assert mentionedThreadList.getTotalElements() == 2;
  }

  // 작성자
  private User getTestUser(String username, String password) {
    var newUser = User.builder().username(username).password(password).build();
    return userRepository.save(newUser);
  }

  // 댓글
  private Comment getTestComment(User user, String message) {
    var newComment = Comment.builder().message(message).build();
    newComment.setUser(user);
    return commentRepository.save(newComment);
  }

  // 쓰레드 본문
  private Thread getTestThread(String message, Channel savedChannel) {
    var newThread = Thread.builder().message(message).build();
    newThread.setChannel(savedChannel);
    return threadService.insert(newThread);
  }

  // 멘션된 쓰레드목록 정보
  private Thread getTestThread(String message, Channel channel, User mentionedUser) {
    var newThread = getTestThread(message, channel);
    newThread.addMention(mentionedUser);
    return threadService.insert(newThread);
  }

  // 쓰레드 이모지목록 정보
  private Thread getTestThread(String message, Channel channel, User mentionedUser,
      User emotionUser, String emotionValue) {
    var newThread = getTestThread(message, channel, mentionedUser);
    newThread.addEmotion(emotionUser, emotionValue);
    return threadService.insert(newThread);
  }

  // 쓰레드 댓글목록 정보
  private Thread getTestThread(String message, Channel channel, User mentionedUser,
      User emotionUser, String emotionValue, User commentUser, String commentMessage) {
    var newThread = getTestThread(message, channel, mentionedUser, emotionUser, emotionValue);
    newThread.addComment(getTestComment(commentUser, commentMessage)); // 댓글 넣어줌
    return threadService.insert(newThread);
  }

  // 쓰레드 댓글목록의 이모지목록 정보
  private Thread getTestThread(String message, Channel channel, User mentionedUser,
      User emotionUser, String emotionValue, User commentUser, String commentMessage,
      User commentEmotionUser, String commentEmotionValue) {
    var newThread = getTestThread(message, channel, mentionedUser, emotionUser, emotionValue,
        commentUser, commentMessage);
    newThread.getComments() // 넣어준 댓글 꺼내서 이모지 넣어주기
        .forEach(comment -> comment.addEmotion(commentEmotionUser, commentEmotionValue));
    return threadService.insert(newThread);
  }
}