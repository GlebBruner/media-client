package nure.ua.mediaclient.service;

import java.util.List;

import nure.ua.mediaclient.model.JwtAuthenticationResponse;
import nure.ua.mediaclient.model.OrderDTO;
import nure.ua.mediaclient.model.ui.OrderUi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderService {

    @POST("api/orders")
    Call<OrderUi> createOrder(
            @Body OrderDTO orderDTO
    );

    @GET("/api/orders")
    Call<List<OrderUi>> getAllOrders();

}
