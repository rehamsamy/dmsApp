package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.googleplay.AndroidPublisherHelper;
import com.dmsegypt.dms.rest.googleplay.ApplicationConfig;
import com.google.api.services.androidpublisher.AndroidPublisher;

import java.io.IOException;
import java.security.GeneralSecurityException;

import butterknife.ButterKnife;

/**
 * Created by amr on 25/03/2018.
 */

public class ReviewsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        try {
            AndroidPublisher publisher=
                     AndroidPublisherHelper.init(this,
                    ApplicationConfig.APPLICATION_NAME, ApplicationConfig.SERVICE_ACCOUNT_EMAIL);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_review;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.review_title;
    }
}
