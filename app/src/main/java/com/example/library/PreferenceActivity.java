package com.example.library;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreferenceActivity extends AppCompatActivity {

    private boolean showPrice = false;
    private Integer fontSize = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        SwitchCompat switchCompat = findViewById(R.id.show_price);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showPrice = isChecked;
        });
        SharedPreferences shared = getSharedPreferences("style", Context.MODE_PRIVATE);
//        showPrice = shared.getBoolean("showPrice", false);
        fontSize = Integer.parseInt(shared.getString("fontSize", "15"));
        EditText editText = findViewById(R.id.font_size);
        editText.setHint(fontSize.toString());
        Button save = findViewById(R.id.save_change);
        save.setOnClickListener(view -> {
            EditText editText1 = findViewById(R.id.font_size);
            fontSize = editText1.getText().toString().equals("") ? Integer.parseInt(shared.getString("fontSize", "15"))
        : parseInt(editText1.getText().toString());
            SharedPreferences sharedPreferences = getSharedPreferences("style", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("showPrice", String.valueOf(showPrice));
            editor.putString("fontSize", String.valueOf(fontSize));
            editor.apply();
            Toast.makeText(this, "修改设置成功", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}