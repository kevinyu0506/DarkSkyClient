package com.chuntingyu.weather.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chuntingyu.weather.activities.main.MainActivity;
import com.chuntingyu.weather.applications.BaseActivity;
import com.chuntingyu.weather.common.CommonUtils;
import com.chuntingyu.weather.applications.WeatherApp;
import com.chuntingyu.weather.R;
import com.chuntingyu.weather.tools.coredata.DataManager;

public class LoginActivity extends BaseActivity implements LoginMvpView {
    private static final String TAG = "LoginActivity";
    private LoginPresenterBase loginPresenter;
    private EditText emailTxt, passwordTxt;
    private Button loginBtn;

    TextInputLayout emailWrapper, passwordWrapper;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataManager dataManager = ((WeatherApp) getApplication()).getDataManager();
        loginPresenter = new LoginPresenterBase(dataManager);

        loginPresenter.onAttach(this);

//        ButterKnife.bind(this);

        emailTxt = findViewById(R.id.login_email_txt);
        passwordTxt = findViewById(R.id.login_password_txt);
        loginBtn = findViewById(R.id.login_next_btn);
        emailWrapper = findViewById(R.id.login_email_wrapper);
        passwordWrapper = findViewById(R.id.login_password_wrapper);
        emailWrapper.setHint("Email");
        passwordWrapper.setHint("Password");

        loginBtn.setOnClickListener(new View.OnClickListener() {
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

        String email = emailWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();

        if (!CommonUtils.isEmailValid(email)) {
            Toast.makeText(this, "Enter correct Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (password == null || password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        loginPresenter.startLogin(email);
    }
}
