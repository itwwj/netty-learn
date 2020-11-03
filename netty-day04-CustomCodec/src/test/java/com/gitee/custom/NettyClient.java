package com.gitee.custom;

import com.github.custom.utils.HexConvertUtil;
import org.junit.Test;



/**
 * @author jie
 */
public class NettyClient {

    @Test
    public void  test01(){

        String netty = HexConvertUtil.convertStringToHex("netty");
        System.out.println(netty);
    }

}
