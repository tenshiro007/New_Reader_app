package com.example.newreaderapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<NewsItem>news;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.new_item,parent,false);
        return new ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull  NewsAdapter.ViewHolder holder, int position) {

        holder.txtTitle.setText(news.get(position).getTitle());
        holder.txtContext.setText(news.get(position).getDescription());
        holder.txtDate.setText(news.get(position).getDate());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 7/15/2021 navigete to web activity
                Intent intent=new Intent(context, WebsiteActivity.class);
                intent.putExtra("url",news.get(position).getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        try{
            return news.size();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle,txtContext,txtDate;
        private CardView parent;
        public ViewHolder( View itemView) {
            super(itemView);
            txtContext=itemView.findViewById(R.id.txtContent);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtDate=itemView.findViewById(R.id.txtDate);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
