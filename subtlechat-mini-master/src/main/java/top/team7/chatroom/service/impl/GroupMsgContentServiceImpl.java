package top.team7.chatroom.service.impl;

import cn.hutool.core.date.DateUtil;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import top.team7.chatroom.constant.RedisKeyConstant;
import top.team7.chatroom.dao.GroupMsgContentDao;
import top.team7.chatroom.entity.GroupMsgContent;
import top.team7.chatroom.entity.RespPageBean;
import top.team7.chatroom.service.GroupMsgContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("groupMsgContentService")
public class GroupMsgContentServiceImpl implements GroupMsgContentService {
    @Resource
    private GroupMsgContentDao groupMsgContentDao;

    @Resource
    private RedissonClient redisson;


    @Override
    public GroupMsgContent queryById(Integer id) {
        return this.groupMsgContentDao.queryById(id);
    }


    @Override
    public List<GroupMsgContent> queryAllByLimit(Integer offset, Integer limit) {
        if (offset == null && limit == null) {
            String key = getGroupMsgKey();
            RList<GroupMsgContent> list = redisson.getList(key);
            if (list != null && !list.isEmpty()) {
                return list.readAll();
            }
        }
        List<GroupMsgContent> retList = this.groupMsgContentDao.queryAllByLimit(offset, limit);
        if (retList != null && !retList.isEmpty() && offset == null && limit == null) {
            RList<GroupMsgContent> list = redisson.getList(getGroupMsgKey());
            list.addAll(retList);
            list.expire(1L, TimeUnit.DAYS);
        }
        return retList;
    }

    private String getGroupMsgKey() {
        return String.format(RedisKeyConstant.GROUP_MSG_LIST_KEY, DateUtil.format(new Date(), "yyyyMMdd"));
    }


    @Override
    public GroupMsgContent insert(GroupMsgContent groupMsgContent) {
        this.groupMsgContentDao.insert(groupMsgContent);
        String key = getGroupMsgKey();
        RList<GroupMsgContent> list = redisson.getList(key);
        if (list.isExists()) {
            list.add(groupMsgContent);
        }
        return groupMsgContent;
    }

    @Override
    public GroupMsgContent update(GroupMsgContent groupMsgContent) {
        this.groupMsgContentDao.update(groupMsgContent);
        return this.queryById(groupMsgContent.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.groupMsgContentDao.deleteById(id) > 0;
    }

    @Override
    public RespPageBean getAllGroupMsgContentByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<GroupMsgContent> allGroupMsgContentByPage = groupMsgContentDao.getAllGroupMsgContentByPage(page, size, nickname, type, dateScope);
        Long total = groupMsgContentDao.getTotal(nickname, type, dateScope);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(allGroupMsgContentByPage);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    @Override
    public Integer deleteGroupMsgContentByIds(Integer[] ids) {
        return groupMsgContentDao.deleteGroupMsgContentByIds(ids);
    }

    @Override
    public void deleteGroupMsgById(GroupMsgContent groupMsgContent) {
        groupMsgContentDao.deleteGroupMsgById(groupMsgContent);
    }
}
