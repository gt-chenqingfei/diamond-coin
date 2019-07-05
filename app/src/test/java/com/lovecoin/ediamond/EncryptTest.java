package com.lovecoin.ediamond;

import com.lovecoin.ediamond.api.utils.EncryptUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created on 2017/11/19 0019.
 */
@RunWith(JUnit4.class)
public class EncryptTest {

    @Test
    public void testGet() {
        String params = "111";
        String encodedParams = EncryptUtils.encode(params, "hiKrHs0kEDhadHy12QAnv7I9hzhdj8An");
        System.out.println(encodedParams);

        String decode = EncryptUtils
                .decode("8NDp0Ya2iOo0vPnmX/goZJj1nRcMZkmsUpLIV+S+ZOPhAsNbKpgEBjQ6svYkp198dcRRjfH4i+dcucpD9ZVgBQ==", "hiKrHs0kEDhadHy12QAnv7I9hzhdj8An");
        System.out.println(decode);
    }
}
