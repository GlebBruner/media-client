package nure.ua.mediaclient.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.util.SpinnerBodies;

public class CreateOrderActivity extends AppCompatActivity {

    Spinner categorySpinner;
    Spinner videoSpinner;
    Spinner photoSpinner;
    CheckBox isPrivateCheckBox;
    EditText dateEdit;
    EditText timeEdit;
    EditText title;
    EditText money;
    EditText descr;
    EditText fieldForAddHashtag;
    Button createButton;
    Button addHashtag;
    Button deleteHashtag;
    List<String> hashtagsList;
    TextView allHashtagsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_create);

        isPrivateCheckBox = findViewById(R.id.oc_private);
        dateEdit = findViewById(R.id.oc_dl_date);
        timeEdit = findViewById(R.id.oc_dl_time);
        title = findViewById(R.id.oc_title);
        money = findViewById(R.id.oc_money);
        descr = findViewById(R.id.oc_descr);
        fieldForAddHashtag = findViewById(R.id.oc_hashtags);
        createButton = findViewById(R.id.oc_button_create);
        addHashtag = findViewById(R.id.action_add_hashtag);
        categorySpinner = findViewById(R.id.oc_spinner_cat);
        photoSpinner = findViewById(R.id.oc_spinner_ph);
        videoSpinner = findViewById(R.id.oc_spinner_vid);
        allHashtagsView = findViewById(R.id.text_view_all_hashtags);
        deleteHashtag = findViewById(R.id.action_delete_hashtag);


        hashtagsList = new ArrayList<>();

        dateEdit.setFocusable(false);
        dateEdit.setClickable(true);

        timeEdit.setFocusable(false);
        timeEdit.setClickable(true);


        ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getAllCategories());
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);

        ArrayAdapter<Integer> photoVideoAdatper = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getIntegersForPhotoAndVideo());
        photoVideoAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoSpinner.setAdapter(photoVideoAdatper);
        videoSpinner.setAdapter(photoVideoAdatper);

        // dialogs

        Date deadline = new Date();
        final Calendar calendar = Calendar.getInstance();

        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        dateEdit.setText(i + " " + i1 + " " + i2);
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrderActivity.this, dateSetListener , year, month, day);
                datePickerDialog.show();
            }


        });

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int min = myCalendar.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY , i);
                        calendar.set(Calendar.MINUTE , i1);
                        timeEdit.setText(i + " : " + i1);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateOrderActivity.this, timeSetListener, hour, min, true);
                timePickerDialog.show();
            }

        });

        deadline = calendar.getTime();

        addHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashtagsList.add(fieldForAddHashtag.getText().toString());
                fieldForAddHashtag.setText("");
                allHashtagsView.append(hashtagsList.get(hashtagsList.size() - 1) + " ");
            }
        });

        deleteHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashtagsList.remove(hashtagsList.size() - 1);
                allHashtagsView.setText("");
                for (String hashtag : hashtagsList) {
                    allHashtagsView.append(hashtag);
                }
            }
        });


    }

}
