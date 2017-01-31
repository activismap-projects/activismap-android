package com.entropy_factory.activismap.ui.tool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.util.FormUtils;

import static com.entropy_factory.activismap.ui.tool.LoginActivity.LOGIN_USER;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    public static final int REGISTER_USER = 10;
    public static final String USER_ID = "user_id";

    private EditText personName;
    private EditText username;
    private EditText password;
    private EditText repassword;
    private EditText email;
    private Activis activis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        activis = new Activis(this);
        personName = (EditText) findViewById(R.id.person_name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);

        View btnSend = findViewById(R.id.btn_register);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        View btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activis.openLoginScreen();
            }
        });
    }

    private void send() {
        boolean hasError = false;

        if (FormUtils.isEmpty(username)) {
            hasError = true;
            username.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR USERNAME");
        }

        if (FormUtils.isEmpty(password)) {
            hasError = true;
            password.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR PASSWORD");
        }

        if (FormUtils.isEmpty(repassword)) {
            hasError = true;
            repassword.setError(getString(R.string.mandatory_field));
            Log.e(TAG, "ERROR REPASSWORD");
        }

        if (!FormUtils.equals(password, repassword)) {
            hasError = true;
            password.setError(getString(R.string.passwords_must_match));
            repassword.setError(getString(R.string.passwords_must_match));
            Log.e(TAG, "ERROR PASSWRDS");
        }

        if (!FormUtils.email(email)) {
            hasError = true;
            email.setError(getString(R.string.mandatory_field));
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
            if (requestCode == LOGIN_USER) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}