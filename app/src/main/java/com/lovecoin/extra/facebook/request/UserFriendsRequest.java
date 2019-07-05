package com.lovecoin.extra.facebook.request;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.Profile;

/**
 * doc:
 * https://developers.facebook.com/docs/graph-api/reference/user/friends
 * <p>
 * This edge allows you to:
 * <p>
 * get the User's friends who have installed the app making the query
 * get the User's total number of friends (including those who have not installed the app making the query)
 */

public class UserFriendsRequest {

    public static void makeRequest(GraphRequest.Callback callback) {
        String userId = Profile.getCurrentProfile().getId();
        String endPoint = String.format("/%s/friends", userId);

        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                endPoint,
                null,
                HttpMethod.GET,
                callback
        );
        request.executeAsync();
    }
}
