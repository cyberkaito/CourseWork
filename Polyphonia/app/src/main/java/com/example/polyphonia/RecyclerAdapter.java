package com.example.polyphonia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Root.News> news;
    private Intent intent;

    public RecyclerAdapter(Context context, ArrayList<Root.News> newsArrayList){
        this.context = context;
        this.news = newsArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        Root.News release = news.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
        Date d = null;
        try {d = sdf.parse(release.date);} catch (ParseException e) {e.printStackTrace();}
        String formattedTime = output.format(d);
        holder.tv_date.setText(formattedTime);
        holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.card_layout));
        holder.tv_header.setText(release.header);
        if(release.rateCount != 0 && release.rate != 0){
            holder.tv_rating.setText(String.format("%.2f",release.rate/(double)release.rateCount));
        }else{
            holder.tv_rating.setText(String.valueOf(0.0));
        }
        Call<ArrayList<Root.Categories>> getCategories = holder.apiInterface.getCategoriesList();
        getCategories.enqueue(new Callback<ArrayList<Root.Categories>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.Categories>> call, Response<ArrayList<Root.Categories>> response) {
                if(response.isSuccessful()){
                    ArrayList<Root.Categories> categories = response.body();
                    Root.Categories category = categories.stream().filter(n -> n.idCategory == release.idCategory).findFirst().orElse(null);
                    holder.tv_category.setText(category.name);
                }else {Toast.makeText(context,"Ошибка определения категории", Toast.LENGTH_SHORT).show();}
            }
            @Override
            public void onFailure(Call<ArrayList<Root.Categories>> call, Throwable t) {Toast.makeText(context,"Ошибка определения категории", Toast.LENGTH_SHORT).show();}
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context,NewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idNews",release.idNews);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date, tv_category, tv_header, tv_rating;
        RecyclerView gameRecycler;
        AppInterface apiInterface;
        ViewHolder(View view){
            super(view);
            tv_date = view.findViewById(R.id.tv_date);
            tv_category = view.findViewById(R.id.tv_category);
            tv_header = view.findViewById(R.id.tv_header);
            tv_rating = view.findViewById(R.id.rating);
            gameRecycler = view.findViewById(R.id.recycler_view);
            apiInterface = Configurator.buildRequest().create(AppInterface.class);
        }
    }
}

