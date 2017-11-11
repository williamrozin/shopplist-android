package com.example.william.shopplist.server;

import com.example.william.shopplist.model.ShoppingList;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by walte on 18/10/2017.
 */

public interface ServerInterface {

    @Headers("userId: 1")
    @GET("lists/")
    public Call<List<ShoppingList>> getAllShoppingLists();
/*

    @POST("cliente/")
    public Call<String> insertClientes(@Body Cliente c);
*/

}
