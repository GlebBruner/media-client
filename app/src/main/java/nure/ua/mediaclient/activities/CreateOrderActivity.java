package nure.ua.mediaclient.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private Calendar calendarForDeadLine;

    private SharedPreferences preferences;


    //all with maps
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currLocationMarker;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_create);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_co);
        mapFragment.getMapAsync(this);



        title = findViewById(R.id.titleedit_co);
        dateDL = findViewById(R.id.date_co);
        dateDL.setFocusable(false);
        dateDL.setClickable(true);
        timeDL = findViewById(R.id.time_co);
        timeDL.setFocusable(false);
        timeDL.setClickable(true);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);


        hashtagsList = new ArrayList<>();


        ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getAllCategories());
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);

        ArrayAdapter<Integer> photoVideoAdatper = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getIntegersForPhotoAndVideo());
        photoVideoAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoSpinner.setAdapter(photoVideoAdatper);
        videoSpinner.setAdapter(photoVideoAdatper);


        calendarForDeadLine = Calendar.getInstance();

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
                        calendarForDeadLine.set(i, i1, i2);
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
                        calendarForDeadLine.set(Calendar.HOUR_OF_DAY , i);
                        calendarForDeadLine.set(Calendar.MINUTE , i1);
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
                Date deadLine = calendarForDeadLine.getTime();

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
                                .header("Authorization",
                                        "Bearer: " + preferences.getString(SharedProperties.TOKEN,
                                                "no token found"));
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

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Your Place");
                googleMap.addMarker(markerOptions);
            }
        });

    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                lastLocation = location;
                if (currLocationMarker != null) {
                    currLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                currLocationMarker = googleMap.addMarker(markerOptions);

                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    /*@Override
    protected void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        this.googleMap = googleMap;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(120000); // two minute interval
        locationRequest.setFastestInterval(120000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CreateOrderActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
