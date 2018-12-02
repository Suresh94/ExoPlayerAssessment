package com.example.nextbrain.assessment.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.example.nextbrain.assessment.R;
import com.example.nextbrain.assessment.adapters.MoviesAdapter;
import com.example.nextbrain.assessment.connections.APIClient;
import com.example.nextbrain.assessment.connections.APIInterface;
import com.example.nextbrain.assessment.model.Req;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private ProgressDialog dialog;

    APIInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("mainAct", "true");
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading Data..");
        dialog.show();
        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);


        /**
         GET List Resources
         **/
        Call<List<Req>> call = apiInterface.getMovieData();
        call.enqueue(new Callback<List<Req>>() {
            @Override
            public void onResponse(Call<List<Req>> call, Response<List<Req>> response) {

                dialog.dismiss();

                Log.e("Resp", new Gson().toJson(response.body()));

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                mAdapter = new MoviesAdapter(response.body(), getApplicationContext());

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

                recyclerView.setLayoutManager(layoutManager);

                 recyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onFailure(Call<List<Req>> call, Throwable t) {
                Log.e("failure", t.getMessage());


            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
