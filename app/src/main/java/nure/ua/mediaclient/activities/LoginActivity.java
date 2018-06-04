package nure.ua.mediaclient.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import nure.ua.mediaclient.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private EditText emailEdit;
    private EditText passwordEdit;
    private View progressView;
    private View loginFormView;
    private TextView registerClickableTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        progressView = (View) findViewById(R.id.login_progress);
        loginFormView = (View) findViewById(R.id.login_form);
        registerClickableTextView = findViewById(R.id.textView_register_clickable);

        registerClickableTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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