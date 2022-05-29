package com.example.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private JSONArray bookList;
    private Context context;

    public MyAdapter(JSONArray bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recycleview_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject jsonObject = bookList.getJSONObject(position);
            holder.bookName.setText((String)jsonObject.get("name"));
            holder.author.setText((String)jsonObject.get("author"));
            holder.publisher.setText((String)jsonObject.get("publisher"));
            holder.pageNum.setText((String)jsonObject.get("page_num"));
            holder.date.setText((String)jsonObject.get("date"));
            holder.price.setText((String)jsonObject.get("price"));
            holder.category.setText((String)jsonObject.get("category"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName;
        private TextView author;
        private TextView publisher;
        private TextView pageNum;
        private TextView date;
        private TextView price;
        private TextView category;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.author_name);
            publisher = itemView.findViewById(R.id.publisher_name);
            pageNum = itemView.findViewById(R.id.page_number);
            date = itemView.findViewById(R.id.published_time);
            price = itemView.findViewById(R.id.price_num);
            category = itemView.findViewById(R.id.category_name);
        }
    }
}
