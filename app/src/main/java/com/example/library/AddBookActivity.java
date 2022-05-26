package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddBookActivity extends AppCompatActivity {
    private EditText isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
    }

    public void btn3Click(View view){
        isbn = findViewById(R.id.isbn);
        LinearLayout li = findViewById(R.id.linear1);
        li.setVisibility(View.VISIBLE);
        String text = isbn.getText().toString();
    }

}