package nure.ua.mediaclient.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.model.OrderDTO;
import nure.ua.mediaclient.model.domain.Point;
import nure.ua.mediaclient.model.ui.OrderUi;
import nure.ua.mediaclient.service.OrderService;
import nure.ua.mediaclient.util.HTTP;
import nure.ua.mediaclient.util.SharedProperties;
import nure.ua.mediaclient.util.SpinnerBodies;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CreateOrderActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int LOCATION_UPDATE_INTERVAL_DEFAULT = 30000;
    private static final int ZOOM_DEFAULT = 11;

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

    private List<String> hashtagsList = new ArrayList<>();
    private Calendar calendarForDeadLine = Calendar.getInstance();
    private Point locatedMarker = new Point();

    private SharedPreferences preferences;

    private GoogleMap googleMap;
    private Marker currLocationMarker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private OrderService orderService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.order_create);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.preferences = this.getSharedPreferences(SharedProperties.APP_PREFERENCES, Context.MODE_PRIVATE);
        this.initializeNetworkClient();
        this.requestMap();
        this.bindViews();
        this.configureViews();
    }

    private void configureViews() {
        final ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                SpinnerBodies.getAllCategories());
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.categorySpinner.setAdapter(categoriesSpinnerAdapter);

        final ArrayAdapter<Integer> photoVideoAdatper = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                SpinnerBodies.getIntegersForPhotoAndVideo());
        photoVideoAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.photoSpinner.setAdapter(photoVideoAdatper);
        this.videoSpinner.setAdapter(photoVideoAdatper);

        this.dateDL.setOnClickListener(view -> this.showDatePickingDialog());
        this.timeDL.setOnClickListener(view -> this.showTimePickingDialog());
        this.addHashtag.setOnClickListener(view -> this.addHashTag());
        this.deleteHashtag.setOnClickListener(view -> this.deleteLastHashTag());
        this.createOrder.setOnClickListener(v -> this.createOrder());
    }

    private void createOrder() {
        final boolean paid = this.money.getText() != null && this.money.getText().toString().equals("0");
        final Set<String> categories = new HashSet<>();
        categories.add(this.categorySpinner.getSelectedItem().toString());

        final OrderDTO dto = new OrderDTO(
                this.from(this.title),
                false, // paid
                this.isPrivate.isChecked(),
                this.from(this.description),
                Integer.valueOf(this.photoSpinner.getSelectedItem().toString()),
                Integer.valueOf(this.videoSpinner.getSelectedItem().toString()),
                new HashSet<>(this.hashtagsList),
                categories,
                OrderDTO.NO_LIKES,
                OrderDTO.NO_DISLIKES,
                locatedMarker,
                new Date(),
                this.calendarForDeadLine.getTime(),
                0.0f // money
        );

        if (this.money.getText().toString().equals("") ||
                this.money.getText().toString().equals(" ") ||
                this.money.getText().toString().equals("0")) {
            dto.setPaid(false);
            dto.setMoney(0.0f);
        } else {
            dto.setPaid(true);
            dto.setMoney(Float.valueOf(this.money.getText().toString()));
        }

        Call<OrderUi> call = this.orderService.createOrder(dto);

        call.enqueue(new Callback<OrderUi>() {

            @Override
            public void onResponse(Call<OrderUi> call, Response<OrderUi> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(CreateOrderActivity.this,
                            "Created" + response.code() + response.message(),
                            Toast.LENGTH_LONG).show();
                    CreateOrderActivity.this.finish();
                } else {
                    Toast.makeText(CreateOrderActivity.this,
                            "not good "+response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderUi> call, Throwable t) {
                Toast.makeText(CreateOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteLastHashTag() {
        if (!this.hashtagsList.isEmpty()) {
            this.hashtagsList.remove(this.hashtagsList.size() - 1);
        }
        final StringBuilder builder = new StringBuilder();
        this.hashtags.setText("");
        for (String hashtag : this.hashtagsList) {
            builder.append(hashtag)
                    .append(" ");
        }
        this.hashtagsView.setText(builder.toString());
    }

    private void addHashTag() {
        this.hashtagsList.add(this.hashtags.getText().toString());
        this.hashtags.setText("");
        this.hashtagsView.append(this.hashtagsList.get(this.hashtagsList.size() - 1) + " ");
    }

    private void showTimePickingDialog() {
        final Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int min = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, inputHour, inputMinute) -> {
            this.calendarForDeadLine.set(Calendar.HOUR_OF_DAY, inputHour);
            this.calendarForDeadLine.set(Calendar.MINUTE, inputMinute);
            this.timeDL.setText(this.getResources().getString(R.string.chosen_time_content, inputHour, inputMinute));
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateOrderActivity.this, timeSetListener, hour, min, true);
        timePickerDialog.show();
    }

    private void showDatePickingDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, inputYear, inputMonth, inputDay) -> {
            this.calendarForDeadLine.set(inputYear, inputMonth, inputDay);
            this.dateDL.setText(this.getResources().getString(R.string.chosen_date_content, inputYear, inputMonth, inputDay));
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrderActivity.this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void requestMap() {
        final MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map_co);
        mapFragment.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
            this.requestLocationUpdates(googleMap);
            googleMap.setOnMapClickListener(latLng -> {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Your Place");
                locatedMarker.setLatitude((float) latLng.latitude); //todo check for cast
                locatedMarker.setLongitude((float) latLng.longitude);
                googleMap.addMarker(markerOptions);
            });
        });
    }

    private void bindViews() {
        this.title = this.findViewById(R.id.titleedit_co);
        this.dateDL = this.findViewById(R.id.date_co);
        this.timeDL = this.findViewById(R.id.time_co);
        this.money = this.findViewById(R.id.money_co);
        this.hashtags = this.findViewById(R.id.hashtags_co);
        this.description = this.findViewById(R.id.descr_edit_co);
        this.createOrder = this.findViewById(R.id.createorder_co);
        this.addHashtag = this.findViewById(R.id.add_hash_co);
        this.deleteHashtag = this.findViewById(R.id.delete_hash_co);
        this.categorySpinner = this.findViewById(R.id.cat_spinner);
        this.photoSpinner = this.findViewById(R.id.ph_spinner);
        this.videoSpinner = this.findViewById(R.id.vid_spinner);
        this.isPrivate = this.findViewById(R.id.private_check_co);
        this.hashtagsView = this.findViewById(R.id.hashview_co);
    }

    private void requestLocationUpdates(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(CreateOrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.fusedLocationProviderClient.requestLocationUpdates(new LocationRequest()
                    .setInterval(LOCATION_UPDATE_INTERVAL_DEFAULT)
                    .setFastestInterval(LOCATION_UPDATE_INTERVAL_DEFAULT)
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final List<Location> locationList = locationResult.getLocations();
                    if (!locationList.isEmpty()) {
                        final Location location = locationList.get(locationList.size() - 1);
                        if (CreateOrderActivity.this.currLocationMarker != null) {
                            CreateOrderActivity.this.currLocationMarker.remove();
                        }
                        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        final MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title("Current Position")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        CreateOrderActivity.this.currLocationMarker = CreateOrderActivity.this.googleMap.addMarker(markerOptions);
                        CreateOrderActivity.this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT));
                    }
                }
            }, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        } else {
            this.checkLocationPermission();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateOrderActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(CreateOrderActivity.this)
                        .setTitle(R.string.rationale_location_permission_dialog_title)
                        .setMessage(R.string.rationale_location_permission_dialog_message)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(CreateOrderActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION))
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.requestLocationUpdates(this.googleMap);
                }
            }
        }
    }

    private void initializeNetworkClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization",
                            "Bearer " + this.preferences.getString(SharedProperties.TOKEN,
                                    "no token found"));
            return chain.proceed(requestBuilder.build());
        });


        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(HTTP.URI)
                .addConverterFactory(JacksonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder
                .client(httpClient.build())
                .build();

        this.orderService = retrofit.create(OrderService.class);
    }

    private String from(final EditText editText) {
        return editText.getText().toString();
    }
}