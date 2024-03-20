package top.javahai.chatroom.dao;

import org.apache.ibatis.annotations.Param;
import top.javahai.chatroom.entity.Feedback;


import java.util.List;


public interface FeedbackDao {

    Feedback queryById(String id);

    List<Feedback> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<Feedback> queryAll(Feedback feedback);

    int insert(Feedback feedback);

    int update(Feedback feedback);

    int deleteById(String id);

}
