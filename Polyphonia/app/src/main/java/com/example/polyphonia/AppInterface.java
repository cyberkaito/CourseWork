package com.example.polyphonia;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppInterface {
    @GET("Clients")
    Call<ArrayList<Root.Clients>> getClientList();
    @GET("Clients/{id}")
    Call<Root.Clients> getClient(@Path("id") int id);
    @Headers({"accept: text/plain","Content-Type: application/json"})
    @POST("Clients")
    Call<Root.Clients> postClient(@Body Root.Clients client);
    @DELETE("Clients/{id}")
    Call<Root.Clients> deleteClient(@Path("id") int id);
    @GET("Clients/Auth")
    Call<ResponseBody> Auth(@Query("login") String login, @Query("password") String password);

    @GET("News")
    Call<ArrayList<Root.News>> getNewsList();
    @GET("News/{id}")
    Call<Root.News> getNews(@Path("id") int id);
    @Headers({"Content-Type: application/json"})
    @PUT("News/{id}")
    Call<Root.News> updateNews(@Path("id") int id, @Body Root.News news);

    @GET("Categories")
    Call<ArrayList<Root.Categories>> getCategoriesList();
    @GET("Categories/{id}")
    Call<Root.Categories> getCategory(@Path("id") int id);

    @GET("Media")
    Call<ArrayList<Root.Media>> getMediaList();

    @GET("Comments")
    Call<ArrayList<Root.Comments>> getCommentsList();


    @POST("Comments")
    Call<Root.Comments> postComment(@Body Root.Comments comments);

    @GET("Channels/{id}")
    Call<Root.Channels> getChannel(@Path("id") int id);
    @GET("Channels")
    Call<ArrayList<Root.Channels>> getChannelList();


    @GET("ClientTypes")
    Call<ArrayList<Root.ClientTypes>> getClientTypesList();
    @POST("ClientTypes")
    Call<Root.ClientTypes> postClientType(@Body Root.ClientTypes clientTypes);
    @DELETE("ClientTypes/{id}")
    Call<Root.ClientTypes> deleteClientType(@Path("id") int id);

}