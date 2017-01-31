package com.entropy_factory.activismap.ui.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.util.FormUtils;

import static com.entropy_factory.activismap.ui.tool.RegisterActivity.REGISTER_USER;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    public static final int LOGIN_USER = 10;
    public static final String USER_ID = "user_id";

    private EditText username;
    private EditText password;
    private Activis activis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activis = new Activis(this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        User u = User.getUser();
        if (u != null) {
            username.setText(u.getName());
        }

        View btnSend = findViewById(R.id.btn_login);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        View btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activis.openRegisterScreen();
            }
        });
    }

    private void send() {
        boolean hasError = false;

        if (FormUtils.isEmpty(username)) {
            hasError = true;
            username.setError(getString(R.string.mandatory_field));
        }

        if (FormUtils.isEmpty(password)) {
            hasError = true;
            password.setError(getString(R.string.mandatory_field));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_OK) {
            if (requestCode == REGISTER_USER) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}