package com.example.polyphonia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelActivity extends AppCompatActivity {

    private int idChannel;
    private RecyclerView newsRecycler;
    private AppInterface appInterface;
    private PaperDb paper;
    private TextView tv_name,tv_bio, cardText;
    private CircleImageView profileImage;
    private int ClientID;
    private RecyclerAdapter adapter;
    private CardView cardView;
    private ArrayList<Root.ClientTypes> clientTypes;
    private boolean isSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        newsRecycler = findViewById(R.id.recycler_view);
        idChannel = getIntent().getIntExtra("idChannel",0);
        profileImage = findViewById(R.id.profileImage);
        tv_bio = findViewById(R.id.tv_bio);
        tv_name = findViewById(R.id.tv_name);
        cardView = findViewById(R.id.cardView);
        cardText = findViewById(R.id.cardText);
        paper = new PaperDb();
        ClientID = paper.getClient().idClient;
        newsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        newsRecycler.setHasFixedSize(true);
        Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
        Picasso picasso = picassoBuilder.build();
        appInterface = Configurator.buildRequest().create(AppInterface.class);

        Call<Root.Channels> getChannel = appInterface.getChannel(idChannel);
        Call<ArrayList<Root.News>> getNews = appInterface.getNewsList();
        getChannel.enqueue(new Callback<Root.Channels>() {
            @Override
            public void onResponse(Call<Root.Channels> call, Response<Root.Channels> response) {
                if(response.isSuccessful()){
                    Root.Channels channel = response.body();
                    picasso.load(channel.avatar).error(getApplicationContext().getDrawable(R.drawable.blank_profile)).into(profileImage);
                    tv_name.setText(channel.name);
                    setTitle(channel.name);
                    tv_bio.setText(channel.description);
                }else{
                    Toast.makeText(getApplicationContext(),"Не удалось загрузить данные канала", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Root.Channels> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_LONG).show();
            }
        });
        getNews.enqueue(new Callback<ArrayList<Root.News>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.News>> call, Response<ArrayList<Root.News>> response) {
                if(response.isSuccessful()){
                    ArrayList<Root.News> listNews = response.body();
                    listNews = (ArrayList<Root.News>) listNews.stream().filter(n -> n.idChannel == idChannel).collect(Collectors.toList());
                    adapter = new RecyclerAdapter(getApplicationContext(),  listNews);
                    newsRecycler.setAdapter(adapter);
                }else{
                    Toast.makeText(getApplicationContext(),"Не удалось загрузить новости канала", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Root.News>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_LONG).show();
            }
        });
        updateSub();
        cardView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(isSub == true){
                    Root.ClientTypes subClient =  clientTypes.stream().filter(n -> n.idClient == ClientID & n.idChannel == idChannel & n.idRole == 1 ).findFirst().get();
                    if(subClient != null){
                        Call<Root.ClientTypes> deleteClientTypes = appInterface.deleteClientType(subClient.idClientType);
                        deleteClientTypes.enqueue(new Callback<Root.ClientTypes>() {
                            @Override
                            public void onResponse(Call<Root.ClientTypes> call, Response<Root.ClientTypes> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Вы отписались от канала", Toast.LENGTH_LONG).show();
                                    updateSub();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Не удалось отписаться от канала", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Root.ClientTypes> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Не удалось отписаться от канала", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"Не удалось отписаться от канала", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Root.ClientTypes newClientTypes = new Root.ClientTypes(ClientID, idChannel, 1);
                    Call<Root.ClientTypes> postClientType = appInterface.postClientType(newClientTypes);
                    postClientType.enqueue(new Callback<Root.ClientTypes>() {
                        @Override
                        public void onResponse(Call<Root.ClientTypes> call, Response<Root.ClientTypes> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Вы подписались на канал", Toast.LENGTH_LONG).show();
                                updateSub();
                            }else{
                                Toast.makeText(getApplicationContext(),"Не удалось подписаться на канал", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Root.ClientTypes> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Не удалось подписаться на канал", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private void updateSub(){
        Call<ArrayList<Root.ClientTypes>> getClientTypes = appInterface.getClientTypesList();
        getClientTypes.enqueue(new Callback<ArrayList<Root.ClientTypes>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.ClientTypes>> call, Response<ArrayList<Root.ClientTypes>> response) {
                if(response.isSuccessful()){
                    clientTypes = response.body();
                    int count = clientTypes.stream().filter(n -> n.idClient == ClientID & n.idChannel == idChannel & n.idRole == 1 ).collect(Collectors.toList()).size();
                    if(count > 0){
                        cardView.setBackgroundColor(Color.parseColor("#BC00F7FF"));
                        cardText.setText("Отписаться");
                        isSub = true;
                    }else {
                        cardText.setText("Подписаться");
                        isSub = false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Root.ClientTypes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_LONG).show();
            }
        });
    }
}
