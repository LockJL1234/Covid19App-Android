package com.example.covid19app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {

    private TextView question, questionNum, scoreView;
    private Button optionBtn1, optionBtn2, optionBtn3, optionBtn4, returnBtn;
    private ArrayList<QuizModel> quizModels;
    private int currentScore = 0, qAttemptNum = 1, currentQ = 0;
    private ProgressBar progressBar;
    private String username, ldboardScore;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        question = findViewById(R.id.question);
        questionNum = findViewById(R.id.progressNum);
        optionBtn1 = findViewById(R.id.option1_btn);
        optionBtn2 = findViewById(R.id.option2_btn);
        optionBtn3 = findViewById(R.id.option3_btn);
        optionBtn4 = findViewById(R.id.option4_btn);
        progressBar = findViewById(R.id.progressBar);

        //create arraylist to store quiz data
        quizModels = new ArrayList<>();
        //get quiz data with getter method in quiz java class
        getQuizQuestion(quizModels);

        //set current view as current question based on number of questions done
        setDataToView(currentQ);

        //get username data from QuizStart Activity
        username = getIntent().getStringExtra("Username");

        //user answer question by clicking on one of the option button
        //right answer will add user's score and proceed to next question
        optionBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnswer(optionBtn1);
            }
        });

        optionBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnswer(optionBtn2);
            }
        });

        optionBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnswer(optionBtn3);
            }
        });

        optionBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnswer(optionBtn4);
            }
        });
    }

    //initiate and display bottom sheet to show quiz score of user and return user to quiz starting page
    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Quiz.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_bottom_sheet, (LinearLayout) findViewById(R.id.score_layout));
        scoreView = bottomSheetView.findViewById(R.id.quizScore);
        returnBtn = bottomSheetView.findViewById(R.id.restartBtn);
        scoreView.setText(currentScore + "/8");
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        //quiz score will be save in realtime database after user click return to quiz starting page
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore();
                startActivity(new Intent(Quiz.this, QuizStart.class));
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        if (!bottomSheetDialog.isShowing())
            bottomSheetDialog.show();
    }

    //function to perform view changing to next question and showing bottom sheet when user finish answering the quiz
    private void setDataToView(int currentQ) {
        questionNum.setText(qAttemptNum + "/8");
        if (currentQ > 7)
            showBottomSheet();
        else {
            progressBar.setProgress(qAttemptNum);
            question.setText(quizModels.get(currentQ).getQuestion());
            optionBtn1.setText(quizModels.get(currentQ).getOption1());
            optionBtn2.setText(quizModels.get(currentQ).getOption2());
            optionBtn3.setText(quizModels.get(currentQ).getOption3());
            optionBtn4.setText(quizModels.get(currentQ).getOption4());
        }
    }

    //function to add quiz data to quizModel arraylist with answer of quiz
    private void getQuizQuestion(ArrayList<QuizModel> quizModels) {
        quizModels.add(new QuizModel("Where was the first known Covid-19 infection found?", "Guangdong", "Wuhan",
                "Hunan", "Sichuan", "Wuhan"));
        quizModels.add(new QuizModel("Where was the Covid-19 \"Delta\" Variant first identified?", "UK", "India",
                "South Africa", "Brazil", "India"));
        quizModels.add(new QuizModel("Where was the Covid-19 \"Gamma\" Variant first identified?", "UK", "India",
                "South Africa", "Brazil","Brazil"));
        quizModels.add(new QuizModel("Which of the following is NOT a symptoms of Covid-19?", "Cough", "Headache",
                "Paralysis", "Sore Throat", "Paralysis"));
        quizModels.add(new QuizModel("Which of the following is a prevention method of Covid-19?", "Social distancing of 1 metre",
                "Do not get vaccinated", "Sanitize or wash hands seldom", "Do not wear a mask in public", "Social distancing of 1 metre"));
        quizModels.add(new QuizModel("What is the efficacy rate of \"Moderna\" vaccine?", "100%", "60%", "51%",
                "95%", "95%"));
        quizModels.add(new QuizModel("What is the challenge in the delivery of mRNA-based vaccine?", "Requires extremely cold storage conditions",
                "Complex manufacturing process", "Need to ensure the viral vector is safe for usage", "High manufacturing cost",
                "Requires extremely cold storage conditions"));
        quizModels.add(new QuizModel("Which of the following is not a side effect of Covid-19 vaccine?", "Headache", "Arm Pain",
                "Tiredness", "Blurry Vision", "Blurry Vision"));
    }

    //function to add quiz score to realtime database with username
    //score will be updated if user's quiz score is higher than previous quiz score
    private void updateScore() {
        reference = FirebaseDatabase.getInstance().getReference("leaderboard").child(username);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //user quiz score will be recorded if user quiz score does not exists in realtime database
                if (!snapshot.exists()) {
                    reference.child("username").setValue(String.valueOf(username));
                    reference.child("score").setValue(String.valueOf(currentScore));
                }
                //if user quiz score exists in realtime database, compare previous score with current score
                else {
                    //retrieve previous user quiz score from realtime database
                    ldboardScore = String.valueOf(snapshot.child("score").getValue());
                    //compare previous score with current score
                    if (ldboardScore != null && currentScore > Integer.parseInt(ldboardScore)) {
                        reference.child("username").setValue(String.valueOf(username));
                        reference.child("score").setValue(String.valueOf(currentScore));
                    }
                }
            }

            //error message if data retrieval from realtime database fails
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ScoreBoardActivity", "Failed to read fire.data");
            }
        });
    }

    //function for receiving user's answer and inform user if answer is right or wrong
    //if user chose wrong answer, the right answer will be revealed to user using Toast pop up message
    private void revealAnswer(Button optionBtn) {
        if (quizModels.get(currentQ).getAnswer().trim().toLowerCase().equals(optionBtn.getText().toString().trim().toLowerCase())) {
            Toast.makeText(getApplicationContext(), "Right Answer!!!", Toast.LENGTH_SHORT).show();
            currentScore++;
        } else
            Toast.makeText(getApplicationContext(), "Wrong Answer!\n Answer is " + quizModels.get(currentQ).getAnswer(), Toast.LENGTH_SHORT).show();
        if (qAttemptNum < 9) {
            qAttemptNum++;
            //minus 1 for number of attempt question as number of attempt question will be 9 as user answer question number 8
            if (qAttemptNum == 9)
                qAttemptNum --;
            currentQ++;
        }
        setDataToView(currentQ);
    }
}
