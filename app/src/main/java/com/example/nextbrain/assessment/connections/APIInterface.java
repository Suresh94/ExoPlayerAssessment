package com.example.nextbrain.assessment.connections;

import com.example.nextbrain.assessment.model.Req;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("media.json?print=pretty")
    Call<List<Req>> getMovieData();
}
