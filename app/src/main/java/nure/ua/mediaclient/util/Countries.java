package nure.ua.mediaclient.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface Countries {

    static List<String> getAllCountries() {

        List<String> allCountries = new ArrayList<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !allCountries.contains(country)) {
                allCountries.add(country);
            }
            Collections.sort(allCountries);
        }
        return allCountries;
    }

}
