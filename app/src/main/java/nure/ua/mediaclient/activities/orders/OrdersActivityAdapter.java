package nure.ua.mediaclient.activities.orders;

import android.app.Fragment;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.activities.orders.comparator.NullObjectComparator;
import nure.ua.mediaclient.model.ui.OrderUi;

public class OrdersActivityAdapter extends RecyclerView.Adapter<OrdersActivityAdapter.OrderHolder> {

    private final List<OrderUi> orders;
    private final OnOrderClickListener onOrderClickListener;
    private final Resources resources;
    private Comparator<OrderUi> orderUiComparator = new NullObjectComparator();

    private Fragment fragment;

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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order, parent, false);
        final OrderHolder holder = new OrderHolder(inflate);
        holder.itemView.setOnClickListener(view -> this.onOrderClickListener.onClick(this.orders.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderHolder holder, final int position) {
        holder.fullname.setText(orders.get(position).getFullName());
        holder.title.setText(orders.get(position).getTitle());
        holder.description.setText(orders.get(position).getDescription());

        Calendar calendarCreationDate = Calendar.getInstance();
        calendarCreationDate.setTime(orders.get(position).getCreationDate());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(calendarCreationDate.get(Calendar.DAY_OF_MONTH))
                .append("-")
                .append(calendarCreationDate.get(Calendar.MONTH))
                .append("-")
                .append(calendarCreationDate.get(Calendar.YEAR));
        holder.creationDate.setText(stringBuilder.toString());

        Date date = orders.get(position).getDeadline();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dateDeadLineYear = String.valueOf(calendar.get(Calendar.YEAR));
        String dateDeadLineMonth = String.valueOf(calendar.get(Calendar.MONTH));
        String dateDeadLineDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String timeDeadLineHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String timeDeadLineMinute = String.valueOf(calendar.get(Calendar.MINUTE));


        holder.dateDL.setText(dateDeadLineDay + "-" + dateDeadLineMonth + "-" + dateDeadLineYear);
        holder.timeDL.setText(timeDeadLineHour + ":" + timeDeadLineMinute);


        holder.phoroCount.setText(String.valueOf(orders.get(position).getPhotoCount()));
        holder.videoCount.setText(String.valueOf(orders.get(position).getVideoCount()));
        holder.rating.setText(String.valueOf(orders.get(position).getLikes() - orders.get(position).getDislikes()));

        holder.money.setText(String.valueOf(orders.get(position).getMoney()));

        if (orders.get(position).getMoney() != 0.0f) {
            holder.money.setText(String.valueOf(orders.get(position).getMoney()));
            holder.nomoneyImg.setVisibility(View.INVISIBLE);
        } else {
            holder.money.setText("");
            holder.moneyImg.setVisibility(View.INVISIBLE);
        }


        StringBuilder hashtags = new StringBuilder();
        StringBuilder category = new StringBuilder();
        for (String hashtag : orders.get(position).getHashtags()) {
            hashtags.append(" ").append(hashtag);
        }

        for (String categoryy: orders.get(position).getCategories()) {
            category.append(categoryy);
        }

        holder.hashtags.setText(hashtags.toString());
        holder.category.setText(category.toString());
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

        CardView cv;
        TextView fullname;
        TextView category;
        TextView title;
        TextView description;
        TextView dateDL;
        TextView timeDL;
        TextView phoroCount;
        TextView videoCount;
        TextView money;
        TextView hashtags;
        TextView rating;
        TextView comments;
        TextView creationDate;

        ImageView lockedImg;
        ImageView unlockedImg;
        ImageView moneyImg;
        ImageView nomoneyImg;

        OrderHolder(final View itemView) {

            super(itemView);

            cv = itemView.findViewById(R.id.single_item);
            fullname = itemView.findViewById(R.id.user_name_so);
            category = itemView.findViewById(R.id.category_so);
            title = itemView.findViewById(R.id.title_so);
            description = itemView.findViewById(R.id.descr_so);
            dateDL = itemView.findViewById(R.id.datedl_so);
            timeDL = itemView.findViewById(R.id.timedl_so);
            phoroCount = itemView.findViewById(R.id.photocount_so);
            videoCount = itemView.findViewById(R.id.vidcount_so);
            money = itemView.findViewById(R.id.moneycount_so);
            hashtags = itemView.findViewById(R.id.hashtags_so);
            rating = itemView.findViewById(R.id.rating_so2);
            comments = itemView.findViewById(R.id.comments_so);
            creationDate = itemView.findViewById(R.id.creation_date_so);

            lockedImg = itemView.findViewById(R.id.locked_so);
            unlockedImg = itemView.findViewById(R.id.unlockedimg_so);

            moneyImg = itemView.findViewById(R.id.moneyimg_so);
            nomoneyImg = itemView.findViewById(R.id.nomoneyImg_so);
        }
    }
}
