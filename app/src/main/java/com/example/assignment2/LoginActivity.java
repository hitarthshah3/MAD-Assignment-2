package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn, btnGoogleSignIn;
    private TextView tvForgotPassword, tvSignUp;
    private ProgressBar pbLogin;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
        pbLogin = findViewById(R.id.pb_login);

        // Google Sign-In setup
        int webClientIdRes = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
        if (webClientIdRes != 0) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(webClientIdRes))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account.getIdToken());
                            }
                        } catch (ApiException e) {
                            Toast.makeText(LoginActivity.this, "Google Sign-In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnSignIn.setOnClickListener(v -> loginUser());

        btnGoogleSignIn.setOnClickListener(v -> {
            if (mGoogleSignInClient != null) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            } else {
                Toast.makeText(LoginActivity.this, "Google Sign-In requires Web Client ID configuration in Firebase Console.", Toast.LENGTH_LONG).show();
            }
        });

        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

        tvSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        btnSignIn.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    pbLogin.setVisibility(View.GONE);
                    btnSignIn.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                        Toast.makeText(LoginActivity.this, "Login Failed: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        pbLogin.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    pbLogin.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Firebase Google Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showForgotPasswordDialog() {
        final EditText resetEmailInput = new EditText(this);
        resetEmailInput.setHint("Enter your registered email");
        resetEmailInput.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        if (!etEmail.getText().toString().isEmpty()) {
            resetEmailInput.setText(etEmail.getText().toString());
        }

        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email address to receive a password reset link.")
                .setView(resetEmailInput)
                .setPositiveButton("Send", (dialog, which) -> {
                    String email = resetEmailInput.getText().toString().trim();
                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Reset link sent to your email!", Toast.LENGTH_LONG).show();
                                    } else {
                                        String err = task.getException() != null ? task.getException().getMessage() : "Error sending reset link";
                                        Toast.makeText(LoginActivity.this, "Error: " + err, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
