package com.entropy_factory.activismap.ui.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.util.FormUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    public static final int LOGIN_USER = 10;
    public static final String USER_ID = "user_id";

    private EditText personName;
    private EditText username;
    private EditText email;
    private EditText repassword;
    private EditText password;
    private TextView question;
    private Button actionButton;
    private TextInputLayout tilPersonName;
    private TextInputLayout tilUsername;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilRepassword;

    private Activis activis;
    private boolean loginForm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activis = new Activis(this);

        personName = (EditText) findViewById(R.id.person_name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);

        tilPersonName = (TextInputLayout)  findViewById(R.id.til_person_name);
        tilUsername = (TextInputLayout)  findViewById(R.id.til_username);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilRepassword = (TextInputLayout) findViewById(R.id.til_repassword);

        question = (TextView) findViewById(R.id.account_question);
        View forgotPassword = findViewById(R.id.forgot_password);

        actionButton = (Button) findViewById(R.id.login);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginForm) {
                    login();
                } else {
                    register();
                }
            }
        });

        User u = User.getUser();
        if (u != null) {
            username.setText(u.getName());
        }

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForm();
            }
        });
        toggleForm();

    }

    private void toggleForm() {
        loginForm = !loginForm;

        if (loginForm) {
            tilPersonName.setVisibility(View.GONE);
            tilRepassword.setVisibility(View.GONE);
            tilEmail.setVisibility(View.GONE);
            question.setText(R.string.no_account);
            actionButton.setText(R.string.sign_in);
            tilUsername.setHint(getString(R.string.username_or_email));
            username.requestFocus();
        } else {
            tilPersonName.setVisibility(View.VISIBLE);
            tilRepassword.setVisibility(View.VISIBLE);
            tilEmail.setVisibility(View.VISIBLE);
            question.setText(R.string.has_account);
            actionButton.setText(R.string.sign_up);
            tilUsername.setHint(getString(R.string.username));
            email.requestFocus();
        }

    }

    private void login() {
        boolean hasError = false;

        if (FormUtils.isEmpty(username)) {
            hasError = true;
            tilUsername.setError(getString(R.string.mandatory_field));
        }

        if (FormUtils.isEmpty(password)) {
            hasError = true;
            tilPassword.setError(getString(R.string.mandatory_field));
        }

        if (!hasError) {
            String username = this.username.getText().toString();
            String password = this.password.getText().toString();

            ActivisListener<User> listener = new ActivisListener<User>() {
                @Override
                public void onResponse(ActivisResponse<User> response) {
                    if (response.hasError()) {
                        setResult(RESULT_CANCELED);
                    } else {
                        User user = response.getElementAt(0);
                        Intent data = new Intent();
                        data.putExtra(USER_ID, user.getServerId());
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            };

            activis.login(username, password, listener);
        }
    }

    private void register() {
        boolean hasError = false;

        if (FormUtils.isEmpty(username)) {
            hasError = true;
            tilUsername.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR USERNAME");
        }

        if (FormUtils.isEmpty(password)) {
            hasError = true;
            tilPassword.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR PASSWORD");
        }

        if (FormUtils.isEmpty(repassword)) {
            hasError = true;
            tilRepassword.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR REPASSWORD");
        }

        if (!FormUtils.equals(password, repassword)) {
            hasError = true;
            tilPassword.setError(getString(R.string.passwords_must_match));
            tilRepassword.setError(getString(R.string.passwords_must_match));
            Log.e(TAG, "ERROR PASSWRDS");
        }

        if (!FormUtils.email(email)) {
            hasError = true;
            tilEmail.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR EMAIL");
        }

        Log.e(TAG, "ERROR =" + hasError);
        if (!hasError) {
            String personName = this.personName.getText().toString();
            String username = this.username.getText().toString();
            String password = this.password.getText().toString();
            String repassword = this.repassword.getText().toString();
            String email = this.email.getText().toString();

            ActivisListener<User> listener = new ActivisListener<User>() {
                @Override
                public void onResponse(ActivisResponse<User> response) {
                    if (response.hasError()) {
                        setResult(RESULT_CANCELED);
                    } else {
                        User user = response.getElementAt(0);
                        Intent data = new Intent();
                        data.putExtra(USER_ID, user.getServerId());
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            };

            activis.register(username, password, repassword, email, personName, listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}