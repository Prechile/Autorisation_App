package com.githubprechile.autorisation.Metier

import com.githubprechile.autorisation.Retrofit.Interface
import com.githubprechile.autorisation.Retrofit.UnsafeOK_HttpClient
import com.githubprechile.autorisation.model.ApiAutorisation
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit.*
import retrofit2.converter.gson.GsonConverterFactory

class Metier {
    fun envoie(
        person: ApiAutorisation,
        url: String
    ): String? {

        val server = MockWebServer()
        server.enqueue(MockResponse())
        try {

            server.start()
            val retrofit2 = Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOK_HttpClient.getUnsafeOkHttpClient().build())
                .build()
            val service = retrofit2.create<Interface>(Interface::class.java)
            val response = service.envoiePersonne(person).execute()

            return if (response.code() == 200 ) {
                 response.body()
                   // return person
            } else {
                null
            }
        } catch (ex: Exception) {
            println(ex)
        }
        return null

    }
}


