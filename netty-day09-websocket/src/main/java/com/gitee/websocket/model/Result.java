package com.gitee.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;
    private String title;
    private String msg;


    public static Result ok(){
       return new Result(true,"ok",null);
    }
    public static Result ok(String data){
        return new Result(true,"ok",data);
    }
    public static Result eror(String data){
        return new Result(false,"error",data);
    }
}
