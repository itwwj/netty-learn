package com.github.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TODO
 *
 * @author jie
 * @date 2020/8/14 22:42
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket=null;
        OutputStream os=null;
        InputStream is=null;
        try {
             socket = new Socket("127.0.0.1", 1100);
             os = socket.getOutputStream();
             os.write("axiba".getBytes());
             is = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = is.read(bytes);
            System.out.println(new String(bytes,0,read));
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            is.close();
            os.close();
            socket.close();
        }
    }
}
