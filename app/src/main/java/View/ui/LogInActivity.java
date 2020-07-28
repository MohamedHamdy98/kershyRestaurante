package View.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testeverythingtwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.editText_login_gmail)
    EditText editTextLoginGmail;
    @BindView(R.id.editText_login_password)
    EditText editTextLoginPassword;
    @BindView(R.id.prgressBar_login)
    ProgressBar prgressBarLogin;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.button_goto_main_activity)
    Button buttonGotoMainActivity;
    @BindView(R.id.textView_goto_signUpActivity)
    TextView textViewGotoSignUpActivity;
    FirebaseAuth firebaseAuth;
    View view;
    @BindView(R.id.textView_resetPassword)
    TextView textViewResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeEdit);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        SharedPreferences preferences = getSharedPreferences("keep", MODE_PRIVATE);
        String check = preferences.getString("remember", "");
        if (check.equals("true")) {
            startActivity(new Intent(LogInActivity.this, CategoriesActivity.class));
        } else if (check.equals("false")) {
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                } else if (!buttonView.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });
        textViewGotoSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        // To go to reset password activity..
        textViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,ResetPasswordActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        buttonGotoMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = editTextLoginGmail.getText().toString();
                final String password = editTextLoginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, R.string.Please_Enter_Your_Email, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, R.string.Please_Enter_Your_Password, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Snackbar.make(v, R.string.Password_should_be_more_than_6_letters, Snackbar.LENGTH_SHORT).show();
                }
                editTextLoginGmail.getText().clear();
                editTextLoginPassword.getText().clear();
                prgressBarLogin.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                prgressBarLogin.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LogInActivity.this, CategoriesActivity.class));
                                    Snackbar.make(v, R.string.LogIn_success, Snackbar.LENGTH_SHORT).show();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                } else {
                                    Snackbar.make(v, R.string.LogIn_failed, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}