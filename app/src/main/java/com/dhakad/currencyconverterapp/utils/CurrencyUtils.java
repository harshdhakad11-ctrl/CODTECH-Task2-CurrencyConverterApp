package com.dhakad.currencyconverterapp.utils;

import com.dhakad.currencyconverterapp.R;

import java.util.HashMap;

public class CurrencyUtils {

    private static final HashMap<String, String> currencyNames = new HashMap<>();
    private static final HashMap<String, Integer> currencyFlags = new HashMap<>();

    static {

        // Currency Names

        currencyNames.put("USD", "US Dollar");
        currencyNames.put("INR", "Indian Rupee");
        currencyNames.put("EUR", "Euro");
        currencyNames.put("GBP", "British Pound");
        currencyNames.put("AED", "UAE Dirham");
        currencyNames.put("JPY", "Japanese Yen");
        currencyNames.put("AUD", "Australian Dollar");
        currencyNames.put("CAD", "Canadian Dollar");
        currencyNames.put("CHF", "Swiss Franc");
        currencyNames.put("CNY", "Chinese Yuan");



        // Flags

        currencyFlags.put("USD", R.drawable.flag_us);
        currencyFlags.put("INR", R.drawable.flag_in);
        currencyFlags.put("EUR", R.drawable.flag_eu);

        currencyFlags.put("AED", R.drawable.flag_ae);
        currencyFlags.put("JPY", R.drawable.flag_jp);
        currencyFlags.put("AUD", R.drawable.flag_au);
        currencyFlags.put("CAD", R.drawable.flag_ca);
        currencyFlags.put("CHF", R.drawable.flag_ch);
        currencyFlags.put("CNY", R.drawable.flag_cn);

    }



    /**
     * Return Currency Name
     */

    public static String getCurrencyName(String code) {

        if (currencyNames.containsKey(code))
            return currencyNames.get(code);

        return code;

    }



    /**
     * Return Flag Drawable
     */

    public static int getCurrencyFlag(String code) {

        if (currencyFlags.containsKey(code))
            return currencyFlags.get(code);

        return R.drawable.flag_default;

    }

}