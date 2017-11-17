package com.example.william.shopplist.server;

import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.model.User;
import com.example.william.shopplist.model.Login;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by walte on 18/10/2017.
 */

public interface ServerInterface {

    @GET("lists/")
    public Call<List<ShoppingList>> getAllShoppingLists(@Header("userId") long userId);

    @GET("meta-item/")
    public Call<List<MetaItem>> getAllMetaItems(@Header("userId") long userId);

    @GET("category/")
    public Call<List<Category>> getAllCategories(@Header("userId") long userId);

    @Headers("Content-type: application/json")
    @POST("user/login")
    public Call<User> login(@Body Login login);

    @Headers("Content-type: application/json")
    @POST("user/signup")
    public Call<User> signup(@Body User user);

    @Headers("Content-type: application/json")
    @POST("lists")
    public Call<ShoppingList> createShoppingList(@Body ShoppingList shoppingList);

    @PUT("item/{id}/check")
    public Call<Void> checkItem(@Path("id") long id);

    @PUT("item/{id}/uncheck")
    public Call<Void> uncheckItem(@Path("id") long id);
/*

    @POST("cliente/")
    public Call<String> insertClientes(@Body Cliente c);
*/

}
