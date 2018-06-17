package nure.ua.mediaclient.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.activities.orders.OnOrderClickListener;
import nure.ua.mediaclient.activities.orders.OrdersActivityAdapter;
import nure.ua.mediaclient.activities.orders.comparator.BestComparator;
import nure.ua.mediaclient.activities.orders.comparator.DiscussedComparator;
import nure.ua.mediaclient.activities.orders.comparator.HotComparator;
import nure.ua.mediaclient.activities.orders.comparator.NewComparator;
import nure.ua.mediaclient.model.ui.OrderUi;
import nure.ua.mediaclient.service.OrderService;
import nure.ua.mediaclient.util.HTTP;
import nure.ua.mediaclient.util.SharedProperties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FeedActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private OrdersActivityAdapter adapter;
    private FloatingActionButton createOrderFab;
    private SharedPreferences preferences;
    private OrderService orderService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        tabLayout = findViewById(R.id.sliding_tabs);
        createOrderFab = findViewById(R.id.fab);
        this.initializeNetworkClient();
        this.preferences = this.getSharedPreferences(SharedProperties.APP_PREFERENCES, Context.MODE_PRIVATE);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        final RecyclerView recyclerView = findViewById(R.id.content_list);
        final LinearLayoutManager llm = new LinearLayoutManager(FeedActivity.this);
        recyclerView.setLayoutManager(llm);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.fillTabLayout();


        Call<List<OrderUi>> call = this.orderService.getAllOrders();
        call.enqueue(new Callback<List<OrderUi>>() {

            @Override
            public void onResponse(Call<List<OrderUi>> call, Response<List<OrderUi>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedActivity.this,
                            response.code() + " " + response.message(),
                            Toast.LENGTH_LONG).show();
                    FeedActivity.this.adapter.refresh(response.body());
                } else {
                    Toast.makeText(FeedActivity.this,
                            response.code() + " " + response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<OrderUi>> call, Throwable t) {
                Toast.makeText(FeedActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        adapter = new OrdersActivityAdapter(new OnOrderClickListener() {
            @Override
            public void onClick(OrderUi orderUi) {
                Intent intent = new Intent(FeedActivity.this, OrderDetailsActivity.class);
                intent.putExtra(OrderDetailsActivity.JSON_ORDERUI, (new Gson()).toJson(orderUi));
                startActivity(intent);
            }
        }, this.getResources());

        recyclerView.setAdapter(this.adapter);



        createOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FeedActivity.this, CreateOrderActivity.class));
            }
        });
    }

    private void fillTabLayout() {
        final Resources resources = this.getResources();
        final Map<String, Comparator<OrderUi>> comparatorMap = this.mapComparatorsToMessages(resources);
        for (final String title : comparatorMap.keySet()) {
            final TabLayout.Tab tab = this.tabLayout.newTab();
            this.tabLayout.addTab(tab.setText(title));
        }
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                FeedActivity.this.adapter.reorder(comparatorMap.get(tab.getText().toString()));
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });
    }

    @NonNull
    private Map<String, Comparator<OrderUi>> mapComparatorsToMessages(final Resources resources) {
        final Map<String, Comparator<OrderUi>> comparatorMap = new ArrayMap<>();
        comparatorMap.put(resources.getString(R.string.tab_best_feed_activity), new BestComparator());
        comparatorMap.put(resources.getString(R.string.tab_new_feed_activity), new NewComparator());
        comparatorMap.put(resources.getString(R.string.tab_hot_feed_activity), new HotComparator());
        comparatorMap.put(resources.getString(R.string.tab_discussed_feed_activity), new DiscussedComparator());
        return comparatorMap;
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
}
