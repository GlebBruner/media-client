package nure.ua.mediaclient.util.validator;

import android.text.TextWatcher;
import android.widget.TextView;

abstract class TextValidator implements TextWatcher {

    protected final TextView textViewToValidate;


    public TextValidator(TextView textViewToValidate) {
        this.textViewToValidate = textViewToValidate;
    }

    public abstract boolean validate(TextView textView);
}
