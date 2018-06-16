package nure.ua.mediaclient.activities;


import android.content.Intent;
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

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.activities.orders.OrdersActivityAdapter;
import nure.ua.mediaclient.activities.orders.comparator.BestComparator;
import nure.ua.mediaclient.activities.orders.comparator.DiscussedComparator;
import nure.ua.mediaclient.activities.orders.comparator.HotComparator;
import nure.ua.mediaclient.activities.orders.comparator.NewComparator;
import nure.ua.mediaclient.model.ui.OrderUi;

public class FeedActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private OrdersActivityAdapter adapter;
    private FloatingActionButton createOrderFab;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        tabLayout = findViewById(R.id.sliding_tabs);

        adapter = new OrdersActivityAdapter(orderUi -> { }, this.getResources());




        final RecyclerView recyclerView = findViewById(R.id.content_list);
        recyclerView.setAdapter(this.adapter);

        final LinearLayoutManager llm = new LinearLayoutManager(FeedActivity.this); // todo CHECK

        recyclerView.setLayoutManager(llm);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.fillTabLayout();

        createOrderFab = findViewById(R.id.fab);

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
        comparatorMap.put(resources.getString(R.string.tab_new_feed_activity), new NewComparator());
        comparatorMap.put(resources.getString(R.string.tab_hot_feed_activity), new HotComparator());
        comparatorMap.put(resources.getString(R.string.tab_discussed_feed_activity), new DiscussedComparator());
        comparatorMap.put(resources.getString(R.string.tab_best_feed_activity), new BestComparator());
        return comparatorMap;
    }
}
