package com.entropy_factory.activismap.ui.content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.ui.tool.CategorySelectorActivity;
import com.entropy_factory.activismap.util.FileUtils;
import com.entropy_factory.activismap.util.FormUtils;
import com.entropy_factory.activismap.util.IntentUtils;
import com.entropy_factory.activismap.util.TimeUtils;
import com.entropy_factory.activismap.util.Utils;
import com.entropy_factory.activismap.widget.ClassificationView;
import com.entropy_factory.activismap.widget.ItemClassificationView;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.entropy_factory.activismap.ui.tool.CategorySelectorActivity.SELECT_CLASSIFICATION;
import static com.entropy_factory.activismap.ui.tool.CompanyListActivity.PICK_COMPANY;
import static com.entropy_factory.activismap.ui.tool.LoginActivity.LOGIN_USER;
import static com.entropy_factory.activismap.ui.tool.MapsActivity.PICK_LOCATION;
import static com.entropy_factory.activismap.ui.tool.RegisterActivity.REGISTER_USER;
import static com.entropy_factory.activismap.util.IntentUtils.PICK_IMAGE;


/**
 * Created by ander on 30/09/16.
 */
public class EventFormActivity extends AppCompatActivity {

    private static final String TAG = "EventFormActivity";

    private long startMillis;
    private long endMillis;
    private LatLng location;
    private ImageView eventImage;
    private File fileImage;
    private View pick;
    private View pickText;
    private TextView locationView;
    private TextView startDate;
    private TextView endDate;
    private View openSelector;
    private ItemClassificationView classificationView;
    private EditText title;
    private EditText description;
    private Activis activis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activis = new Activis(this);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        eventImage = (ImageView) findViewById(R.id.event_image);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        pick = findViewById(R.id.pick);
        pickText = findViewById(R.id.pick_text);
        locationView = (TextView) findViewById(R.id.location);
        openSelector = findViewById(R.id.open_selector);
        classificationView = (ItemClassificationView) findViewById(R.id.classification);

