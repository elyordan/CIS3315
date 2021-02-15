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

    public static boolean hasText(EditText editText,TextView textview) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            textview.setTextColor(Color.RED);
            textview.setText("The field can not be blank!");
            return false;
        } else {
            textview.setVisibility(View.GONE);
        }

        return true;
    }

    public boolean Validate() {

        boolean check = true;
        if (!hasText(inputUsername, userTextView)) check = false;
        if (!hasText(inputEmail, emailTextView)) check = false;
        if (!hasText(inputPassword, passTextView)) check = false;
        if (!hasText(inputPasswordConfirmation, passConfirmTextView)) check = false;
        return check;
    }


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

                String PASSWORD = inputPassword.getText().toString();
                String PASSWORDCONFIRM = inputPasswordConfirmation.getText().toString();

                if(Validate()) {
                    if (PASSWORD.equals(PASSWORDCONFIRM)) {
                        Toast.makeText(FormActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(FormActivity.this, "Sorry passwords do not match, try again", Toast.LENGTH_LONG ).show();
                    }
            }
        });




    }
}