package com.gitee.iot.ssl;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jie
 */
public class StreamReader {
    public String toByteArray(InputStream fin)
    {
        int i = -1;
        StringBuilder buf = new StringBuilder();

        try {
            while((i=fin.read())!=-1){
                if(buf.length()>0) {
                    buf.append(",");
                }
                buf.append("(byte)");
                buf.append(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buf.toString();
    }
}
