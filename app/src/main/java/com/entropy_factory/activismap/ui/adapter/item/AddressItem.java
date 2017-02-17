package com.entropy_factory.activismap.ui.adapter.item;

import android.animation.Animator;
import android.location.Address;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entropy_factory.activismap.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by Andersson G. Acosta on 17/02/17.
 */
public class AddressItem  extends AbstractFlexibleItem<AddressItem.ViewHolder> {

    private static final String TAG = "AddressItem";

    private Address address;

    public AddressItem(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(getLayoutRes(), parent, false);
        return new ViewHolder(v, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < address.getMaxAddressLineIndex()+1; x++) {
            sb.append(address.getAddressLine(x)).append(", ");
        }

        String addressString = sb.toString();
        if (!addressString.isEmpty()) {
            addressString = addressString.substring(0, addressString.length() -2);
        }

        Log.e(TAG, address.toString());
        holder.address.setText(addressString);
    }

    @Override
    public boolean equals(Object o) {
        Address p;
        if (o instanceof Address) {
            p = (Address) o;
            return address.equals(p);
        } else if (o instanceof AddressItem) {
            p = ((AddressItem) o).address;
            return address.equals(p);
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.address_location_row;
    }

    public static class ViewHolder extends FlexibleViewHolder {

        TextView address;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            initialize(view);
        }

        public ViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            initialize(view);
        }

        private void initialize(View itemView){
            address = (TextView) itemView.findViewById(R.id.address);
        }

        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
            if (position % 2 != 0)
                AnimatorHelper.slideInFromRightAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
            else
                AnimatorHelper.slideInFromLeftAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
        }
    }

    public static List<AddressItem> build(Collection<Address> addresses) {
        ArrayList<AddressItem> addressItems = new ArrayList<>();

        for (Address a : addresses) {
            addressItems.add(new AddressItem(a));
        }

        return addressItems;
    }
}
