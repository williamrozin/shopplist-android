package com.example.william.shopplist.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by walte on 20/10/2017.
 */

public class ServerConnection {
    private static ServerConnection instance = null;
    private ServerInterface server;

    public static ServerConnection getInstance(){
        if(instance == null){
            instance = new ServerConnection();
        }
        return instance;
    }

    private ServerConnection(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.25.12:8080/dsi2017web/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        server = retrofit.create(ServerInterface.class);
    }

    public ServerInterface getServer() {
        return server;
    }
}
