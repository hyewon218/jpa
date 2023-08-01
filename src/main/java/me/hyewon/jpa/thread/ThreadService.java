package me.hyewon.jpa.thread;

import java.util.List;
import me.hyewon.jpa.channel.Channel;

public interface ThreadService {

  List<Thread> selectNotEmptyThreadList(Channel channel);

  Thread insert(Thread thread);
}