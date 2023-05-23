package com.example.polyphonia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private CircleImageView clientImage;
    private TextView author, tv_date, tv_category,header, content,rating;
    private RecyclerView listRecycler;
    private AppInterface appInterface;
    private RatingBar ratingBar;
    private ListAdapter adapter;
    private Integer idNews;
    private Root.News news;
    private PaperDb paper = new PaperDb();
    private int ClientID;
    private TextInputEditText commentText;
    private TextInputLayout textInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listRecycler = findViewById(R.id.list_view);
        Intent intent = getIntent();
        idNews = intent.getIntExtra("idNews",0);
        clientImage = findViewById(R.id.clientImage);
        author = findViewById(R.id.author);
        tv_category = findViewById(R.id.tv_category);
        tv_date = findViewById(R.id.tv_date);
        header = findViewById(R.id.header);
        content = findViewById(R.id.content);
        ratingBar = findViewById(R.id.ratingBar);
        rating = findViewById(R.id.rating);
        textInputLayout = findViewById(R.id.textInputLayout);
        commentText = findViewById(R.id.textInput);
        ClientID = paper.getClient().idClient;

        this.setTitle("Статья №"+String.valueOf(idNews));
        appInterface = Configurator.buildRequest().create(AppInterface.class);
        getComments();
        getNews();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                news.setRate(news.rate+Double.valueOf(rating));
                news.setRateCount(news.rateCount+1);
                Call<Root.News> newsupdate = appInterface.updateNews(idNews,news);
                newsupdate.enqueue(new Callback<Root.News>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(Call<Root.News> call, Response<Root.News> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Оценка "+rating+" поставлена", 5000).show();
                            getNews();
                        }
                    }
                    @Override
                    public void onFailure(Call<Root.News> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Ошибка со стороны сервера", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(commentText.getText().toString().length() != 0){
                    Root.Comments comment = new Root.Comments(ClientID, idNews,commentText.getText().toString());
                    Call<Root.Comments> commentsCall = appInterface.postComment(comment);
                    commentsCall.enqueue(new Callback<Root.Comments>() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onResponse(Call<Root.Comments> call, Response<Root.Comments> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Комментарий оставлен", 5000).show();
                                getComments();
                            }else{
                                Toast.makeText(getApplicationContext(),"Ошибка со стороны сервера", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Root.Comments> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Ошибка со стороны сервера", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Введите комментарий", 5000).show();
                }
            }
        });
    }
    public void getNews(){
        Call<Root.News> getNews = appInterface.getNews(idNews);
        getNews.enqueue(new Callback<Root.News>() {
            @Override
            public void onResponse(Call<Root.News> call, Response<Root.News> response) {
                news = response.body();
                Call<Root.Channels> getChannel = appInterface.getChannel(news.idChannel);
                getChannel.enqueue(new Callback<Root.Channels>() {
                    @Override
                    public void onResponse(Call<Root.Channels> call, Response<Root.Channels> response) {
                        Root.Channels channel = response.body();
                        author.setText(channel.name);
                        Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
                        Picasso picasso = picassoBuilder.build();
                        picasso.load(channel.avatar).error(getDrawable(R.drawable.blank_profile)).into(clientImage);
                        clientImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                                intent.putExtra("idChannel",channel.idChannel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Root.Channels> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Ошибка загрузки аватарки канала", Toast.LENGTH_SHORT).show();
                    }
                });
                Call<Root.Categories> getCategory = appInterface.getCategory(news.idCategory);
                getCategory.enqueue(new Callback<Root.Categories>() {
                    @Override
                    public void onResponse(Call<Root.Categories> call, Response<Root.Categories> response) {
                        tv_category.setText(response.body().name);
                    }
                    @Override
                    public void onFailure(Call<Root.Categories> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Ошибка загрузки категории", Toast.LENGTH_SHORT).show();
                    }
                });
                header.setText(news.header);
                TextView tv = new TextView(getApplicationContext());
                content.setText(news.description.replaceAll("<[^>]*>",""));
                if(news.rateCount != 0 && news.rate != 0){
                    rating.setText(String.format("%.2f",news.rate/(double)news.rateCount));
                }else{
                    rating.setText(String.valueOf(0.0));
                }
            }

            @Override
            public void onFailure(Call<Root.News> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Новость не найдена!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getComments(){
        Call<ArrayList<Root.Comments>> getComments = appInterface.getCommentsList();
        getComments.enqueue(new Callback<ArrayList<Root.Comments>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Root.Comments>> call, Response<ArrayList<Root.Comments>> response) {
                listRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                listRecycler.setHasFixedSize(true);
                ArrayList<Root.Comments> listComments = (ArrayList<Root.Comments>) response.body().stream().filter(n -> n.idNews == idNews).collect(Collectors.toList());
                adapter = new ListAdapter(getApplicationContext(),  listComments);
                listRecycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Root.Comments>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка загрузки комментариев", Toast.LENGTH_SHORT).show();
            }
        });
    }
}