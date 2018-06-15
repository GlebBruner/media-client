package nure.ua.mediaclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.HttpURLConnection;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.model.UserDTO;
import nure.ua.mediaclient.service.RegistrationService;
import nure.ua.mediaclient.util.SpinnerBodies;
import nure.ua.mediaclient.util.HTTP;
import nure.ua.mediaclient.util.validator.EmailValidator;
import nure.ua.mediaclient.util.validator.NameValidator;
import nure.ua.mediaclient.util.validator.PasswordValidator;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText matchingPasswordEdit;
    private EditText nameEdit;
    private EditText surnameEdit;
    private Spinner countrySpinner;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEdit = findViewById(R.id.email_input);
        passwordEdit = findViewById(R.id.password_input);
        matchingPasswordEdit = findViewById(R.id.password_input_matching);
        nameEdit = findViewById(R.id.name_input);
        surnameEdit = findViewById(R.id.surname_input);
        countrySpinner = findViewById(R.id.countries_list_spinner);
        registerButton = findViewById(R.id.register_button);


        ArrayAdapter<String> countrySprinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SpinnerBodies.getAllCountries());
        countrySprinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countrySprinnerAdapter);

        emailEdit.addTextChangedListener(new EmailValidator(emailEdit));
        nameEdit.addTextChangedListener(new NameValidator(nameEdit));
        surnameEdit.addTextChangedListener(new NameValidator(surnameEdit));
        passwordEdit.addTextChangedListener(new PasswordValidator(passwordEdit));
        matchingPasswordEdit.addTextChangedListener(new PasswordValidator(matchingPasswordEdit));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordEdit.getText().toString().equals(matchingPasswordEdit.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(HTTP.URI)
                        .addConverterFactory(JacksonConverterFactory.create());
                Retrofit retrofit = retrofitBuilder
                        .client(httpClient.build())
                        .build();

                RegistrationService registration = retrofit.create(RegistrationService.class);

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(emailEdit.getText().toString());
                userDTO.setPassword(passwordEdit.getText().toString());
                userDTO.setName(nameEdit.getText().toString());
                userDTO.setSurname(surnameEdit.getText().toString());
                userDTO.setCoutry(countrySpinner.getSelectedItem().toString());

                Call<Object> call = registration.registerUser(userDTO);

                call.enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            switch (response.code()) {
                                case HttpURLConnection.HTTP_CONFLICT:
                                    Toast userExistsToast = Toast.makeText(getApplicationContext(),
                                            "User with specified email or password already exists",
                                            Toast.LENGTH_SHORT);
                                    userExistsToast.show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast failOccuredToast = Toast.makeText(getApplicationContext(),
                                t.getMessage(), Toast.LENGTH_LONG);
                        failOccuredToast.show();
                    }

                });
            }
        });
    }
}
