package com.lucasurbas.facebooksdktest.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 26/01/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.activity_login__button_login) LoginButton loginButton;

    private CallbackManager callbackManager;

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupToolbar();
        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();
//        loginButton.setPublishPermissions("publish_actions");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(GalleryActivity.getIntent(LoginActivity.this));
                finish();
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                showToast("Error: " + exception.getMessage());
            }
        });

    }

    private void setupToolbar() {
        getSupportActionBar().setTitle(R.string.activity_login__title);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
