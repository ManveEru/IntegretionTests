package ru.manveru.integrationaltests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitSumResponse {
    private int totalSum;
    private int oddSum;
    private int maxDigit;

    // Конструктор по умолчанию
    public DigitSumResponse() {}

    // Геттеры и сеттеры
    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public int getOddSum() {
        return oddSum;
    }

    public void setOddSum(int oddSum) {
        this.oddSum = oddSum;
    }

    public int getMaxDigit() {
        return maxDigit;
    }

    public void setMaxDigit(int maxDigit) {
        this.maxDigit = maxDigit;
    }

    @Override
    public String toString() {
        return String.format(
            "DigitSumResponse{totalSum=%d, oddSum=%d, maxDigit=%d}",
            totalSum, oddSum, maxDigit
        );
    }
}