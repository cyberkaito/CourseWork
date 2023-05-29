package com.example.polyphonia.ui.dashboard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyphonia.AppInterface;
import com.example.polyphonia.Configurator;
import com.example.polyphonia.DashboardAdapter;
import com.example.polyphonia.DashboardClientsAdapter;
import com.example.polyphonia.R;
import com.example.polyphonia.RecyclerAdapter;
import com.example.polyphonia.Root;
import com.example.polyphonia.databinding.FragmentDashboardBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private AppInterface appInterface;
    private SearchView searchView;
    private ArrayList<Root.Channels> listChannels;
    private SwitchMaterial switchBar;
    private RecyclerView dashboardRecycler;
    private DashboardAdapter adapter;
    private ArrayList<Root.Clients> listClients;
    private DashboardClientsAdapter adapterClients;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        appInterface = Configurator.buildRequest().create(AppInterface.class);
        dashboardRecycler = root.findViewById(R.id.dashboardRecycler);
        searchView = root.findViewById(R.id.searchView);
        switchBar = root.findViewById(R.id.switchBar);
        switchBar.setChecked(false);

        Call<ArrayList<Root.Channels>> getChannels = appInterface.getChannelList();
        Call<ArrayList<Root.Clients>> getClients = appInterface.getClientList();
        getChannelsFunc(getChannels, root.getContext());
        SearchByState(false, root.getContext());

        getChannels.enqueue(new Callback<ArrayList<Root.Channels>>() {
            @Override
            public void onResponse(Call<ArrayList<Root.Channels>> call, Response<ArrayList<Root.Channels>> response) {
                listChannels = response.body();
                dashboardRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
                dashboardRecycler.setHasFixedSize(true);
                adapter = new DashboardAdapter(getContext(),  listChannels);
                dashboardRecycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Root.Channels>> call, Throwable t) {
                Toast.makeText(getContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
            }
        });
        getClients.enqueue(new Callback<ArrayList<Root.Clients>>() {
            @Override
            public void onResponse(Call<ArrayList<Root.Clients>> call, Response<ArrayList<Root.Clients>> response) {
                if(response.isSuccessful()){
                    listClients = response.body();
                }else{
                    Toast.makeText(getContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Root.Clients>> call, Throwable t) {
                Toast.makeText(getContext(),"Ошибка сервера", Toast.LENGTH_SHORT).show();
            }
        });
        switchBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean state) {
                if(state == true){
                    dashboardRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    dashboardRecycler.setHasFixedSize(true);
                    adapterClients = new DashboardClientsAdapter(getContext(),  listClients);
                    dashboardRecycler.setAdapter(adapterClients);

                }else{
                    dashboardRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    dashboardRecycler.setHasFixedSize(true);
                    adapter = new DashboardAdapter(getContext(),  listChannels);
                    dashboardRecycler.setAdapter(adapter);
                }
                SearchByState(state, root.getContext());
            }
        });
        return root;
    }
    private void getChannelsFunc(Call<ArrayList<Root.Channels>> getChannels, Context context){

    }
    private void SearchByState(boolean state, Context context){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                    if(state == false) {
                        ArrayList<Root.Channels> listFilter = (ArrayList<Root.Channels>) listChannels.stream().filter(n -> n.name.toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList());
                        adapter = new DashboardAdapter(getContext(), listFilter);
                        dashboardRecycler.setAdapter(adapter);
                    }else{
                        ArrayList<Root.Clients> listFilter = (ArrayList<Root.Clients>) listClients.stream().filter(n -> n.name.toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList());
                        adapterClients = new DashboardClientsAdapter(getContext(), listFilter);
                        dashboardRecycler.setAdapter(adapterClients);
                    }
                return false;
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}