package com.myvetpath.myvetpath.data;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


//This class will be used for POST functionality regarding the API, but we don't currently have the API.
//Followed this guide: https://www.youtube.com/watch?v=Bv-KAxPOCzY


public interface MyVetPathAPI {

    String BASE_URL = "https://www.reddit.com/"; //this is a different API. It is completely unrelated to our project, it is mostly here to prove that the app can use an api to post

    @Headers("Content-Type: application/json")
    @GET(".json")
    Call<Feed> getData();


    //This is a function related to the Reddit API. It won't be used for our project, it is mostly here to show that the app can use an API to post
    @POST("{user}")
    Call<ResponseBody> login(
            @HeaderMap Map<String, String> headers,
            @Path("user") String username,
            @Query("user") String user,       //?user=
            @Query("passwd") String password, //&passwd=
            @Query("api-type") String type    //&api-type=json
    );

    //Query to post submission
//    @POST("{submission}")
//    Call<ResponseBody> submission(
//            @HeaderMap Map<String, String> headers,
//            @Query("Group_ID") Long Group_ID,
//            @Query("User_ID") Integer User_ID,
//            @Query("Title") String title,
//            @Query("DateOfCreation") Long DateOfCreation,
//            @Query("StatusFlag") Integer StatusFlag,
//            @Query("Submitted") Long Submitted,
//            @Query("ReportComplete") Long ReportComplete,
//            @Query("UserComment") String UserComment,
//            @Query("api-type") String type
//    );

    //the bearer token expires after a set time. If you get an unathorized response, then post user again and take the bearer token sent in response.
    // We will need to figure out a better way to handle this when we get login

    @Headers({"Content-Type: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMywiaWF0IjoxNTU3OTY3NjMwLCJleHAiOjE1NTgwNTQwMzB9.SArqvMg9PcDm6KaFL1RrdANWAYK3KlxhyyIDuxTj-X8"})
    @POST("submissions")
    Call<SubmissionTable> submission(@Body SubmissionTable submission);

    @GET("secretinfo")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);


    //Query to post reply
    @POST("reply")
    Call<ResponseBody> reply(@Body ReplyTable reply);

    //Query to post sample
    @POST("{sample}")
    Call<ResponseBody> sample(@Body SampleTable sample);

    //Query to post picture
    @POST("{picture}")
    Call<ResponseBody> picture(@Body PictureTable picture);

    //Query to post report
    @POST("{report}")
    Call<ResponseBody> report(@Body ReportTable report);

    //Query to post patient
    @POST("{patient}")
    Call<ResponseBody> patient(@Body PatientTable patient);

}
