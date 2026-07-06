package com.dhakad.currencyconverterapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.currencyconverterapp.R;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> implements Filterable {

    public interface OnCurrencyClickListener {
        void onCurrencyClick(String currency);
    }

    private final ArrayList<String> currencyList;
    private final ArrayList<String> filteredList;
    private final OnCurrencyClickListener listener;

    public CurrencyAdapter(ArrayList<String> list, OnCurrencyClickListener listener) {

        this.currencyList = new ArrayList<>(list);
        this.filteredList = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String currency = filteredList.get(position);

        holder.txtCurrency.setText(currency);

        holder.itemView.setOnClickListener(v ->
                listener.onCurrencyClick(currency));

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCurrency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCurrency = itemView.findViewById(R.id.txtCurrency);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                filteredList.clear();

                if (constraint == null || constraint.length() == 0) {

                    filteredList.addAll(currencyList);

                } else {

                    String search = constraint.toString().toLowerCase();

                    for (String item : currencyList) {

                        if (item.toLowerCase().contains(search)) {

                            filteredList.add(item);

                        }

                    }

                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                notifyDataSetChanged();

            }
        };
    }
}