package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookListActivity extends AppCompatActivity {

    public static JSONArray bookList = new JSONArray();
    public static JSONArray result = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        System.out.println(bookList);

        RecyclerView recyclerView = findViewById(R.id.recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(bookList, this);
        recyclerView.setAdapter(myAdapter);

        TextView numHint = findViewById(R.id.num_hint);
        String text = "共" + bookList.length() + "条记录";
        numHint.setText(text);

        Button queryBtn = findViewById(R.id.btn5);
        queryBtn.setOnClickListener(view -> {
            EditText editText = findViewById(R.id.search_text);
            String author_name = editText.getText().toString();
            new Thread(()->{
                try {
                    getAuthorList(author_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void getAuthorList(String author_name) throws JSONException {
        result = new JSONArray();
        for(int i = 0; i < bookList.length(); i++){
            JSONObject jsonObject = bookList.getJSONObject(i);
            if(((String) jsonObject.get("author")).contains(author_name)){
                result.put(jsonObject);
            }
        }
        handler.sendEmptyMessage(0x00);
    }

    Handler handler = new Handler(message -> {
        if(message.what == 0x00){
            RecyclerView recyclerView = findViewById(R.id.recycle);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookListActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            MyAdapter myAdapter = new MyAdapter(result, BookListActivity.this);
            recyclerView.setAdapter(myAdapter);
        }
        return true;
    });

    public static void addBook(JSONObject book){
       bookList.put(book);
    }


}