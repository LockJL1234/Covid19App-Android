package com.example.covid19app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView email, username;
    private DatabaseReference reference;
    private String userID, myUri = "";
    private BottomNavigationView btmNavBar;
    private Toolbar toolbar;
    private View headerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CV19StatsFragment CV19StatsFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CircleImageView profileImage;
    private Uri imageUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        //initiate bottom navigation bar
        btmNavBar = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
        btmNavBar.setOnNavigationItemSelectedListener(navListener);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        //initiate toolbar on top of application
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Covid 19 Statistics");

        //initiate drawer navigation bar
        drawerLayout = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.home_nav_view);
        //get headerview of drawer
        headerView = navigationView.getHeaderView(0);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //open drawer when user enter homepage
        drawerLayout.openDrawer(GravityCompat.START);

        //profile image of user that uploads picture from gallery and store in firebase
        profileImage = headerView.findViewById(R.id.profile_image);

        //get userid from login activity from passing string userid
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users");

        //get user detail
        getUserDetails(userID);

        //initiate fragment view and set fragment view as homepageFragment
        CV19StatsFragment = new CV19StatsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, CV19StatsFragment);
        fragmentTransaction.commit();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    //get user data after activity is restarted
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //change fragment view based on item selected on bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.cv19StatsFragment:
                            selectedFragment = new CV19StatsFragment();
                            btmNavBar.getMenu().setGroupCheckable(0, true, true);
                            toolbar.setTitle("Covid 19 Statistics");
                            break;
                        case R.id.leaderboardFragment:
                            selectedFragment = new LeaderboardFragment();
                            btmNavBar.getMenu().setGroupCheckable(0, true, true);
                            toolbar.setTitle("Quiz Leaderboard");
                            break;
                        case R.id.profileFragment:
                            selectedFragment = new ProfileFragment();
                            btmNavBar.getMenu().setGroupCheckable(0, true, true);
                            toolbar.setTitle("User Profile");
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    //change activity based on item selected on drawer navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_quiz:
                startActivity(new Intent(Homepage.this, QuizStart.class));
                break;
            case R.id.nav_information:
                startActivity(new Intent(Homepage.this, Cv19Information.class));
                break;
            case R.id.nav_item_checklist:
                startActivity(new Intent(Homepage.this, Checklist.class));
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //user logout with confirmation from message dialog box
    public void logout() {
        //building an alert pop up message dialog for user to confirm log out action
        new AlertDialog.Builder(this)
                .setTitle("Logging Out")
                .setMessage("Confirm Log Out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Homepage.this, Login.class));
                        Toast.makeText(Homepage.this, "Logging out...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    //drawer animation
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //get username, email, and profile picture of currently logged in user
    private void getUserDetails(String userID){
        username = headerView.findViewById(R.id.drawer_head_username);
        email = headerView.findViewById(R.id.drawer_head_email);
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = String.valueOf(snapshot.child("username").getValue());
                String uEmail = String.valueOf(snapshot.child("email").getValue());
                username.setText("Welcome back!\n" + uname);
                email.setText(uEmail);
                if (snapshot.exists() && snapshot.getChildrenCount()>0)
                    if (snapshot.hasChild("image")){
                        //get url image from realtime database and use "Picasso" library to load image from url
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImage);
                    }
            }
            //error message
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Homepage.this, "Error: Unable to Load Data from Realtime Database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //create menu on the right side of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cv19_source_menu, menu);
        return true;
    }

    //Handle item click on the question mark icon located right side of toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cv19_source) {
            Toast.makeText(this, "Covid-19 Statistics Source:\n COVIDNOW in Malaysia", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //intent with set local storage file directory for user to choose user profile picture from local storage
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    //Overridden onActivityResult method to get image data from local storage chosen by user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadPicture();
        }
    }

    //method to upload chosen image from local storage to Firebase Storage
    private void uploadPicture() {
        //progress dialog to track progress and inform user
        final ProgressDialog pd = new ProgressDialog(this);
        HashMap<String, Object> userPicMap = new HashMap<>();
        pd.setTitle("Uploading Image...");
        pd.show();

        // Create a reference to '[user id].jpg'
        StorageReference imagesRef = storageReference.child(userID + ".jpg");
        uploadTask = imagesRef.putFile(imageUri);

        //task Uri to continue uploading profile picture to Firebase Storage and retrieve url of uploaded Profile Picture and store into Realtime Database
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return imagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //store url of profile picture into realtime database under child "userID" of user
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    myUri = downloadUri.toString();
                    HashMap<String, Object> userPicMap = new HashMap<>();
                    userPicMap.put("image", myUri);
                    reference.child(userID).updateChildren(userPicMap);
                    pd.dismiss();
                    Toast.makeText(Homepage.this, "Profile picture set and uploaded successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Homepage.this, "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}