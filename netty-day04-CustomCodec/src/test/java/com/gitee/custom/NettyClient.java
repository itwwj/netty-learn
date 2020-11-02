package com.gitee.custom;

import com.alibaba.fastjson.JSON;
import com.github.custom.model.User;
import com.github.custom.utils.HexConvertUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author jie
 */
public class NettyClient {
    public static void main(String[] args) throws IOException {
        Socket socket=null;
        OutputStream os=null;
        InputStream is=null;
        try {
            socket = new Socket("127.0.0.1", 1100);
            os = socket.getOutputStream();
            User user=new User();
            user.setName("axiba");
            user.setAge(18);
            byte[] bytes1 = JSON.toJSONBytes(user);
            os.write("bytes1".getBytes());
//            is = socket.getInputStream();
//            byte[] bytes = new byte[1024];
//            int read = is.read(bytes);
            //System.out.println(JSON.parseObject(bytes,User.class));
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
//            is.close();
//            os.close();
//            socket.close();
        }
    }
    @Test
    public void  test01(){
        System.out.println(HexConvertUtil.convertStringToHex("hihi"));

    }
}
