package com.dhakad.currencyconverterapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.currencyconverterapp.R;
import com.dhakad.currencyconverterapp.model.HistoryModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<HistoryModel> list;

    public HistoryAdapter(ArrayList<HistoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HistoryModel item = list.get(position);

        holder.txtCurrency.setText(item.getFromCurrency() + " → " + item.getToCurrency());

        holder.txtAmount.setText("Amount : " + item.getAmount());

        holder.txtResult.setText("Result : " + item.getResult());

        holder.txtDate.setText(item.getDateTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCurrency, txtAmount, txtResult, txtDate;

        ViewHolder(View itemView) {
            super(itemView);

            txtCurrency = itemView.findViewById(R.id.txtCurrency);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtResult = itemView.findViewById(R.id.txtResult);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}