package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class AddBookActivity extends AppCompatActivity {
    private EditText isbn;
    public static String apiKey = "12678.7bfc86600ba9e20a98c19644d45be169.41d1f80d97acc08aaaf1a48c6a3878c1";

    public static String isbnNum;
    public static String bookName;
    public static String author;
    public static String publisher;
    public static String pageNum;
    public static String date;
    public static String price;
    public static String category;
    public static String cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Button queryBtn = findViewById(R.id.btn3);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbn = findViewById(R.id.isbn);
                LinearLayout li = findViewById(R.id.linear1);
                li.setVisibility(View.VISIBLE);
                Button button = findViewById(R.id.btn4);
                button.setEnabled(true);
                new Thread(() -> {
                    System.out.println("New Thread");
                    getBookInfo(isbn);
                }).start();
            }
        });
    }

//    public void btn3Click(View view) {
//        isbn = findViewById(R.id.isbn);
//        LinearLayout li = findViewById(R.id.linear1);
//        li.setVisibility(View.VISIBLE);
//        new Thread(() -> {
//            System.out.println("New Thread");
//
//            getBookInfo(isbn);
//        });
//    }

    public void getBookInfo(EditText isbn){
        HttpURLConnection connection;
        String urlPath = "https://api.jike.xyz/situ/book/isbn/" + isbn.getText() + "?apikey=" + apiKey;
        System.out.println(urlPath);
        try{
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println("Code: " + code);
            if(code == 200){
                InputStream inputStream = connection.getInputStream();
                String json = readString(inputStream);
                JSONObject jsonObject = new JSONObject(json);
                JSONObject bookInfo = jsonObject.getJSONObject("data");
                isbnNum = isbn.getText().toString();
                bookName = bookInfo.getString("name");
                author = bookInfo.getString("author");
                publisher = bookInfo.getString("publishing");
                pageNum = bookInfo.getString("pages");
                date = bookInfo.getString("published");
                price = bookInfo.getString("price");
                category = bookInfo.getString("designed");
                cover = bookInfo.getString("photoUrl");
                handler.sendEmptyMessage(0x00);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == 0x00){
                Toast.makeText(AddBookActivity.this, "图书信息查询成功", Toast.LENGTH_LONG).show();
                System.out.println("请求成功");
                LinearLayout li = findViewById(R.id.linear1);
                li.setVisibility(View.VISIBLE);

                TextView book_name = findViewById(R.id.book_name);
                TextView author_name = findViewById(R.id.author);
                TextView publisher_name = findViewById(R.id.publisher);
                TextView page_num = findViewById(R.id.page_num);
                TextView publish_date = findViewById(R.id.date);
                TextView price_num = findViewById(R.id.price);
                TextView type = findViewById(R.id.category);
                book_name.setText(bookName);
                author_name.setText(author);
                publisher_name.setText(publisher);
                page_num.setText(pageNum);
                publish_date.setText(date);
                price_num.setText(price);
                type.setText(category);
            }
            return true;
        }
    });

    public static byte[] readInput(InputStream inputStream){
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int l;
            while ((l = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, l);
            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readString(InputStream inputStream){
        return new String(readInput(inputStream));
    }


    public void btn4Click(View view) throws JSONException {
        boolean isAdded = false;
        if(BookListActivity.bookList != null){
            for(int i = 0; i < BookListActivity.bookList.length(); i++){
                try {
                    if(((String) BookListActivity.bookList.getJSONObject(i).get("isbn")).equals(isbn.getText().toString())){
                        isAdded = true;
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(isAdded){
            Toast.makeText(AddBookActivity.this, "该书目已存在", Toast.LENGTH_LONG).show();
        }
        else{
            JSONObject book = new JSONObject();
            book.put("isbn", isbn.getText().toString());
            book.put("name", bookName);
            book.put("author", author);
            book.put("publisher", publisher);
            book.put("page_num", pageNum);
            book.put("date", date);
            book.put("price", price);
            book.put("category", category);
            BookListActivity.addBook(book);
            Toast.makeText(AddBookActivity.this, "图书信息保存成功", Toast.LENGTH_LONG).show();
            System.out.println(BookListActivity.bookList);
        }
        Button button = findViewById(R.id.btn4);
        button.setEnabled(false);
    }
}