package com.entropy_factory.activismap.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.ui.content.EventCalendarFragment;
import com.entropy_factory.activismap.ui.content.EventFormActivity;
import com.entropy_factory.activismap.ui.content.EventMapFragment;
import com.entropy_factory.activismap.ui.content.EventStreetMapFragment;
import com.entropy_factory.activismap.ui.tool.LoginActivity;
import com.entropy_factory.activismap.ui.tool.RegisterActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static com.entropy_factory.activismap.core.receiver.ActivisEventReceiver.ACTION_CHANGE_CATEGORY;
import static com.entropy_factory.activismap.ui.tool.LoginActivity.LOGIN_USER;
import static com.entropy_factory.activismap.ui.tool.RegisterActivity.REGISTER_USER;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static HomeActivity instance;

    private static final int MAP = 1;
    private static final int MAP2 = 5;
    private static final int CALENDAR = 2;
    private static final int EVENT = 3;
    private static final int REGISTER = 4;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Drawer drawer;
    private ActionBarDrawerToggle toggle;
    private AccountHeader accountHeader;
    private FragmentManager fm;
    private boolean userNotPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int defaultSelection = getIntent().getIntExtra("selection", MAP);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fm = getSupportFragmentManager();

        final PrimaryDrawerItem map = new PrimaryDrawerItem().withIdentifier(MAP).withName(R.string.event_radar).withIcon(R.drawable.ic_menu_radar).withIconTintingEnabled(true);
        final PrimaryDrawerItem map2 = new PrimaryDrawerItem().withIdentifier(MAP2).withName("Radar 2").withIcon(R.drawable.ic_menu_radar).withIconTintingEnabled(true);
        final PrimaryDrawerItem calendar = new PrimaryDrawerItem().withIdentifier(CALENDAR).withName(R.string.my_events).withIcon(R.drawable.ic_menu_calendar).withIconTintingEnabled(true);
        final PrimaryDrawerItem event = new PrimaryDrawerItem().withIdentifier(EVENT).withName(R.string.add_event).withIcon(R.drawable.ic_menu_add_event).withIconTintingEnabled(true);
        final PrimaryDrawerItem register = new PrimaryDrawerItem().withIdentifier(REGISTER).withName(R.string.login).withIcon(R.drawable.ic_menu_register).withIconTintingEnabled(true);

        SectionDrawerItem categories = new SectionDrawerItem().withDivider(true).withName(R.string.categories);
        SectionDrawerItem tools = new SectionDrawerItem().withDivider(true).withName(R.string.tools);
        ActivisCategory[] activisCategories = ActivisCategory.values();

        IDrawerItem[] items = new IDrawerItem[activisCategories.length + 6];
        items[0] = map;
        items[1] = categories;

        for (int x = 0; x < activisCategories.length; x++) {
            ActivisCategory ac = activisCategories[x];
            String name = getResources().getStringArray(ac.getCategoryResource())[0];
            items[x+2] = new PrimaryDrawerItem().withIdentifier(ac.getIcon()).withName(name).withIcon(ac.getIcon());
        }

        items[activisCategories.length +2] = tools;
        items[activisCategories.length +3] = calendar;
        items[activisCategories.length +4] = event;
        items[activisCategories.length +5] = register;

        DrawerBuilder db = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(items)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int id = (int) drawerItem.getIdentifier();
                        performOnClick(id);
                        drawer.closeDrawer();
                        return true;
                    }
                });

        AccountHeaderBuilder ahb = new AccountHeaderBuilder();

        ahb.withActivity(this)
                .withHeaderBackground(R.drawable.activismo);

        User u = User.getUser();
        if (User.getUser() != null) {
            ProfileDrawerItem pdi = new ProfileDrawerItem().withName(u.getFriendlyName()).withEmail(u.getEmail());

            Log.d(TAG, "User has avatar: " + u.hasAvatar());
            if (u.hasAvatar()) {
                pdi.withIcon(u.getRemoteAvatar());
            }

            ahb.addProfiles(pdi);
        } else {
            userNotPresent = true;
        }

        accountHeader = ahb.build();

        db.withAccountHeader(accountHeader);

        drawer = db.build();
        DrawerLayout drawerLayout = drawer.getDrawerLayout();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.material_drawer_open, R.string.material_drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

        drawer.setSelection(defaultSelection);
    }

    private void performOnClick(int id) {
        Fragment f;
        ActivisCategory ac = null;
        boolean sendCategoryAction = false;
        switch (id) {
            case MAP:
                f = new EventMapFragment();
                fm.beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case CALENDAR:
                f = new EventCalendarFragment();
                fm.beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case MAP2:
                f = new EventStreetMapFragment();
                fm.beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case EVENT:
                Intent eventIntent = new Intent(this, EventFormActivity.class);
                startActivity(eventIntent);
                break;
            case R.drawable.ic_amnesty:
                sendCategoryAction = true;
                ac = ActivisCategory.AMNESTY;
                break;
            case R.drawable.ic_animal_mov:
                sendCategoryAction = true;
                ac = ActivisCategory.ANIMAL_MOV;
                break;
            case R.drawable.ic_anticapitalism:
                sendCategoryAction = true;
                ac = ActivisCategory.ANTICAPITALISM;
                break;
            case R.drawable.ic_antimilitarism:
                sendCategoryAction = true;
                ac = ActivisCategory.ANTIMILITARISM;
                break;
            case R.drawable.ic_asylum:
                sendCategoryAction = true;
                ac = ActivisCategory.ASYLUM;
                break;
            case R.drawable.ic_ecology:
                sendCategoryAction = true;
                ac = ActivisCategory.ECOLOGY;
                break;
            case R.drawable.ic_education:
                sendCategoryAction = true;
                ac = ActivisCategory.EDUCATION;
                break;
            case R.drawable.ic_fair_trade:
                sendCategoryAction = true;
                ac = ActivisCategory.FAIR_TRADE;
                break;
            case R.drawable.ic_feminism:
                sendCategoryAction = true;
                ac = ActivisCategory.FEMINISM;
                break;
            case R.drawable.ic_health:
                sendCategoryAction = true;
                ac = ActivisCategory.HEALTH;
                break;
            case R.drawable.ic_integration:
                sendCategoryAction = true;
                ac = ActivisCategory.INTEGRATION;
                break;
            case R.drawable.ic_internet:
                sendCategoryAction = true;
                ac = ActivisCategory.INTEGRATION;
                break;
            case R.drawable.ic_memory:
                sendCategoryAction = true;
                ac = ActivisCategory.MEMORY;
                break;
            case R.drawable.ic_policy:
                sendCategoryAction = true;
                ac = ActivisCategory.POLICY;
                break;
            case R.drawable.ic_proindependence:
                sendCategoryAction = true;
                ac = ActivisCategory.PRO_INDEPENDENCE;
                break;
            case R.drawable.ic_solidarity:
                sendCategoryAction = true;
                ac = ActivisCategory.SOLIDARITY;
                break;
            case R.drawable.ic_territory_defense:
                sendCategoryAction = true;
                ac = ActivisCategory.TERRITORY_DEFENSE;
                break;
            case R.drawable.ic_treated:
                sendCategoryAction = true;
                ac = ActivisCategory.TREATED;
                break;
            case REGISTER:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
        }

        if (sendCategoryAction) {
            Intent categoryIntent = new Intent(ACTION_CHANGE_CATEGORY);
            categoryIntent.putExtra("category", ac);
            sendBroadcast(categoryIntent);
        }
    }

    public static HomeActivity instance() {
        return instance;
    }

    public void openRegisterScreen() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, REGISTER_USER);
    }

    public void openLoginScreen() {
        Intent registerIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(registerIntent, LOGIN_USER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle != null && toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        User u = User.getUser();
        if (userNotPresent && u != null) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.putExtra("selection", (int) drawer.getCurrentSelection());
            startActivity(homeIntent);

            finish();
        }
    }
}
