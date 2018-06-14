package nure.ua.mediaclient.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nure.ua.mediaclient.R;
import nure.ua.mediaclient.model.JwtAuthenticationResponse;
import nure.ua.mediaclient.model.UserDTO;
import nure.ua.mediaclient.service.LoginService;
import nure.ua.mediaclient.util.HTTP;
import nure.ua.mediaclient.util.SharedProperties;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private EditText emailEdit;
    private EditText passwordEdit;
    private View progressView;
    private View loginFormView;
    private TextView registerClickableTextView;
    private Button loginButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        progressView = (View) findViewById(R.id.login_progress);
        loginFormView = (View) findViewById(R.id.login_form);
        registerClickableTextView = findViewById(R.id.textView_register_clickable);
        loginButton = findViewById(R.id.login_button);


        registerClickableTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(HTTP.URI)
                        .addConverterFactory(JacksonConverterFactory.create());
                Retrofit retrofit = retrofitBuilder
                        .client(httpClient.build())
                        .build();

                LoginService login = retrofit.create(LoginService.class);

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(emailEdit.getText().toString());
                userDTO.setPassword(passwordEdit.getText().toString());


                Call<JwtAuthenticationResponse> call = login.loginUser(userDTO);

                call.enqueue(new Callback<JwtAuthenticationResponse>() {
                    @Override
                    public void onResponse(Call<JwtAuthenticationResponse> call, Response<JwtAuthenticationResponse> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getAccessToken();
                            SharedPreferences preferences = getSharedPreferences(SharedProperties.APP_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor preferencesEditor = preferences.edit();
                            preferencesEditor.putString(SharedProperties.TOKEN, token);
                            preferencesEditor.apply();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    preferences.getString(SharedProperties.TOKEN, "Ooops, nothing is there"), Toast.LENGTH_LONG);
                            toast.show();

                            startActivity(new Intent(LoginActivity.this, FeedActivity.class));

                        } else {


                        }
                    }

                    @Override
                    public void onFailure(Call<JwtAuthenticationResponse> call, Throwable t) {
                        Toast failOccuredToast = Toast.makeText(getApplicationContext(),
                                t.getMessage(), Toast.LENGTH_LONG);
                        failOccuredToast.show();
                    }
                });
            }

        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}