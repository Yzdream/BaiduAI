package com.yz.baiduai;

import com.yz.utils.Md5Util;
import com.yz.utils.StringMapUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        List<String> al = Arrays.asList("a", "b", "c", "d");
    }

    @Test
    public void test(){
        Map<String,String> map = new HashMap<>();
        map.put("mch_id","123456");
        map.put("bank","1");
        map.put("card_no","123456");
        //bank=1&card_no=123456&mch_id=123456
        System.out.println(Md5Util.MD5(StringMapUtils.createLinkString(map)));
    }
}