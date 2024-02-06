package com.example.planify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.planify.databinding.ActivityDataInsertBinding;
import com.example.planify.databinding.ActivityExamDataInsertBinding;

public class ExamDataInsertActivity extends AppCompatActivity {
    ActivityExamDataInsertBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExamDataInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("type");
        if (type.equals("update")) {
            setTitle("Update");
            binding.ExamName.setText(getIntent().getStringExtra("ExamName"));
            binding.Date.setText(getIntent().getStringExtra("Date"));
            binding.Time.setText(getIntent().getStringExtra("Time"));
            binding.Location.setText(getIntent().getStringExtra("Location"));
            binding.ExtraDetails.setText(getIntent().getStringExtra("ExtraDetails"));

            int id = getIntent().getIntExtra("id", 0);
            binding.addExam.setText("Update Exam");
            binding.addExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("ExamName", binding.ExamName.getText().toString());
                    intent.putExtra("Date", binding.Date.getText().toString());
                    intent.putExtra("Time", binding.Time.getText().toString());
                    intent.putExtra("Location", binding.Location.getText().toString());
                    intent.putExtra("ExtraDetails", binding.ExtraDetails.getText().toString());
                    intent.putExtra("id", id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } else {
            setTitle("Add Mode");
            //add --> referencing button widget id
            binding.addExam.setText("Add Exam");
            binding.addExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("ExamName", binding.ExamName.getText().toString());
                    intent.putExtra("Date", binding.Date.getText().toString());
                    intent.putExtra("Time", binding.Time.getText().toString());
                    intent.putExtra("Location", binding.Location.getText().toString());
                    intent.putExtra("ExtraDetails", binding.ExtraDetails.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ExamDataInsertActivity.this, MainActivity.class));
    }
}