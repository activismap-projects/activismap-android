package com.entropy_factory.activismap.ui.tool;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.ui.base.AbstractOptionListener;
import com.entropy_factory.activismap.widget.CategoryView;
import com.entropy_factory.activismap.widget.ClassificationView;
import com.entropy_factory.activismap.widget.ItemClassificationView;
import com.entropy_factory.activismap.widget.OptionsView;
import com.entropy_factory.activismap.widget.TypeClassificationView;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class CategorySelectorActivity extends AppCompatActivity {

    public static final int SELECT_CLASSIFICATION = 9855;

    private ItemClassificationView itemClassificationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selector);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemClassificationView = (ItemClassificationView) findViewById(R.id.item_classification);
        itemClassificationView.clear();

        final CategoryView categoryView = (CategoryView) findViewById(R.id.category_selector);
        TypeClassificationView typeClassificationView = (TypeClassificationView) findViewById(R.id.type_selector);

        categoryView.setOnOptionClickListener(new AbstractOptionListener<CategoryView.Option<ActivisCategory>>() {
            @Override
            public void onOptionClick(View v, CategoryView.Option<ActivisCategory> option) {

                if (!itemClassificationView.containsCategory(option.object)) {
                    itemClassificationView.setCategory(option.object);
                    v.setPressed(true);
                } else {
                    itemClassificationView.removeCategory(option.object);
                    v.setPressed(false);
                }

                super.onOptionClick(v, option);
            }

            @Override
            public boolean onOptionLongClick(View v, CategoryView.Option<ActivisCategory> option) {
                openTypeTooltip(v, TextUtils.join("\n", getResources().getStringArray(option.object.getCategoryResource())));
                return  true;
            }
        });

        typeClassificationView.setOnClassificationClickListener(new ClassificationView.OnClassificationClickListener<ActivisType>() {
            @Override
            public void onClassificationClick(View v, ActivisType item) {
                itemClassificationView.setType(item);
            }

            @Override
            public boolean onClassificationLongClick(View v, ActivisType item) {
                openTypeTooltip(v, TextUtils.join("\n", getResources().getStringArray(item.getTypeResource())));
                return true;
            }
        });
    }

    private void openTypeTooltip(View v, String text) {
        SimpleTooltip st = new SimpleTooltip.Builder(this)
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(getResources().getColor(R.color.colorSecondaryHint))
                .animated(true)
                .anchorView(v)
                .modal(true)
                .arrowColor(getResources().getColor(R.color.colorSecondaryHint))
                .build();
        st.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_category_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_done:
                Intent data = new Intent();
                ActivisCategory[] categories = itemClassificationView.getCategories();

                for (int x = 0; x < categories.length; x++) {
                    data.putExtra("category" + (x+1), categories[x]);
                }

                if (itemClassificationView.getType() != null) {
                    data.putExtra("type", itemClassificationView.getType());
                }

                setResult(RESULT_OK, data);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
