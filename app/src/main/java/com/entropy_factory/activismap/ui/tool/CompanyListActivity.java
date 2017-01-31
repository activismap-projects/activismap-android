package com.entropy_factory.activismap.ui.tool;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.Company;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.ui.adapter.CommentItem;
import com.entropy_factory.activismap.ui.adapter.CompanyAdapter;
import com.entropy_factory.activismap.ui.adapter.item.CompanyItem;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class CompanyListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final int PICK_COMPANY = 25;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CompanyAdapter adapter;
    private Activis client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        client = new Activis(this);
        onRefresh();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        invalidateAdapter();

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);


    }

    private void invalidateAdapter() {
        adapter = new CompanyAdapter(CompanyItem.parse(Company.getGrantedCompanies()), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int i) {
                CompanyItem ci = adapter.getItem(i);
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        client.getAccount(new ActivisListener<User>() {
            @Override
            public void onResponse(ActivisResponse<User> response) {
                refreshLayout.setRefreshing(false);
                invalidateAdapter();
            }
        });
    }
}
