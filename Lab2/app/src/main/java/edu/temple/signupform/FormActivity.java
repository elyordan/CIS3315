package edu.temple.signupform;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirmation;
    private TextView userTextView;
    private TextView emailTextView;
    private  TextView passTextView;
    private TextView passConfirmTextView;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = findViewById(R.id.editTextUsername);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputPasswordConfirmation = findViewById(R.id.editTextPasswordConfirmation);
        userTextView = findViewById(R.id.userTextView);
        emailTextView = findViewById(R.id.emailTextView);
        passTextView = findViewById(R.id.passTextView);
        passConfirmTextView = findViewById(R.id.passConfirmTextView);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String USERNAME = inputUsername.getText().toString();
                String EMAIL = inputUsername.getText().toString();
                String PASSWORD = inputUsername.getText().toString();
                String PASSWORDCONFIRM = inputUsername.getText().toString();

                //Toast.makeText(FormActivity.this, "Yes it is working!", Toast.LENGTH_LONG ).show();

                if (USERNAME.isEmpty()) {
                    userTextView.setTextColor(Color.RED);
                    userTextView.setText("Username can not be blank!");
                } else {
                    userTextView.setVisibility(View.GONE);
                }
                if (EMAIL.toString().isEmpty()) {
                    emailTextView.setTextColor(Color.RED);
                    emailTextView.setText("Email can not be blank!");
                } else {
                    emailTextView.setVisibility(View.GONE);
                }
                if (PASSWORD.toString().isEmpty()) {
                    passTextView.setTextColor(Color.RED);
                    passTextView.setText("Password can not be blank!");
                } else {
                    passTextView.setVisibility(View.GONE);
                }
                if (PASSWORDCONFIRM.toString().isEmpty()) {
                    passConfirmTextView.setTextColor(Color.RED);
                    passConfirmTextView.setText("Password Confirmation can not be blank!");
                } else {
                    passConfirmTextView.setVisibility(View.GONE);
                }

            }
        });


    }
}