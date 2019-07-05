package com.lovecoin.extra.facebook;

import com.facebook.Profile;
import com.facebook.ProfileTracker;

/**
 * Created on 2017/11/12 0012.
 */

public class UserProfileTracker extends ProfileTracker {

    private UserProfileChangedListener listener;

    public interface UserProfileChangedListener {
        void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile);
    }

    public UserProfileTracker(UserProfileChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
        if (listener != null) {
            listener.onCurrentProfileChanged(oldProfile, currentProfile);
        }

        this.stopTracking();
    }
}
