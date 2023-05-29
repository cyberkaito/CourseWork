package com.example.polyphonia;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.polyphonia.databinding.ActivityLoginBinding;
import java.util.ArrayList;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    public TextView login, password;
    public Button loginBtn, signupBtn;
    private AppInterface appInterface;
    private PaperDb paper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        appInterface = Configurator.buildRequest().create(AppInterface.class);
        this.setTitle("Вход в приложение");
        Paper.init(this);
        paper = new PaperDb();


        login = binding.editTextTextEmailAddress;
        password = binding.editTextTextPassword;
        loginBtn = binding.loginBtn;
        signupBtn = binding.sigupBtn;

        if(paper.getClient() != null){
            Intent intent = new Intent(view.getContext(),MainActivity.class);
            startActivity(intent);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ArrayList<Root.Clients>> getClientList = appInterface.getClientList();
                getClientList.enqueue(new Callback<ArrayList<Root.Clients>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ArrayList<Root.Clients>> call, Response<ArrayList<Root.Clients>> response) {
                        if(response.isSuccessful()) {
                            ArrayList<Root.Clients> listClients = response.body();
                            Root.Clients client = listClients.stream()
                                    .filter(item -> item.email.contentEquals(login.getText().toString().trim()))
                                    .findFirst()
                                    .orElse(null);
                            if(client != null) {
                                appInterface.Auth(login.getText().toString(), password.getText().toString()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.code() == 200){
                                            paper.setClient(client);
                                            Intent intent = new Intent(view.getContext(),MainActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Неверный пароль",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Ошибка со стороны сервера",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"Пользователь с данной почтой не найден",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Ошибка со стороны сервера",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Root.Clients>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).{8,}";
                String patternLogin = "^(.+)@(\\S+)$";;
                if (login.getText().toString().matches(patternLogin)) {
                    if (password.getText().toString().matches(pattern)) {
                        Root.Clients client = new Root.Clients(login.getText().toString().split("@")[0],
                                login.getText().toString().trim(),
                                password.getText().toString().trim(), "-", "-");
                        Call<Root.Clients> postClient = appInterface.postClient(client);
                        postClient.enqueue(new Callback<Root.Clients>() {
                            @Override
                            public void onResponse(Call<Root.Clients> call, Response<Root.Clients> response) {
                                if (response.isSuccessful()) {
                                    paper.setClient(client);
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Успешная регистрация", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Пользователь с данной почтой уже зарегистрирован", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Root.Clients> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Ошибка со стороны сервера", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Используйте пароль больше 8 символов латинские символы, спец. символы и цифры", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Введите корректную почту", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}