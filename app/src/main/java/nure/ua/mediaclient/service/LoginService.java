package nure.ua.mediaclient.service;

import nure.ua.mediaclient.model.JwtAuthenticationResponse;
import nure.ua.mediaclient.model.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("api/auth/signin")
    Call<JwtAuthenticationResponse> loginUser(
            @Body UserDTO userDTO
    );


}
