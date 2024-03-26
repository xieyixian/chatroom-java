package top.javahai.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.io.Serializable;


public class GroupMsgContent implements Serializable {
    private static final long serialVersionUID = 980328865610261046L;

    private Integer id;

    private Integer fromId;

    private String fromName;

    private String fromProfile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String content;

    private Integer messageTypeId;

    private int type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromProfile() {
        return fromProfile;
    }

    public void setFromProfile(String fromProfile) {
        this.fromProfile = fromProfile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Integer messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GroupMsgContent{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", fromName='" + fromName + '\'' +
                ", fromProfile='" + fromProfile + '\'' +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                ", messageTypeId=" + messageTypeId +
                ", type=" + type +
                '}';
    }
}