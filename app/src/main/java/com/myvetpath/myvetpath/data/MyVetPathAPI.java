package com.myvetpath.myvetpath.data;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
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
    @POST("{submission}")
    Call<ResponseBody> submission(
            @HeaderMap Map<String, String> headers,
            @Path("submissions") String submissions,
            @Query("Group_ID") Long Group_ID,
            @Query("User_ID") Integer User_ID,
            @Query("Title") String title,
            @Query("DateOfCreation") Long DateOfCreation,
            @Query("StatusFlag") Integer StatusFlag,
            @Query("Submitted") Long Submitted,
            @Query("ReportComplete") Long ReportComplete,
            @Query("UserComment") String UserComment,
            @Query("api-type") String type
    );

    //Query to post reply
    @POST("{reply}")
    Call<ResponseBody> reply(
            @HeaderMap Map<String, String> headers,
            @Path("reply") String reply,
            @Query("Reply_ID") Integer Reply_ID,
            @Query("Master_ID") Long Master_ID,
            @Query("Sender_ID") Integer sender_ID,
            @Query("Receiver_ID") Integer Receiver_ID,
            @Query("ContentsOfMessage") String contents,
            @Query("DateOfMessage") Long DateOfMessage,
            @Query("api-type") String type
    );

    //Query to post sample
    @POST("{sample}")
    Call<ResponseBody> sample(
            @HeaderMap Map<String, String> headers,
            @Path("sample") String sample,
            @Query("Sample_ID") Integer Sample_ID,
            @Query("Master_ID") Long Master_ID,
            @Query("LocationOfSample") String LocationOfSample,
            @Query("NumberOfSample") Integer NumberOfSamples,
            @Query("NameOfSample") String name,
            @Query("api-type") String type
    );

    //Query to post picture
    @POST("{picture}")
    Call<ResponseBody> picture(
            @HeaderMap Map<String, String> headers,
            @Path("picture") String picture,
            @Query("Image_ID") Integer ImageID,
            @Query("Master_ID") Long Master_ID,
            @Query("ImagePath") String ImagePath,
            @Query("Title") String title,
            @Query("Latitude") String latitude,
            @Query("Longitude") String longitude,
            @Query("DateTaken") Long DateTaken,
            @Query("api-type") String type
    );

    //Query to post report
    @POST("{report}")
    Call<ResponseBody> report(
            @HeaderMap Map<String, String> headers,
            @Path("report") String report,
            @Query("User_ID") Long UserID,
            @Query("Master_ID") Long Master_ID,
            @Query("Submission_Review") String Submission_Review,
            @Query("Conclusion") String conclusion,
            @Query("DateClosed") Float DateClosed,
            @Query("ReportDate") Float reportDate,
            @Query("Attachments") String Attachments,
            @Query("api-type") String type
    );

    //Query to post patient
    @POST("{patient}")
    Call<ResponseBody> patient(
            @HeaderMap Map<String, String> headers,
            @Path("patient") String patient,
            @Query("Patient_ID") Integer PatientID,
            @Query("Master_ID") Long Master_ID,
            @Query("PatientName") String PatientName,
            @Query("Species") String species,
            @Query("Sex") String sex,
            @Query("Euthanized") Integer euthanized,
            @Query("DateOfBirth") Long DateOfBirth,
            @Query("DateOfDeath") Long DateOfDeath,
            @Query("api-type") String type
    );

}
