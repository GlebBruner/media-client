package nure.ua.mediaclient.activities.orders.comparator;

import java.util.Comparator;

import nure.ua.mediaclient.model.ui.OrderUi;

public class NullObjectComparator implements Comparator<OrderUi> {
    @Override
    public int compare(final OrderUi o1, final OrderUi o2) {
        return 0;
    }
}
