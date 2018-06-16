package nure.ua.mediaclient.activities;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.model.OrderDTO;
import nure.ua.mediaclient.service.OrderService;
import nure.ua.mediaclient.service.RegistrationService;
import nure.ua.mediaclient.util.HTTP;
import nure.ua.mediaclient.util.SharedProperties;
import nure.ua.mediaclient.util.SpinnerBodies;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CreateOrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText title;
    private EditText dateDL;
    private EditText timeDL;
    private EditText money;
    private EditText hashtags;
    private EditText description;

    private Button createOrder;
    private Button addHashtag;
    private Button deleteHashtag;

    private Spinner categorySpinner;
    private Spinner photoSpinner;
    private Spinner videoSpinner;

    private TextView category;
    private TextView photo;
    private TextView video;
    private TextView deadline;
    private TextView moneyTv;
    private TextView privateCheck;
    private TextView hashtagsView;

    private CheckBox isPrivate;

    private List<String> hashtagsList;

    private MapFragment mapFragment;
    private GoogleMap googleMap;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_create);

        /*mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.constraint_co, mapFragment);
        fragmentTransaction.commit();*/


        title = findViewById(R.id.titleedit_co);
        dateDL = findViewById(R.id.date_co);
        timeDL = findViewById(R.id.time_co);
        money = findViewById(R.id.money_co);
        hashtags = findViewById(R.id.hashtags_co);
        description = findViewById(R.id.descr_edit_co);

        createOrder = findViewById(R.id.createorder_co);
        addHashtag = findViewById(R.id.add_hash_co);
        deleteHashtag = findViewById(R.id.delete_hash_co);

        categorySpinner = findViewById(R.id.cat_spinner);
        photoSpinner = findViewById(R.id.ph_spinner);
        videoSpinner = findViewById(R.id.vid_spinner);

        isPrivate = findViewById(R.id.private_check_co);

        hashtagsView = findViewById(R.id.hashview_co);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_co);
        mapFragment.getMapAsync(this);





        hashtagsList = new ArrayList<>();

        dateDL.setFocusable(false);
        dateDL.setClickable(true);

        timeDL.setFocusable(false);
        timeDL.setClickable(true);


        ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getAllCategories());
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);

        ArrayAdapter<Integer> photoVideoAdatper = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getIntegersForPhotoAndVideo());
        photoVideoAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoSpinner.setAdapter(photoVideoAdatper);
        videoSpinner.setAdapter(photoVideoAdatper);

        // dialogs

        final Calendar calendar = Calendar.getInstance();

        dateDL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        dateDL.setText(i + " " + i1 + " " + i2);
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrderActivity.this, dateSetListener , year, month, day);
                datePickerDialog.show();
            }


        });

        timeDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int min = myCalendar.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY , i);
                        calendar.set(Calendar.MINUTE , i1);
                        timeDL.setText(i + " : " + i1);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateOrderActivity.this, timeSetListener, hour, min, true);
                timePickerDialog.show();
            }

        });


        addHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashtagsList.add(hashtags.getText().toString());
                hashtags.setText("");
                hashtagsView.append(hashtagsList.get(hashtagsList.size() - 1) + " ");
            }
        });

        deleteHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashtagsList.remove(hashtagsList.size() - 1);
                hashtagsView.setText("");
                for (String hashtag : hashtagsList) {
                    hashtagsView.append(hashtag + " ");
                }
            }
        });

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date deadLine = calendar.getTime();

                OrderDTO orderDTOtoSend = new OrderDTO();
                if (money.getText().toString().equals("0") || money.getText() == null) {
                    orderDTOtoSend.setMoney(0f);
                    orderDTOtoSend.setPaid(false);
                } else {
                    orderDTOtoSend.setMoney(Float.parseFloat(money.getText().toString()));
                    orderDTOtoSend.setPaid(true);
                }

                orderDTOtoSend.setTitle(title.getText().toString());
                orderDTOtoSend.setPrivate(isPrivate.isChecked());
                orderDTOtoSend.setDescription(description.getText().toString());
                orderDTOtoSend.setPhotoCount(Integer.valueOf(photoSpinner.getSelectedItem().toString()));
                orderDTOtoSend.setVideoCount(Integer.valueOf(videoSpinner.getSelectedItem().toString()));

                Set<String> categories = new HashSet<>();
                categories.add(categorySpinner.getSelectedItem().toString());
                orderDTOtoSend.setCategories(categories);

                Set<String> hashSet = new HashSet<>(hashtagsList);
                orderDTOtoSend.setHashtags(hashSet);

                orderDTOtoSend.setLikes(0);
                orderDTOtoSend.setDislikes(0);

                orderDTOtoSend.setCreationDate(new Date());
                orderDTOtoSend.setDeadline(deadLine);

//todo add point
                orderDTOtoSend.setLocation(new nure.ua.mediaclient.model.domain.Point(0f,0f));




                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                preferences = getSharedPreferences(SharedProperties.APP_PREFERENCES, Context.MODE_PRIVATE);
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", preferences.getString(SharedProperties.TOKEN, " "));
                        return chain.proceed(requestBuilder.build());
                    }
                });


                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(HTTP.URI)
                        .addConverterFactory(JacksonConverterFactory.create());
                Retrofit retrofit = retrofitBuilder
                        .client(httpClient.build())
                        .build();

                OrderService orderService = retrofit.create(OrderService.class);

                Call<List<OrderDTO>> call = orderService.createOrder(orderDTOtoSend);

                call.enqueue(new Callback<List<OrderDTO>>() {

                    @Override
                    public void onResponse(Call<List<OrderDTO>> call, Response<List<OrderDTO>> response) {
                        Toast toast = Toast.makeText(CreateOrderActivity.this, "Created", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<List<OrderDTO>> call, Throwable t) {
                        Toast toast = Toast.makeText(CreateOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });



            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
