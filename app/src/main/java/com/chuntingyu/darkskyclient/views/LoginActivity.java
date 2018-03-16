package com.chuntingyu.darkskyclient.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chuntingyu.darkskyclient.CommonUtils;
import com.chuntingyu.darkskyclient.MvpApp;
import com.chuntingyu.darkskyclient.R;
import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.presenters.LoginPresenter;
import com.gc.materialdesign.views.ButtonRectangle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    LoginPresenter loginPresenter;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.buttonLogin)
    ButtonRectangle buttonLogin;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataManager dataManager = ((MvpApp) getApplication()).getDataManager();
        loginPresenter = new LoginPresenter(dataManager);

        loginPresenter.onAttach(this);

        ButterKnife.bind(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClick();
            }
        });

    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginButtonClick() {

        String emailId = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!CommonUtils.isEmailValid(emailId)) {
            Toast.makeText(this, "Enter correct Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (password == null || password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        loginPresenter.startLogin(emailId);

    }
}
