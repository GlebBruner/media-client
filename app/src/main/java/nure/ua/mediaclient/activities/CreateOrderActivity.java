package nure.ua.mediaclient.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

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
    TextView hashtags;
    Button createButton;
    Button addHashtag;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_create);



        ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getAllCategories());
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);

        ArrayAdapter<Integer> photoVideoAdatper = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getIntegersForPhotoAndVideo());
        photoVideoAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoSpinner.setAdapter(photoVideoAdatper);
        videoSpinner.setAdapter(photoVideoAdatper);

        // dialogs

        Calendar myCalendar = Calendar.getInstance();

        




    }

}
