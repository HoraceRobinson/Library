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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    public static JSONArray bookList = new JSONArray();
    public static JSONArray result = new JSONArray();
    public static List<String> list = new ArrayList<>();

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

        list = new ArrayList<>();
        list.add("按作者名排序");
        list.add("按出版日期排序");

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> argO, View argl, int arg2, long arg3) {
                argO.setVisibility(View.VISIBLE);
                String key = adapter.getItem(arg2);
                try {
                    sortList(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onNothingSelected(AdapterView<?> argO) {
                argO.setVisibility(View.VISIBLE);
            }
        });
    }

    private void sortList(String key) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < result.length(); i++) {
            jsonValues.add(result.getJSONObject(i));
        }
        jsonValues.sort(new Comparator<JSONObject>() {
            private String KEY_NAME = null;

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();
                if(key.equals("按作者名排序")){
                    KEY_NAME = "author";
                }
                else if(key.equals("按出版日期排序")){
                    KEY_NAME = "date";
                }
                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return valA.compareTo(valB);
            }
        });

        for (int i = 0; i < result.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        result = sortedJsonArray;
        System.out.println(result);
        handler.sendEmptyMessage(0x00);
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