package com.sugara.submissionandroidintermediate.data.api

import com.sugara.submissionandroidintermediate.data.model.AddStoryModel
import com.sugara.submissionandroidintermediate.data.model.DetailStoryResponse
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.data.model.RegisterModel
import com.sugara.submissionandroidintermediate.data.model.StoryModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginModel>

    @GET("stories")
    fun stories(
        @Header("Authorization") token: String,
    ): Call<StoryModel>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Header("Authorization") token: String,
    ): Call<AddStoryModel>

    @GET("stories/{id}")
    fun storyDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Call<DetailStoryResponse>


}