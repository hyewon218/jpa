package me.hyewon.jpa.channel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hyewon.jpa.common.Timestamp;
import me.hyewon.jpa.thread.Thread;
import me.hyewon.jpa.user.User;
import me.hyewon.jpa.userchannel.UserChannel;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
public class Channel extends Timestamp {

  /**
   * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING) // ORDINAL 은 실무에서 잘 쓰지 않음.(순서 바뀔 가능성 O)
  private Type type;

  public enum Type {
    PUBLIC, PRIVATE // 공개채널, 비공개채널
  }

  /**
   * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
   */
  @Builder
  public Channel(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  /**
   * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
   */
  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Thread> threads = new LinkedHashSet<>(); // LinkedHashSet 은 중복방지 & 순서보장(저장한 순서대로 불러옴)

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserChannel> userChannels = new LinkedHashSet<>();

  /**
   * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
   */
  public void addThread(Thread thread) { // Thread - Channel
    this.threads.add(thread);
  }

  public UserChannel joinUser(User user) { // User - Channel
    var userChannel = UserChannel.builder().user(user).channel(this).build();
    this.userChannels.add(userChannel);
    user.getUserChannels().add(userChannel); // user 쪽에도 userChannel 넣어준다.
    return userChannel;
  }

    /*
      서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */

  /**
   * 라이프 사이클 메소드
   */
  @PrePersist
  public void prePersist() {
    super.updateModifiedAt();
    super.updateCreatedAt();
  }

  @PreUpdate
  public void PreUpdate() {
    super.updateModifiedAt();
  }
}
