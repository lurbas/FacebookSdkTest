package com.lucasurbas.facebooksdktest.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.ui.util.BaseActivity;

/**
 * Created by Lucas on 26/01/2017.
 */

public class LoginActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupToolbar();
//        ButterKnife.bind(this);
    }

    private void setupToolbar() {
        getSupportActionBar().setTitle(R.string.activity_login__title);
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {

    }
}
