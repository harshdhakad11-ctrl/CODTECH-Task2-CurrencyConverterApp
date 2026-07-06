package com.dhakad.currencyconverterapp.model;

public class HistoryModel {

    private int id;
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double result;
    private String dateTime;

    public HistoryModel(int id,
                        String fromCurrency,
                        String toCurrency,
                        double amount,
                        double result,
                        String dateTime) {

        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.result = result;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public double getResult() {
        return result;
    }

    public String getDateTime() {
        return dateTime;
    }
}