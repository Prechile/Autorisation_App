package com.githubprechile.autorisation.Retrofit

import com.githubprechile.autorisation.model.ApiAutorisation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Interface {

    @POST("/api/Authorisation")
    fun envoiePersonne(
        @Body apiAut: ApiAutorisation
    ): Call<String>

}