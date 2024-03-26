package top.team7.chatroom.service;

import top.team7.chatroom.entity.GroupMsgContent;
import top.team7.chatroom.entity.RespPageBean;

import java.util.Date;
import java.util.List;

public interface GroupMsgContentService {


    GroupMsgContent queryById(Integer id);


    List<GroupMsgContent> queryAllByLimit(Integer offset, Integer limit);


    GroupMsgContent insert(GroupMsgContent groupMsgContent);


    GroupMsgContent update(GroupMsgContent groupMsgContent);

    boolean deleteById(Integer id);

    RespPageBean getAllGroupMsgContentByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope);

    Integer deleteGroupMsgContentByIds(Integer[] ids);

    void deleteGroupMsgById(GroupMsgContent groupMsgContent);
}
