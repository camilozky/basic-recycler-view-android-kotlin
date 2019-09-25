package com.example.basic_recycler_view.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList


class DataSource(listeningActivity: ResponseInterface) {

    interface ResponseInterface {
        fun sendResponse(response: ArrayList<ApiMovie>?)
    }

    var isLoadingData: Boolean = false
    private val responseListener: ResponseInterface = listeningActivity
    private val retrofit: Retrofit
    private val service: ApiService

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(ApiService::class.java)
    }

    fun getData() {
        val call = service.getCurrentData(AppId)
        isLoadingData = true

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.code() == 200) {
                    itemResponse(response)
                } else {
                    itemResponse(null)
                }
                isLoadingData = false
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                t.printStackTrace()
                itemResponse(null)
                isLoadingData = false
            }
        })
    }

    private fun itemResponse(apiResponse: Response<ApiResponse>?) {

        apiResponse?.body()?.let { response ->
            responseListener.sendResponse(response.results)
        }
    }

    companion object {
        var BaseUrl = "https://api.themoviedb.org/3/"
        var AppId = "99ac1d44af506e889c0cb61a2ef3fa22"
    }
}
