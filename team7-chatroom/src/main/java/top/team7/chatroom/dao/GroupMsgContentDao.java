package top.team7.chatroom.dao;

import top.team7.chatroom.entity.GroupMsgContent;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface GroupMsgContentDao {


    GroupMsgContent queryById(Integer id);

    List<GroupMsgContent> queryAllByLimit(@Param("offset") Integer offset, @Param("limit") Integer limit);

    List<GroupMsgContent> queryAll(GroupMsgContent groupMsgContent);

    int insert(GroupMsgContent groupMsgContent);

    int update(GroupMsgContent groupMsgContent);

    int deleteById(Integer id);

    List<GroupMsgContent> getAllGroupMsgContentByPage(@Param("page") Integer page,
                                             @Param("size") Integer size,
                                             @Param("nickname") String nickname,
                                             @Param("type") Integer type,
                                             @Param("dateScope") Date[] dateScope);

    Long getTotal(@Param("nickname") String nickname,
                  @Param("type") Integer type,
                  @Param("dateScope") Date[] dateScope);

    Integer deleteGroupMsgContentByIds(@Param("ids") Integer[] ids);

    void deleteGroupMsgById(GroupMsgContent groupMsgContent);
}
