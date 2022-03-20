package hector.developers.contrapp.Api;

import java.util.List;

import hector.developers.contrapp.model.UserHealthData;
import hector.developers.contrapp.model.UserLocation;
import hector.developers.contrapp.model.Users;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("user")
    Call<ResponseBody> createUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("age") String age,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("gender") String gender,
            @Field("address") String address,
            @Field("password") String password,
            @Field("state") String state,
            @Field("lga") String lga,
            @Field("nin") String nin,
            @Field("nextOfKin") String nextOfKin,
            @Field("relationShipWithNextOfKin") String relationShipWithNextOfKin,
            @Field("nextOfKinPhone") String nextOfKinPhone
    );

    @FormUrlEncoded
    @POST("userHealthData")
    Call<UserHealthData> createUserHealthData(
            @Field("date") String date,
            @Field("feverSymptom") String feverSymptom,
            @Field("headacheSymptom") String headacheSymptom,
            @Field("sneezingSymptom") String sneezingSymptom,
            @Field("chestPainSymptom") String chestPainSymptom,
            @Field("bodyPainSymptom") String bodyPainSymptom,
            @Field("nauseaOrVomitingSymptom") String nauseaOrVomitingSymptom,
            @Field("diarrhoeaSymptom") String diarrhoeaSymptom,
            @Field("fluSymptom") String fluSymptom,
            @Field("soreThroatSymptom") String soreThroatSymptom,
            @Field("fatigueSymptom") String fatigueSymptom,

            @Field("newOrWorseningCough") String newOrWorseningCough,
            @Field("difficultyInBreathing") String difficultyInBreathing,
            @Field("lossOfOrDiminishedSenseOfSmell") String lossOfOrDiminishedSenseOfSmell,
            @Field("lossOfOrDiminishedSenseOfTaste") String lossOfOrDiminishedSenseOfTaste,

            @Field("contactWithFamily") String contactWithFamily,

            @Field("userId") Long userId,
            @Field("phone") String phone,
            @Field("firstname") String firstname,
            @Field("risk") String risk
    );

    //the users login call
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("login")
    Call<Users> login(
            @Field("email") String email,
            @Field("password") String password
    );

    //fetching all users
    @GET("users")
    Call<List<Users>> getUsers();

    //fetching all user health data
    @GET("userHealthData")
    Call<List<UserHealthData>> getUserHealthData();

    //fetching users by Id
    @GET("users/{id}")
    Call<List<Users>> getUsersById(@Path("id") Long id);


    //fetching user health data
    @GET("userHealthData/{userId}")
    Call<List<UserHealthData>> getUserHealthByUserId(@Path("userId") Long userId);

    @POST("location")
    Call<UserLocation> createUserLocation(@Body UserLocation userLocation);

    @PUT("location/{userId}")
    Call<UserLocation> updateUserLocation(@Body UserLocation userLocation, @Path("userId") Long userId);


}
