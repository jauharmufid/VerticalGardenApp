package com.example.verticalgarden

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


data class SensorReadingRequest(
    val sensor_readings: List<Double>
)
interface ApiService {
    @POST("/predict")
    fun getPrediction(@Body requestData: SensorReadingRequest): Call<ResponseBody>
}