        View createBtn = findViewById(R.id.create_btn);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        startMillis = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
                        Log.e(TAG, "START DATE = " + dayOfMonth + "/" + monthOfYear + "/" + year + " = " + startMillis);
                        showTimePicker(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                startMillis += (hourOfDay * TimeUtils.HOUR) + (minute * TimeUtils.MINUTE) + (second * TimeUtils.SECOND);
                                Log.e(TAG, "START TIME = " + hourOfDay + ":" + minute + ":" + second + " = " + startMillis);
                                startDate.setText(TimeUtils.getTimeString(startMillis));
                            }
                        });
                    }
                });
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(startMillis, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        endMillis = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
                        Log.e(TAG, "END DATE = " + dayOfMonth + "/" + monthOfYear + "/" + year + " = " + endMillis);
                        showTimePicker(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                endMillis += (hourOfDay * TimeUtils.HOUR) + (minute * TimeUtils.MINUTE) + (second * TimeUtils.SECOND);
                                Log.e(TAG, "END TIME = " + hourOfDay + ":" + minute + ":" + second + " = " + endMillis);
                                endDate.setText(TimeUtils.getTimeString(endMillis));
                            }
                        });
                    }
                });
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openGallery(EventFormActivity.this);
            }
        });

        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openMap(EventFormActivity.this);
            }
        });

        openSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategorySelector();
            }
        });

        classificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategorySelector();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

    }

    private void openCategorySelector() {
        Intent selectorIntent = new Intent(EventFormActivity.this, CategorySelectorActivity.class);
        startActivityForResult(selectorIntent, SELECT_CLASSIFICATION);
    }

    private void send() {
        boolean hasError = false;

        if (FormUtils.isEmpty(title)) {
            hasError = true;
            title.setError(getString(R.string.mandatory_field));
        }

        if (FormUtils.isEmpty(description)) {
            hasError = true;
            description.setError(getString(R.string.mandatory_field));
        }

        if (startMillis == 0) {
            hasError = true;
            startDate.setError(getString(R.string.mandatory_field));
        }

        if (endMillis == 0) {
            hasError = true;
            endDate.setError(getString(R.string.mandatory_field));
        }

        if (location == null) {
            hasError = true;
            locationView.setError(getString(R.string.mandatory_field));
        }

        if (!classificationView.hasType() || !classificationView.hasCategories()) {
            Toast.makeText(this, R.string.indicate_event_classification, Toast.LENGTH_LONG).show();
            hasError = true;
        }

        if (!hasError) {
            processSend();
        }
    }

    private void processSend() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();

        ActivisListener<ActivisEvent> listener = new ActivisListener<ActivisEvent>() {
            @Override
            public void onResponse(ActivisResponse<ActivisEvent> response) {
                finish();
            }
        };

        ActivisCategory[] activisCategories = classificationView.getCategories();
        int length = activisCategories.length;
        String[] categories = new String[length];
        for (int x = 0; x < length; x++) {
            categories[x] = activisCategories[x].toString();
        }


        if (fileImage != null) {
            activis.createEvent(User.getUser(), title, description, startMillis, endMillis, TextUtils.join(",", categories), classificationView.getType(), location, fileImage, listener);

        } else {
            activis.createEvent(User.getUser(), title, description, startMillis, endMillis, TextUtils.join(",", categories), ActivisType.ASSEMBLY, location, listener);
        }
    }

    public void showDatePicker(DatePickerDialog.OnDateSetListener listener) {
        showDatePicker(System.currentTimeMillis(), listener);
    }

    public void showDatePicker(long minDate, DatePickerDialog.OnDateSetListener listener) {
        Calendar now = new GregorianCalendar();
        now.setTime(new Date(minDate));
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void showTimePicker(TimePickerDialog.OnTimeSetListener listener) {
        showTimePicker(System.currentTimeMillis(), listener);
    }

    public void showTimePicker(long minDate, TimePickerDialog.OnTimeSetListener listener) {
        Calendar now = new GregorianCalendar();
        now.setTime(new Date(minDate));
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                listener,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        dpd.setMinTime(now.get(Calendar.HOUR), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE ) {
                if (data == null) {
                    Toast.makeText(this, R.string.open_file_error, Toast.LENGTH_LONG).show();
                    return;
                }
                pickText.setVisibility(View.GONE);
                eventImage.setVisibility(View.VISIBLE);

                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmapImage = Utils.decodeBitmap(this, selectedImage );
                    eventImage.setImageBitmap(bitmapImage);
                    fileImage = FileUtils.file(this, selectedImage);
                } catch (FileNotFoundException | URISyntaxException e ) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.open_file_error, Toast.LENGTH_LONG).show();
                    pickText.setVisibility(View.VISIBLE);
                    eventImage.setVisibility(View.GONE);
                }
            } else if (requestCode == PICK_LOCATION) {
                Bundle extras = data.getExtras();
                location = new LatLng(extras.getDouble("latitude"), extras.getDouble("longitude"));
                Address address = data.getExtras().getParcelable("address");

                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(address.getAddressLine(i)).append(" ");
                }

                Log.e(TAG, "Address: " + strReturnedAddress.toString());
                if (strReturnedAddress.toString().isEmpty()) {
                    locationView.setText(location.latitude + ", " + location.longitude);
                } else {
                    locationView.setText(strReturnedAddress.toString());
                }
            } else if (requestCode == SELECT_CLASSIFICATION) {
                Bundle extras = data.getExtras();

                classificationView.clear();
                boolean showClassification = false;
                if (extras.containsKey("category1")) {
                    ActivisCategory c = (ActivisCategory) extras.getSerializable("category1");
                    classificationView.setCategory(c);
                    showClassification = true;
                }

                if (extras.containsKey("category2")) {
                    ActivisCategory c = (ActivisCategory) extras.getSerializable("category2");
                    classificationView.setCategory(c);
                    showClassification = true;
                }

                if (extras.containsKey("category3")) {
                    ActivisCategory c = (ActivisCategory) extras.getSerializable("category3");
                    classificationView.setCategory(c);
                    showClassification = true;
                }

                if (extras.containsKey("type")) {
                    ActivisType type = (ActivisType) extras.getSerializable("type");
                    classificationView.setType(type);
                    showClassification = true;
                }

                classificationView.setVisibility(showClassification ? View.VISIBLE : View.GONE);
                openSelector.setVisibility(showClassification ? View.GONE : View.VISIBLE);

            } else if (requestCode == LOGIN_USER || requestCode == REGISTER_USER || requestCode == PICK_COMPANY) {
                processSend();
            }
        }

    }
}
