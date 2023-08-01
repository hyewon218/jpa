package me.hyewon.jpa.thread;

import java.util.List;
import me.hyewon.jpa.channel.Channel;
import me.hyewon.jpa.common.PageDTO;
import org.springframework.data.domain.Page;

public interface ThreadService {

  List<Thread> selectNotEmptyThreadList(Channel channel);

  Page<Thread> selectMentionedThreadList(Long userId, PageDTO pageDTO);

  Thread insert(Thread thread);
}