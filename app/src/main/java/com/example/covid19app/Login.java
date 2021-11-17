package com.example.covid19app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Login extends AppCompatActivity {

    private EditText lPassword, lEmail;
    private CircularProgressButton loginBtn;
    private TextView registerPage;
    private String userID, username, email;
    private FirebaseAuth fAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 100;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        lEmail = findViewById(R.id.login_email);
        lPassword = findViewById(R.id.login_password);
        loginBtn = (CircularProgressButton) findViewById(R.id.loginButton);
        registerPage = findViewById(R.id.register_page);
        fAuth = FirebaseAuth.getInstance();
        getEmailFromSP(lEmail); //retrieve email from shared preferences from registration
        //normal email and password sign in with firebase authentication
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.startAnimation();
                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                //check if email and password is entered
                if (TextUtils.isEmpty(email)) {
                    lEmail.setError("Please enter login email.");
                    loginBtn.revertAnimation();
                    return; }
                if (TextUtils.isEmpty(password)) {
                    lPassword.setError("Please enter login password.");
                    loginBtn.revertAnimation();
                    return; }

                //firebase authentication sign in (not google sign in)
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Homepage.class));
                        } else {
                            loginBtn.revertAnimation();
                            Toast.makeText(Login.this, "Login is unsuccessful! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //google sign in button activity
        findViewById(R.id.google_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Google sign in intent with RC_SIGN_IN response code
                createRequest();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //go to registration page
        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    //Request for google sign in
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //error to be ignored as it will be solved during building of app
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Google sign in activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Authenticate google sign in with firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    //login successful with google
                    public void onSuccess(@NonNull AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = fAuth.getCurrentUser();
                        userID = user.getUid();
                        username = user.getDisplayName();
                        email = user.getEmail();
                        //check if user is new or existing (google sign in)
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");
                            //create a node in realtime database with userid as identity of node with user data
                            ProfileHelperClass helperClass = new ProfileHelperClass(username, null, email, null);
                            reference.child(userID).setValue(helperClass);
                            Toast.makeText(Login.this,"New Account Creation Success!", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(Login.this,"Logging in Existing User Account...", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),Homepage.class));
                        finish();
                    }
                })
                // Failed to login with google
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,"Login Authentication Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //function for sending reset password email to entered email
    public void resetPassword(View view) {
        final EditText resetMail = new EditText(view.getContext());
        //dialog message with input text field to ask for email to send reset email password
        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter Email to Receive Reset Password Email:")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(resetMail)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    // get email to send password reset link
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(Login.this, "Reset Password Email Sent Successfully To Your Email", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Reset Password Email Failed to Sent. " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    //get email from shared preference
    private void getEmailFromSP(EditText lEmail) {
        sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        lEmail.setText(sp.getString("email", ""));
    }
}