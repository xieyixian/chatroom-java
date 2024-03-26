package top.team7.chatroom.controller;

import top.team7.chatroom.entity.GroupMsgContent;
import top.team7.chatroom.entity.RespPageBean;
import top.team7.chatroom.service.GroupMsgContentService;
import top.team7.chatroom.entity.RespBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/groupMsgContent")
public class GroupMsgContentController {

    @Resource
    private GroupMsgContentService groupMsgContentService;

    @GetMapping("/")
    private List<GroupMsgContent> getAllGroupMsgContent() {
        return groupMsgContentService.queryAllByLimit(null, null);
    }


    @GetMapping("selectOne")
    public GroupMsgContent selectOne(Integer id) {
        return this.groupMsgContentService.queryById(id);
    }


    @GetMapping("/page")
    public RespPageBean getAllGroupMsgContentByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    String nickname, Integer type,
                                                    Date[] dateScope) {
        return groupMsgContentService.getAllGroupMsgContentByPage(page, size, nickname, type, dateScope);
    }


    @DeleteMapping("/{id}")
    public RespBean deleteGroupMsgContentById(@PathVariable Integer id) {
        if (groupMsgContentService.deleteById(id)) {
            return RespBean.ok("successfully deleted！");
        } else {
            return RespBean.error("failed to delete！");
        }
    }

    @DeleteMapping("/")
    public RespBean deleteGroupMsgContentByIds(Integer[] ids) {
        if (groupMsgContentService.deleteGroupMsgContentByIds(ids) == ids.length) {
            return RespBean.ok("successfully deleted！");
        } else {
            return RespBean.error("failed to delete！");
        }
    }

    @RequestMapping("/deleteGroupMsgById")
    public void deleteGroupMsgById(@RequestBody GroupMsgContent groupMsgContent) {
        groupMsgContentService.deleteGroupMsgById(groupMsgContent);
    }
}
