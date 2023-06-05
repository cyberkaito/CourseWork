package com.example.polyphonia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    private int idClient;
    private CircleImageView profileImage;
    private AppInterface appInterface;
    private TextView tv_name, tv_bio, cardText, HasChannel;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        idClient = getIntent().getIntExtra("idClient",0);
        profileImage = findViewById(R.id.profileImage);
        tv_bio = findViewById(R.id.tv_bio);
        tv_name = findViewById(R.id.tv_name);
        cardView = findViewById(R.id.cardView);
        cardText = findViewById(R.id.cardText);
        HasChannel = findViewById(R.id.HasChannel);

        cardView.setVisibility(View.INVISIBLE);
        HasChannel.setVisibility(View.VISIBLE);
        appInterface = Configurator.buildRequest().create(AppInterface.class);
        Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
        Picasso picasso = picassoBuilder.build();

        Call<Root.Clients> getClient = appInterface.getClient(idClient);
        Call<ArrayList<Root.ClientTypes>> getClientTypes = appInterface.getClientTypesList();
        getClient.enqueue(new Callback<Root.Clients>() {
            @Override
            public void onResponse(Call<Root.Clients> call, Response<Root.Clients> response) {
                if(response.isSuccessful()){
                    Root.Clients client = response.body();
                    picasso.load(client.avatar).error(getApplicationContext().getDrawable(R.drawable.blank_profile)).into(profileImage);
                    tv_name.setText(client.name);
                    tv_bio.setText(client.bio);
                    setTitle(client.name);
                }else{
                    Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Root.Clients> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
            }
        });
        getClientTypes.enqueue(new Callback<ArrayList<Root.ClientTypes>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.ClientTypes>> call, Response<ArrayList<Root.ClientTypes>> response) {
                if(response.isSuccessful()){
                    ArrayList<Root.ClientTypes> clientTypes = response.body();
                    Root.ClientTypes clientChannel = clientTypes.stream().filter(n -> n.idRole == 2 & n.idClient == idClient).findFirst().orElse(null);
                    if(clientChannel != null){
                        Call<Root.Channels> getChannel = appInterface.getChannel(clientChannel.idChannel);
                        getChannel.enqueue(new Callback<Root.Channels>() {
                            @Override
                            public void onResponse(Call<Root.Channels> call, Response<Root.Channels> response) {
                                if(response.isSuccessful()){
                                    Root.Channels channel = response.body();
                                    cardText.setText(channel.name);
                                    cardView.setVisibility(View.VISIBLE);
                                    HasChannel.setVisibility(View.INVISIBLE);
                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                                            intent.putExtra("idChannel",channel.idChannel);
                                            getApplicationContext().startActivity(intent);
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(),"Не удалось найти канал", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Root.Channels> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Не удалось найти канал", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        cardView.setVisibility(View.INVISIBLE);
                        HasChannel.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Root.ClientTypes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
            }
        });
    }
}