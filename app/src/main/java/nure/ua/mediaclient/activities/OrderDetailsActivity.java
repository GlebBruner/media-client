package nure.ua.mediaclient.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.model.ui.OrderUi;

public class OrderDetailsActivity extends AppCompatActivity {

    public static String JSON_ORDERUI;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    private TextView title;
    private TextView fullname;
    private TextView description;
    private TextView category;
    private TextView hashtags;
    private TextView creationDate;
    private TextView timeDL;
    private TextView dateDL;
    private TextView rating;
    private TextView money;
    private TextView photo;
    private TextView video;
    private TextView comments;

    private ImageView thumbUpImg;
    private ImageView thumbDownImg;
    private ImageView commentsImg;
    private ImageView photoImg;
    private ImageView videoImg;
    private ImageView shareImg;
    private ImageView favouriteImg;
    private ImageView moneyImg;
    private ImageView lockedImg;
    private ImageView unlockedImg;
    private ImageView nomoneyImg;

    private ImageView tryResult;

    private Button makeorderButton;

    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private OrderUi orderUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        this.bindviews();
        this.requestMap();
        this.orderUi = (new Gson()).fromJson(getIntent().getStringExtra(JSON_ORDERUI), OrderUi.class);
        this.setDataToFields();

        this.makeorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });



    }



    private void bindviews() {
        this.title = findViewById(R.id.title_so);
        this.fullname = findViewById(R.id.user_name_so);
        this.description = findViewById(R.id.descr_so);
        this.category = findViewById(R.id.category_so);
        this.hashtags = findViewById(R.id.hashtags_so);
        this.creationDate = findViewById(R.id.creation_date_so);
        this.timeDL = findViewById(R.id.timedl_so);
        this.dateDL = findViewById(R.id.datedl_so);
        this.rating = findViewById(R.id.rating_so2);
        this.money = findViewById(R.id.moneycount_so);
        this.photo = findViewById(R.id.photocount_so);
        this.video = findViewById(R.id.vidcount_so);
        this.comments = findViewById(R.id.comments_so);
        this.thumbUpImg = findViewById(R.id.up_so);
        this.thumbDownImg = findViewById(R.id.down_so);
        this.commentsImg = findViewById(R.id.commentsimg_so);
        this.photoImg = findViewById(R.id.photoimg_so);
        this.videoImg = findViewById(R.id.videoimg_so);
        this.shareImg = findViewById(R.id.shareimg_so);
        this.favouriteImg = findViewById(R.id.favouriteimg_so);
        this.moneyImg = findViewById(R.id.moneyimg_so);
        this.lockedImg = findViewById(R.id.locked_so);
        this.unlockedImg = findViewById(R.id.unlockedimg_so);
        this.nomoneyImg = findViewById(R.id.nomoneyImg_so);
        this.makeorderButton = findViewById(R.id.submit_but_od);

    }

    private void requestMap() {
        final MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map_so);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                OrderDetailsActivity.this.googleMap = googleMap;
//              this.requestLocationUpdates(googleMap);
                OrderDetailsActivity.this.googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(orderUi.getLocation().getLatitude(), orderUi.getLocation().getLongitude())));
            }
        });
    }

    private void setDataToFields() {
        StringBuilder stringBuilder = new StringBuilder();
        this.title.setText(orderUi.getTitle());
        this.fullname.setText(orderUi.getFullName());
        this.description.setText(orderUi.getDescription());

        for (String category : orderUi.getCategories()) {
            this.category.setText(category); //todo dumb solution for SET for categories
        }

        for (String hashtag : orderUi.getHashtags()) {
            stringBuilder.append(hashtag);
        }

        this.hashtags.setText(stringBuilder.toString());
        stringBuilder.setLength(0);

        Calendar calendarCreationDate = Calendar.getInstance();
        calendarCreationDate.setTime(orderUi.getCreationDate());
        stringBuilder.append(calendarCreationDate.get(Calendar.DAY_OF_MONTH))
                .append("-")
                .append(calendarCreationDate.get(Calendar.MONTH))
                .append("-")
                .append(calendarCreationDate.get(Calendar.YEAR));

        this.creationDate.setText(stringBuilder.toString());
        stringBuilder.setLength(0);

        Calendar calendarDeadLine = Calendar.getInstance();
        calendarDeadLine.setTime(orderUi.getDeadline());

        stringBuilder.append(calendarDeadLine.get(Calendar.HOUR_OF_DAY))
                .append(":")
                .append(Calendar.MINUTE);
        this.timeDL.setText(stringBuilder.toString());
        stringBuilder.setLength(0);

        stringBuilder.append(calendarDeadLine.get(Calendar.DAY_OF_MONTH))
                .append("-")
                .append(calendarDeadLine.get(Calendar.MONTH))
                .append("-")
                .append(calendarDeadLine.get(Calendar.YEAR));

        this.dateDL.setText(stringBuilder.toString());
        stringBuilder.setLength(0);

        this.rating.setText(String.valueOf(orderUi.getLikes() - orderUi.getDislikes()));

        if (orderUi.getMoney() != 0.0f) {
            this.money.setText(String.valueOf(orderUi.getMoney()));
            this.nomoneyImg.setVisibility(View.INVISIBLE); // todo check
        } else {
            this.money.setText("");
            this.moneyImg.setVisibility(View.INVISIBLE);
        }

        this.photo.setText(String.valueOf(orderUi.getPhotoCount()));
        this.video.setText(String.valueOf(orderUi.getPhotoCount()));

        if (orderUi.isPrivate()) {
            this.unlockedImg.setVisibility(View.INVISIBLE);
        } else {
            this.lockedImg.setVisibility(View.INVISIBLE);
        }

        this.comments.setText(String.valueOf(new Random().nextInt(101)));
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
