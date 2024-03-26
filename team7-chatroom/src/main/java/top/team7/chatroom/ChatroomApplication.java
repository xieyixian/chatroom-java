package top.team7.chatroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("top.team7.chatroom.dao")
@EnableScheduling
public class ChatroomApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChatroomApplication.class, args);
  }
}
