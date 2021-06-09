package com.app.teachertestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.app.teachertestapp.adapter.MessageRecyclerAdapter;
import com.app.teachertestapp.apiservice.ApiClient;
import com.app.teachertestapp.apiservice.ApiInterface;
import com.app.teachertestapp.common.ItemOffsetDecoration;
import com.app.teachertestapp.model.MessageModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MessageRecyclerAdapter adapter;
    ProgressDialog progressDialog;
    List<MessageModel.MessageList> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);



        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        getMessageList();
    }

    private void getMessageList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        ApiInterface service = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<String> call = service.getMessageList();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String>
                    response) {



                String str=response.body();
                str=str.replace("?xml version=\"1.0\"encoding=\"utf-8\"?","");
                str=str.replace("<string"," ");
                str=str.replace("</string>"," ");

                Gson gson=new Gson();
                List<MessageModel.MessageList> data=gson.fromJson(str,new TypeToken<List<MessageModel.MessageList>>(){

                }.getType());

                adapter=new MessageRecyclerAdapter(data,MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();




            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}