package com.lovecoin.extra.facebook.entity;

import java.util.List;

/**
 * Created on 2017/11/12 0012.
 */

public class UserFriendsResp {

    public List<User> data;
    public Summary summary;
    //    public paging

    public static class Summary {
        public int total_count;
    }
}
