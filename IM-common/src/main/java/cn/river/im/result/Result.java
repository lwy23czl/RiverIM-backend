package cn.river.im.result;

import cn.river.im.enums.ApiCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    //描述统一格式中的数据
    private T data;
    //描述统一格式中的编码，用于区分操作，可以简化配置0或1表示成功失败
    private Integer status;
    //描述统一格式中的消息，可选属性
    private String msg;
    //描述是否成功
    private boolean success;

    public Result(){}

    public Result(Integer status,boolean success){
        this.status=status;
        this.success=success;
    }
    public Result(Integer status,boolean success,String msg){
        this.status=status;
        this.success=success;
        this.msg=msg;
    }
    public Result(Integer status,boolean success,String msg,T data){
        this.status=status;
        this.success=success;
        this.msg=msg;
        this.data=data;
    }
    public Result(Integer status,boolean success,T data){
        this.status=status;
        this.success=success;
        this.data=data;
    }

    //无数据的成功结果
    public static Result<Boolean> ok(){
        return new Result<>(ApiCode.SUCCESS.getCode(),true,ApiCode.SUCCESS.getMessage());
    }
    //有数据的成功结果
    public static <T> Result<T> ok(T data){
        return new Result<>(ApiCode.SUCCESS.getCode(),true,ApiCode.SUCCESS.getMessage(),data);
    }
    //自定义状态码与消息
    public static <T> Result<T> ok(Integer status,String msg){
        return new Result<>(status,true,msg);
    }
    //自定义状态、消息,有数据的成功结果
    public static <T> Result<T> ok(Integer status,String msg,T data){
        return new Result<>(status,true,msg,data);
    }

    //无数据的失败结果
    public static Result<Boolean> fail(){
        return new Result<>(ApiCode.FAIL.getCode(),false,ApiCode.FAIL.getMessage());
    }
    //有数据的失败结果
    public static <T> Result<T> fail(T data){
        return new Result<>(ApiCode.FAIL.getCode(), false, ApiCode.FAIL.getMessage(),data);
    }
    //自定义状态码与消息
    public static <T> Result<T> fail(Integer status,String msg){
        return new Result<>(status,false,msg);
    }



}
