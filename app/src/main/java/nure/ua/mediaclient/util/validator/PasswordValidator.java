package nure.ua.mediaclient.util.validator;

import android.text.Editable;
import android.widget.TextView;

import java.util.regex.Pattern;

public class PasswordValidator extends TextValidator {

    //todo at least 1 up ...
    private final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"; //(?=.*[@#$%^&+=])
    private Pattern pattern;


    public PasswordValidator(TextView textViewToValidate) {
        super(textViewToValidate);
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean validate(TextView textView) {
        return pattern.matcher(textView.getText().toString()).matches();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!validate(textViewToValidate)) {
            textViewToValidate.setError("Password must contain 1 A-Z, 1 a-z, 1+ digit, special characters");
        }
    }
}
