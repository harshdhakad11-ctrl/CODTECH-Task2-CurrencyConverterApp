package com.dhakad.currencyconverterapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.currencyconverterapp.adapter.HistoryAdapter;
import com.dhakad.currencyconverterapp.database.DatabaseHelper;
import com.dhakad.currencyconverterapp.model.HistoryModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialButton btnClear;

    private DatabaseHelper databaseHelper;
    private HistoryAdapter adapter;

    private ArrayList<HistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);
        btnClear = findViewById(R.id.btnClearHistory);

        databaseHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();

        btnClear.setOnClickListener(v -> {

            databaseHelper.clearHistory();

            loadHistory();

        });

    }

    private void loadHistory() {

        historyList = databaseHelper.getAllHistory();

        adapter = new HistoryAdapter(historyList);

        recyclerView.setAdapter(adapter);

    }

}