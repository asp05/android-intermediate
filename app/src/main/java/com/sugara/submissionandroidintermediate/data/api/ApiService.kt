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
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterModel

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginModel

    @GET("stories")
    suspend fun stories(
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 10
    ): StoryModel

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?
    ): AddStoryModel

    @GET("stories/{id}")
    suspend fun storyDetail(
        @Path("id") id: String,
    ): DetailStoryResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location : Int = 1,
    ): StoryModel

}