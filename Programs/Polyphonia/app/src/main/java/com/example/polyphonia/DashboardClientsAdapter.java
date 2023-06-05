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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardClientsAdapter extends RecyclerView.Adapter<DashboardClientsAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Root.Clients> clients;

    public DashboardClientsAdapter(Context context, ArrayList<Root.Clients> clientsArrayList){
        this.context = context;
        this.clients = clientsArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        Root.Clients client = clients.get(position);
        holder.name.setText(client.name);
        holder.rating.setVisibility(View.INVISIBLE);
        holder.imageView2.setVisibility(View.INVISIBLE);
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        Picasso picasso = picassoBuilder.build();
        picasso.load(client.avatar).error(context.getDrawable(R.drawable.blank_profile)).into(holder.channelImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClientActivity.class);
                intent.putExtra("idClient",client.idClient);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,rating;
        CircleImageView channelImage;
        ImageView imageView2;
        AppInterface apiInterface;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            rating = view.findViewById(R.id.rating);
            channelImage = view.findViewById(R.id.channelImage);
            apiInterface = Configurator.buildRequest().create(AppInterface.class);
            imageView2 = view.findViewById(R.id.imageView2);
        }
    }
}
