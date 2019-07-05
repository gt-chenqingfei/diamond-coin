package com.lovecoin.extra.facebook.callback;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.lovecoin.extra.facebook.entity.User;

public class GetUserCallback {

    public interface IGetUserResponse {
        void onCompleted(User user);
        void onError();
    }

    private IGetUserResponse mGetUserResponse;
    private GraphRequest.Callback mCallback;

    public GetUserCallback(final IGetUserResponse getUserResponse) {

        mGetUserResponse = getUserResponse;
        mCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getError() == null) {

                    User userFriendsResp = parse(response.getRawResponse());
                    if (userFriendsResp != null) {
                        mGetUserResponse.onCompleted(userFriendsResp);
                        return;
                    }
                }

                mGetUserResponse.onError();
            }
        };
    }

    private User parse(String rawResponse) {
        return new Gson().fromJson(rawResponse, User.class);
    }

    public GraphRequest.Callback getCallback() {
        return mCallback;
    }
}