package nure.ua.mediaclient.service;

import nure.ua.mediaclient.model.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Registration {

    @POST("api/auth/registration")
    Call<Object> registerUser(@Body UserDTO userDTO);

}
