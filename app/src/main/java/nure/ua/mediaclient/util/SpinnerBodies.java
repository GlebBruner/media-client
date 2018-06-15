package nure.ua.mediaclient.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface SpinnerBodies {

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

    static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("City");
        categories.add("Nature");
        categories.add("Events");
        categories.add("Disasters");
        return categories;
    }

    static List<Integer> getIntegersForPhotoAndVideo() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        integerList.add(4);
        integerList.add(5);
        return integerList;
    }

}
