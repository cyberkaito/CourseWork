package com.example.polyphonia.ui.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyphonia.AppInterface;
import com.example.polyphonia.Configurator;
import com.example.polyphonia.PaperDb;
import com.example.polyphonia.R;
import com.example.polyphonia.RecyclerAdapter;
import com.example.polyphonia.Root;
import com.example.polyphonia.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AutoCompleteTextView spinner;
    private RecyclerView newsRecycler;
    private AppInterface appInterface;
    private ArrayList<Root.Categories> categories;
    private ArrayList<Root.News> listNews;
    private ArrayAdapter<String> adapterCategories;
    private RecyclerAdapter adapter;
    private PaperDb paper = new PaperDb();
    private int ClientID;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = binding.spinner;
        newsRecycler = binding.recyclerView;

        appInterface = Configurator.buildRequest().create(AppInterface.class);
        newsRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        newsRecycler.setHasFixedSize(true);
        Call<ArrayList<Root.Categories>> getCategoriesList = appInterface.getCategoriesList();
        Call<ArrayList<Root.News>> getNewsList = appInterface.getNewsList();
        Call<ArrayList<Root.ClientTypes>> getClientTypes = appInterface.getClientTypesList();
        ClientID = paper.getClient().idClient;
        getCategoriesList.enqueue(new Callback<ArrayList<Root.Categories>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onResponse(Call<ArrayList<Root.Categories>> call, Response<ArrayList<Root.Categories>> response) {
                if(response.isSuccessful()){
                    categories = response.body();
                    ArrayList<String> namesCategories = new ArrayList<>();
                    categories.forEach((e) -> {
                        namesCategories.add(e.getName());
                    });
                    adapterCategories = new ArrayAdapter<String>(getContext(),
                            R.layout.spinner_layout, namesCategories );
                    spinner.setAdapter(adapterCategories);
                }else{
                    Toast.makeText(root.getContext(),"Ошибка со стороны сервера",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Root.Categories>> call, Throwable t) {
                Toast.makeText(root.getContext(),"Ошибка со стороны сервера",Toast.LENGTH_LONG).show();
            }
        });
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapterCategories.getItem(position);
                if(name.equals("Сбросить")){
                    adapter = new RecyclerAdapter(root.getContext(),  listNews);
                    newsRecycler.setAdapter(adapter);
                }else if(name.equals("По рейтингу")){
                    ArrayList<Root.News> FilterNews = listNews;
                    Collections.sort(FilterNews,new Comparator<Root.News>() {
                        @Override
                        public int compare(Root.News o1, Root.News o2) {
                            if ((o1.getRate() / Double.valueOf(o1.getRateCount())) > (o2.getRate()/ Double.valueOf(o2.getRateCount()))) {
                                return -1;
                            } else if ((o1.getRate() / Double.valueOf(o1.getRateCount())) > (o2.getRate()/ Double.valueOf(o2.getRateCount()))) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    adapter = new RecyclerAdapter(root.getContext(),  FilterNews);
                    newsRecycler.setAdapter(adapter);
                }else if(name.equals("Подписки")){
                    getClientTypes.enqueue(new Callback<ArrayList<Root.ClientTypes>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Root.ClientTypes>> call, Response<ArrayList<Root.ClientTypes>> response) {
                            if(response.isSuccessful()){
                                ArrayList<Root.News> FilterNews = new ArrayList<Root.News>();
                                ArrayList<Root.ClientTypes> clientTypes = response.body();
                                ArrayList<Root.ClientTypes> channelsSub = (ArrayList<Root.ClientTypes>) clientTypes.stream()
                                        .filter(n -> n.idRole == 1 & n.idClient == ClientID).collect(Collectors.toList());
                                if(channelsSub.size() > 0){
                                    listNews.forEach((Root.News news) -> {
                                            channelsSub.forEach((Root.ClientTypes channel) ->{
                                                if(news.idChannel == channel.idChannel){
                                                    FilterNews.add(news);
                                                }
                                            });
                                    });
                                    adapter = new RecyclerAdapter(root.getContext(),  FilterNews);
                                    newsRecycler.setAdapter(adapter);
                                }else{
                                    Toast.makeText(root.getContext(),"У вас нет каналов в подписках",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(root.getContext(),"Ошибка со стороны клиента",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ArrayList<Root.ClientTypes>> call, Throwable t) {
                            Toast.makeText(root.getContext(),"Сервер не отвечает",Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Root.Categories category = categories.stream().filter(n -> n.name == name).findFirst().orElse(null);
                    ArrayList<Root.News> FilterNews = (ArrayList<Root.News>) listNews.stream().filter(n -> n.idCategory == category.idCategory).collect(Collectors.toList());
                    adapter = new RecyclerAdapter(root.getContext(),  FilterNews);
                    newsRecycler.setAdapter(adapter);
                }
            }
        });
        getNewsList.enqueue(new Callback<ArrayList<Root.News>>() {
            @Override
            public void onResponse(Call<ArrayList<Root.News>> call, Response<ArrayList<Root.News>> response) {
                if(response.isSuccessful()){
                    listNews = response.body();
                    adapter = new RecyclerAdapter(root.getContext(),  listNews);
                    newsRecycler.setAdapter(adapter);
                }else{
                    Toast.makeText(root.getContext(),"Ошибка со стороны сервера",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Root.News>> call, Throwable t) {
                Toast.makeText(root.getContext(),"Сервер не отвечает",Toast.LENGTH_LONG).show();
                Toast.makeText(root.getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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