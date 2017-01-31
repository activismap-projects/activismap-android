package com.entropy_factory.activismap.ui.adapter.item;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.Company;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by Andersson G. Acosta on 20/01/17.
 */
public class CompanyItem extends ProfileItem<Company> {

    private static final String TAG = "CompanyItem";

    public CompanyItem(Company profile) {
        super(profile);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        Company c = getProfile();
        String detail = c.isCurrentUserCompany() ? INSTANCE.getString(R.string.selected_company) : INSTANCE.getString(c.getRole().getStringResrouce());
        holder.profileView.setDetailText(detail);
    }

    public static List<CompanyItem> parse(List<Company> companies) {
        List<CompanyItem> companyItems = new ArrayList<>();

        for (Company c : companies) {
            companyItems.add(new CompanyItem(c));
        }

        return companyItems;
    }
}
