package com.dhakad.currencyconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dhakad.currencyconverterapp.network.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.currencyconverterapp.adapter.CurrencyAdapter;
import com.dhakad.currencyconverterapp.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText etAmount;

    private Button btnFromCurrency;
    private Button btnToCurrency;
    private Button btnConvert;
    private Button btnHistory;

    private ImageButton btnSwap;

    private TextView tvResult;
    private TextView tvRate;
    private TextView tvUpdated;

    private ProgressBar progressBar;

    // Volley Request Queue
    private RequestQueue requestQueue;

    private DatabaseHelper databaseHelper;

    // Currency Rates
    private final HashMap<String, Double> currencyRates = new HashMap<>();

    // Currency Codes
    private final ArrayList<String> currencyList = new ArrayList<>();

    // Default Currency
    private String fromCurrency = "USD";
    private String toCurrency = "INR";
    private boolean isFromSelection = true;

    private ImageView imgFromFlag;
    private ImageView imgToFlag;

    private TextView txtFromCurrencyName;
    private TextView txtToCurrencyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        // Create Volley Queue
        requestQueue = Volley.newRequestQueue(this);

        databaseHelper = new DatabaseHelper(this);

        // Fetch latest exchange rates
        fetchExchangeRates();
        updateCurrencyDetails();

    }

    /**
     * Initialize all UI views
     */
    private void initializeViews() {

        etAmount = findViewById(R.id.etAmount);

        btnFromCurrency = findViewById(R.id.btnFromCurrency);
        btnToCurrency = findViewById(R.id.btnToCurrency);

        imgFromFlag = findViewById(R.id.imgFromFlag);
        imgToFlag = findViewById(R.id.imgToFlag);

        txtFromCurrencyName = findViewById(R.id.txtFromCurrencyName);
        txtToCurrencyName = findViewById(R.id.txtToCurrencyName);

        btnConvert = findViewById(R.id.btnConvert);
        btnHistory = findViewById(R.id.btnHistory);

        btnSwap = findViewById(R.id.btnSwap);

        tvResult = findViewById(R.id.tvResult);
        tvRate = findViewById(R.id.tvRate);
        tvUpdated = findViewById(R.id.tvUpdated);

        progressBar = findViewById(R.id.progressBar);

        btnFromCurrency.setOnClickListener(v -> {

            isFromSelection = true;
            showCurrencyDialog();

        });

        btnToCurrency.setOnClickListener(v -> {

            isFromSelection = false;
            showCurrencyDialog();

        });

        btnSwap.setOnClickListener(v -> swapCurrencies());
        // Convert currency when button is clicked
        btnConvert.setOnClickListener(v -> convertCurrency());

        btnHistory.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

            startActivity(intent);

        });
    }

    /**
     * Fetch latest exchange rates from API
     */
    private void fetchExchangeRates() {

        progressBar.setVisibility(View.VISIBLE);

        com.android.volley.toolbox.JsonObjectRequest request =
                new com.android.volley.toolbox.JsonObjectRequest(

                        com.android.volley.Request.Method.GET,
                        ApiClient.BASE_URL,
                        null,

                        response -> {

                            try {

                                progressBar.setVisibility(View.GONE);

                                String result = response.getString("result");

                                if (!result.equals("success")) {

                                    tvRate.setText("Failed to load exchange rates.");
                                    return;

                                }

                                org.json.JSONObject rates =
                                        response.getJSONObject("rates");

                                java.util.Iterator<String> keys = rates.keys();

                                while (keys.hasNext()) {

                                    String code = keys.next();

                                    double value = rates.getDouble(code);

                                    currencyRates.put(code, value);

                                    currencyList.add(code);

                                }

                                tvUpdated.setText("Exchange rates loaded successfully");

                            } catch (Exception e) {

                                progressBar.setVisibility(View.GONE);

                                tvRate.setText("Parsing Error");

                                e.printStackTrace();

                            }

                        },

                        error -> {

                            progressBar.setVisibility(View.GONE);

                            tvRate.setText("Network Error");

                        });

        requestQueue.add(request);

    }


    /**
     * Open searchable currency dialog
     */
    private void showCurrencyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        android.view.View view = inflater.inflate(R.layout.dialog_currency, null);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        EditText etSearch = view.findViewById(R.id.etSearch);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCurrency);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CurrencyAdapter adapter = new CurrencyAdapter(currencyList, currency -> {

            if (isFromSelection) {

                fromCurrency = currency;

                btnFromCurrency.setText(currency);

            } else {

                toCurrency = currency;

                btnToCurrency.setText(currency);

            }
            updateCurrencyDetails();
            dialog.dismiss();

        });

        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        dialog.show();

    }

    /**
     * Swap source and destination currencies
     */
    private void swapCurrencies() {

        String temp = fromCurrency;

        fromCurrency = toCurrency;

        toCurrency = temp;

        btnFromCurrency.setText(fromCurrency);

        btnToCurrency.setText(toCurrency);

        updateCurrencyDetails();
    }

    /**
     * Convert entered amount using live exchange rates
     */
    private void convertCurrency() {

        // Check if amount is entered
        if (etAmount.getText() == null ||
                etAmount.getText().toString().trim().isEmpty()) {

            etAmount.setError("Enter amount");
            return;
        }

        double amount = Double.parseDouble(etAmount.getText().toString());

        // Check currency data
        if (!currencyRates.containsKey(fromCurrency) ||
                !currencyRates.containsKey(toCurrency)) {

            tvResult.setText("Rates not available");
            return;
        }

        double fromRate = currencyRates.get(fromCurrency);
        double toRate = currencyRates.get(toCurrency);

        // Conversion formula
        double convertedAmount = amount * (toRate / fromRate);

        // Display converted amount
        tvResult.setText(String.format("%.2f", convertedAmount));

        // Display exchange rate
        tvRate.setText(String.format("1 %s = %.4f %s", fromCurrency, (toRate / fromRate), toCurrency));

        String currentDate = new SimpleDateFormat(
                "dd MMM yyyy hh:mm a",
                Locale.getDefault()
        ).format(new Date());

        databaseHelper.insertHistory(
                fromCurrency,
                toCurrency,
                amount,
                convertedAmount,
                currentDate
        );
    }

    /**
     * Update flag and currency name
     */
    private void updateCurrencyDetails() {

        // FROM Currency

        switch (fromCurrency) {

            case "USD":
                imgFromFlag.setImageResource(R.drawable.flag_us);
                txtFromCurrencyName.setText("US Dollar");
                break;

            case "INR":
                imgFromFlag.setImageResource(R.drawable.flag_in);
                txtFromCurrencyName.setText("Indian Rupee");
                break;

            case "EUR":
                imgFromFlag.setImageResource(R.drawable.flag_eu);
                txtFromCurrencyName.setText("Euro");
                break;

            case "GBP":
                imgFromFlag.setImageResource(R.drawable.flag_gb);
                txtFromCurrencyName.setText("British Pound");
                break;

            case "AED":
                imgFromFlag.setImageResource(R.drawable.flag_ae);
                txtFromCurrencyName.setText("UAE Dirham");
                break;

            case "JPY":
                imgFromFlag.setImageResource(R.drawable.flag_jp);
                txtFromCurrencyName.setText("Japanese Yen");
                break;

            case "CAD":
                imgFromFlag.setImageResource(R.drawable.flag_ca);
                txtFromCurrencyName.setText("Canadian Dollar");
                break;

            case "AUD":
                imgFromFlag.setImageResource(R.drawable.flag_au);
                txtFromCurrencyName.setText("Australian Dollar");
                break;

            case "CHF":
                imgFromFlag.setImageResource(R.drawable.flag_ch);
                txtFromCurrencyName.setText("Swiss Franc");
                break;

            case "CNY":
                imgFromFlag.setImageResource(R.drawable.flag_cn);
                txtFromCurrencyName.setText("Chinese Yuan");
                break;

            default:
                imgFromFlag.setImageResource(R.drawable.flag_default);
                txtFromCurrencyName.setText(fromCurrency);

        }


        // TO Currency

        switch (toCurrency) {

            case "USD":
                imgToFlag.setImageResource(R.drawable.flag_us);
                txtToCurrencyName.setText("US Dollar");
                break;

            case "INR":
                imgToFlag.setImageResource(R.drawable.flag_in);
                txtToCurrencyName.setText("Indian Rupee");
                break;

            case "EUR":
                imgToFlag.setImageResource(R.drawable.flag_eu);
                txtToCurrencyName.setText("Euro");
                break;

            case "GBP":
                imgToFlag.setImageResource(R.drawable.flag_gb);
                txtToCurrencyName.setText("British Pound");
                break;

            case "AED":
                imgToFlag.setImageResource(R.drawable.flag_ae);
                txtToCurrencyName.setText("UAE Dirham");
                break;

            case "JPY":
                imgToFlag.setImageResource(R.drawable.flag_jp);
                txtToCurrencyName.setText("Japanese Yen");
                break;

            case "CAD":
                imgToFlag.setImageResource(R.drawable.flag_ca);
                txtToCurrencyName.setText("Canadian Dollar");
                break;

            case "AUD":
                imgToFlag.setImageResource(R.drawable.flag_au);
                txtToCurrencyName.setText("Australian Dollar");
                break;

            case "CHF":
                imgToFlag.setImageResource(R.drawable.flag_ch);
                txtToCurrencyName.setText("Swiss Franc");
                break;

            case "CNY":
                imgToFlag.setImageResource(R.drawable.flag_cn);
                txtToCurrencyName.setText("Chinese Yuan");
                break;

            default:
                imgToFlag.setImageResource(R.drawable.flag_default);
                txtToCurrencyName.setText(toCurrency);

        }

    }
}