package me.hyewon.jpa.thread;

import static me.hyewon.jpa.thread.QThread.thread;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ThreadRepositoryQueryImpl implements ThreadRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Thread> search(ThreadSearchCond cond, Pageable pageable) {
    var query = query(thread, cond)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    query.orderBy(thread.mentions.any().createdAt.desc()); // 멘션된 시간 기준 내림차순

    var threads = query.fetch();
    long totalSize = countQuery(cond).fetch().get(0);

    // 쓰레드 댓글목록의 이모지목록 정보
    threads.stream()
        .map(Thread::getComments)
        .forEach(comments -> comments
            .forEach(comment -> Hibernate.initialize(
                comment.getEmotions()))); // initialize : 강제적으로 영속성 가져옴(DB 조회)

    return PageableExecutionUtils.getPage(threads, pageable, () -> totalSize);
  }

  private <T> JPAQuery<T> query(Expression<T> expr, ThreadSearchCond cond) {
    return jpaQueryFactory.select(expr)
        .from(thread)
        .leftJoin(thread.channel).fetchJoin()
        .leftJoin(thread.emotions).fetchJoin() // 이모지 정보
        .leftJoin(thread.comments).fetchJoin() // 댓글 정보
        .leftJoin(thread.mentions).fetchJoin() // 멘션 정보
        .where(
            channelIdEq(cond.getChannelId()),
            mentionedUserIdEq(cond.getMentionedUserId())
        );
  }

  private JPAQuery<Long> countQuery(ThreadSearchCond cond) {
    return jpaQueryFactory.select(Wildcard.count)
        .from(thread)
        .where(
            channelIdEq(cond.getChannelId()),
            mentionedUserIdEq(cond.getMentionedUserId())
        );
  }

  private BooleanExpression channelIdEq(Long channelId) { //cond 에 조건이 없더라도 에러가 나지 않도록
    return Objects.nonNull(channelId) ? thread.channel.id.eq(channelId) : null;
  }

  private BooleanExpression mentionedUserIdEq(Long mentionedUserId) {
    return Objects.nonNull(mentionedUserId) ? thread.mentions.any().user.id.eq(mentionedUserId)
        : null;
  }
}