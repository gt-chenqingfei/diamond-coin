package com.lovecoin.extra.facebook.callback;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.lovecoin.extra.facebook.entity.UserFriendsResp;

/**
 * Created on 2017/11/12 0012.
 */

public class GetUserFriendsCallback {

    public interface IGetUserFriendsResponse {
        void onCompleted(UserFriendsResp userFriendsResp);

        void onError();
    }

    private GetUserFriendsCallback.IGetUserFriendsResponse mGetUserFriendsResponse;
    private GraphRequest.Callback mCallback;

    public GetUserFriendsCallback(GetUserFriendsCallback.IGetUserFriendsResponse getUserFriendsResponse) {

        mGetUserFriendsResponse = getUserFriendsResponse;
        mCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getError() == null) {

                    //{"data":[],"summary":{"total_count":0}}
                    UserFriendsResp userFriendsResp = parse(response.getRawResponse());
                    if (userFriendsResp != null) {
                        mGetUserFriendsResponse.onCompleted(userFriendsResp);
                        return;
                    }
                }

                mGetUserFriendsResponse.onError();
            }
        };
    }

    private UserFriendsResp parse(String rawResponse) {
        return new Gson().fromJson(rawResponse, UserFriendsResp.class);
    }

    public GraphRequest.Callback getCallback() {
        return mCallback;
    }
}
