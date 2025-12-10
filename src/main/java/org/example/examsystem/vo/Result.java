package org.example.examsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一返回信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result ok(){
        return new Result(200,"success",null);
    }
    public static Result ok(Object data){
        return new Result(200, "success", data);
    }
    public static Result fail(String errorMsg){
        return new Result(500, errorMsg, null);
    }
    public static Result info(Integer code,String message){return new Result(code,message,null);}
}
