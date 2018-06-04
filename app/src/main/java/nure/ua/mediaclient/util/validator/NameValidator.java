package nure.ua.mediaclient.util.validator;

import android.text.Editable;
import android.widget.TextView;

import java.util.regex.Pattern;

public class NameValidator extends TextValidator {

    private final static String NAME_PATTERN = "[A-Z][a-zA-Z]*";
    private Pattern pattern;

    public NameValidator(TextView textViewToValidate) {
        super(textViewToValidate);
        pattern = Pattern.compile(NAME_PATTERN);
    }

    @Override
    public boolean validate(TextView textView) {
        return pattern.matcher(textView.getText().toString()).matches();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        /*not now*/
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*not now*/
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!validate(textViewToValidate)) {
            textViewToValidate.setError("Name must start with UPPER case symbol. Without digits and special characters");
        }
    }
}
