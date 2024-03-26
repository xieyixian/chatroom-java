package top.team7.chatroom.exception;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.team7.chatroom.entity.RespBean;

import java.sql.SQLException;


@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(SQLException.class)
  public RespBean sqlExceptionHandler(SQLException e){
    if (e instanceof MySQLIntegrityConstraintViolationException){
      return RespBean.error("This data is related to other data and cannot be deleted！");
    } else {
      e.printStackTrace();
      return RespBean.error("Database exception, operation failed！");
    }
  }
}
