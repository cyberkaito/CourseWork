package com.example.polyphonia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Root.Comments> comments;

    public ListAdapter(Context context, ArrayList<Root.Comments> commentsArrayList){
        this.context = context;
        this.comments = commentsArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        Root.Comments comment = comments.get(position);
        holder.tv_comment.setText(comment.text);
        Call<Root.Clients> getPhoto = holder.apiInterface.getClient(comment.idClient);
        getPhoto.enqueue(new Callback<Root.Clients>() {
            @Override
            public void onResponse(Call<Root.Clients> call, Response<Root.Clients> response) {
                Root.Clients client = response.body();
                holder.tv_author.setText(client.name);
                Picasso.Builder picassoBuilder = new Picasso.Builder(context);
                Picasso picasso = picassoBuilder.build();
                picasso.load(client.avatar).error(context.getDrawable(R.drawable.blank_profile)).into(holder.profileImage);
                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClientActivity.class);
                        intent.putExtra("idClient",client.idClient);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                holder.tv_author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClientActivity.class);
                        intent.putExtra("idClient",client.idClient);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<Root.Clients> call, Throwable t) {
                Toast.makeText(context,"Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_comment, tv_author;
        CircleImageView profileImage;
        ListView listRecycler;
        AppInterface apiInterface;
        ViewHolder(View view){
            super(view);
            profileImage = view.findViewById(R.id.profileImage);
            tv_comment = view.findViewById(R.id.tv_comment);
            tv_author = view.findViewById(R.id.tv_author);
            listRecycler = view.findViewById(R.id.list_view);
            apiInterface = Configurator.buildRequest().create(AppInterface.class);
        }
    }
}
