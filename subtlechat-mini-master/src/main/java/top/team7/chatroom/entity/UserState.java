package top.team7.chatroom.entity;

import java.io.Serializable;


public class UserState implements Serializable {
    private static final long serialVersionUID = -38130170610280885L;
    
    private Integer id;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}