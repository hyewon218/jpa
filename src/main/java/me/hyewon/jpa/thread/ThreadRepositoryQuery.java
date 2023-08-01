package me.hyewon.jpa.thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThreadRepositoryQuery {

  Page<Thread> search(ThreadSearchCond cond, Pageable pageable); // ThreadSearchCond : 조건들을 모아 넣는 곳

}
