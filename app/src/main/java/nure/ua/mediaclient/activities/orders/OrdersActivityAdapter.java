package nure.ua.mediaclient.activities.orders;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.activities.orders.comparator.NullObjectComparator;
import nure.ua.mediaclient.model.ui.OrderUi;

public class OrdersActivityAdapter extends RecyclerView.Adapter<OrdersActivityAdapter.OrderHolder> {

    private final List<OrderUi> orders;
    private final OnOrderClickListener onOrderClickListener;
    private final Resources resources;
    private Comparator<OrderUi> orderUiComparator = new NullObjectComparator();

    public OrdersActivityAdapter(final List<OrderUi> orders, final OnOrderClickListener onOrderClickListener, final Resources resources) {
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
        this.resources = resources;
    }

    public OrdersActivityAdapter(final OnOrderClickListener onOrderClickListener, final Resources resources) {
        this.resources = resources;
        this.orders = new ArrayList<>(0);
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        final OrderHolder holder = new OrderHolder(inflate);
        holder.itemView.setOnClickListener(view -> this.onOrderClickListener.onClick(this.orders.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderHolder holder, final int position) {
        // TODO: 6/14/2018 binding data
    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }

    public void reorder(final Comparator<OrderUi> orderUiComparator) {
        this.orderUiComparator = orderUiComparator;
        this.orders.sort(orderUiComparator);
        this.notifyDataSetChanged();
    }

    public void refresh(final Collection<OrderUi> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        this.notifyDataSetChanged();
    }

    static class OrderHolder extends RecyclerView.ViewHolder {

        OrderHolder(final View itemView) {
            super(itemView);
        }
    }
}
