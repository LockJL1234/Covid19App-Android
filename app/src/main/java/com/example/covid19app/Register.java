package com.example.covid19app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;


public class Register extends AppCompatActivity {
    private EditText rUsername, rPassword, rEmail;
    private CircularProgressButton registerBtn;
    private TextView loginPage;
    private String userID;
    private FirebaseAuth fAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private SharedPreferences sp;

    // static boolean method to check format of registration email
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        rUsername = findViewById(R.id.register_username);
        rPassword = findViewById(R.id.register_password);
        rEmail = findViewById(R.id.register_email);
        fAuth = FirebaseAuth.getInstance();
        registerBtn = (CircularProgressButton) findViewById(R.id.registerButton);
        loginPage = findViewById(R.id.LoginPage);

        //start registration after clicking button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBtn.startAnimation();
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String username = rUsername.getText().toString();

                //check validity of registration email and password
                if (TextUtils.isEmpty(username)) {
                    rUsername.setError("Please enter a username.");
                    registerBtn.revertAnimation();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    rEmail.setError("Please enter an email.");
                    registerBtn.revertAnimation();
                    return;
                }
                if (!isValidEmail(email)) {
                    rEmail.setError("Please enter a valid email.");
                    registerBtn.revertAnimation();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    rPassword.setError("Please enter a password.");
                    registerBtn.revertAnimation();
                    return;
                }
                if (password.length() < 6) {
                    rPassword.setError("Password Must be >= 6 characters");
                    registerBtn.revertAnimation();
                    return;
                }

                //register user in firebase authentication cloud database
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Account created successfully, username and email stored in realtime database
                        //Display error message if failed to create user account
                        if (task.isSuccessful()) {
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");
                            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            ProfileHelperClass helperClass = new ProfileHelperClass(username, null, email, null);
                            reference.child(userID).setValue(helperClass);
                            storeEmail(email);
                            registerBtn.revertAnimation();
                            Toast.makeText(Register.this, "User Created! ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Go to login page
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    //store email registration to shared preference
    private void storeEmail(String email){
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.commit();
        Toast.makeText(this, "Email saved", Toast.LENGTH_SHORT).show();
    }
}
