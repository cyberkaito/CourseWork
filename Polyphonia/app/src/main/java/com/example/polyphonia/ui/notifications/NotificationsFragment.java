package com.example.polyphonia.ui.notifications;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.polyphonia.AppInterface;
import com.example.polyphonia.ChannelActivity;
import com.example.polyphonia.Configurator;
import com.example.polyphonia.PaperDb;
import com.example.polyphonia.R;
import com.example.polyphonia.Root;
import com.example.polyphonia.databinding.FragmentNotificationsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Optional;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private CircleImageView profileImage;
    private MaterialCardView btnLogout, channelBtn;
    private AppInterface appInterface;
    private PaperDb paper;
    private TextInputEditText login,key;
    private TextView tv_name,tv_bio, hasChannel;
    private int ClientID;
    private Root.Channels currentChannel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Picasso.Builder picassoBuilder = new Picasso.Builder(getContext());
        Picasso picasso = picassoBuilder.build();
        appInterface = Configurator.buildRequest().create(AppInterface.class);

        profileImage = binding.profileImage;

        login = binding.login;
        key = binding.key;
        tv_bio = binding.tvBio;
        tv_name = binding.tvName;
        btnLogout = binding.btnLogout;
        channelBtn = binding.channelBtn;
        hasChannel = binding.HasChannel;

        paper = new PaperDb();
        Call<Root.Clients> getClient = appInterface.getClient(paper.getClient().idClient);
        Call<ArrayList<Root.ClientTypes>> clientTypes = appInterface.getClientTypesList();
        Call<ArrayList<Root.Channels>> channelsList = appInterface.getChannelList();
        tv_name.setText(paper.getClient().name);
        tv_bio.setText(paper.getClient().bio);
        login.setText(paper.getClient().email);
        key.setText(paper.getClient().key);
        hasChannel.setVisibility(View.VISIBLE);
        channelBtn.setVisibility(View.INVISIBLE);
        getClient.enqueue(new Callback<Root.Clients>() {
            @Override
            public void onResponse(Call<Root.Clients> call, Response<Root.Clients> response) {
                if(response.isSuccessful()){
                    picasso.load(response.body().avatar).error(getContext().getDrawable(R.drawable.blank_profile)).into(profileImage);
                    ClientID = response.body().idClient;
                }else{
                    Toast.makeText(getContext(),"Не удалось загрузить изображение профиля", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Root.Clients> call, Throwable t) {
                Toast.makeText(getContext(),"Не удалось загрузить изображение профиля", Toast.LENGTH_SHORT).show();
            }
        });
        clientTypes.enqueue(new Callback<ArrayList<Root.ClientTypes>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.ClientTypes>> call, Response<ArrayList<Root.ClientTypes>> response) {
                if(response.isSuccessful()){
                    ArrayList<Root.ClientTypes> clientTypes1 = response.body();
                    Root.ClientTypes channel = clientTypes1.stream().filter(n -> n.idClient == ClientID & n.idRole == 2).findFirst().orElse(null);
                    if(channel != null) {
                        channelsList.enqueue(new Callback<ArrayList<Root.Channels>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Root.Channels>> call, Response<ArrayList<Root.Channels>> response) {
                                ArrayList<Root.Channels> channelsList = response.body();
                                currentChannel = channelsList.stream().filter(n -> n.idChannel == channel.idChannel).findFirst().orElse(null);
                                if (currentChannel != null) {
                                    channelBtn.setVisibility(View.VISIBLE);
                                    hasChannel.setVisibility(View.INVISIBLE);
                                }else{
                                    hasChannel.setVisibility(View.VISIBLE);
                                    channelBtn.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<Root.Channels>> call, Throwable t) {
                                Toast.makeText(getContext(), "Ошибка загрузки списка каналов", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Root.ClientTypes>> call, Throwable t) {
                Toast.makeText(getContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paper.removeUser();
                getActivity().finish();
            }
        });
        channelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChannelActivity.class);
                intent.putExtra("idChannel",currentChannel.idChannel);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}