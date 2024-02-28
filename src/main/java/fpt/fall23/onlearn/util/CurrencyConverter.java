package fpt.fall23.onlearn.util;

import java.text.DecimalFormat;

public class CurrencyConverter {
    // Exchange rate from VND to USD
    private static final double USD_EXCHANGE_RATE = 0.000043;

    private static final double VND_EXCHANGE_RATE = 23255.81395348837;

    // Function to convert VND to USD
    public static double convertVNDtoUSD(double vndAmount) {
        Double usd = vndAmount * USD_EXCHANGE_RATE;
        DecimalFormat df = new DecimalFormat("#.##");
        double roundedNumber = Double.parseDouble(df.format(usd));
        return roundedNumber;
    }

    // Function to convert USD to VND
    public static double convertUSDtoVND(double usdAmount) {
        Double vnd = usdAmount * VND_EXCHANGE_RATE;
        DecimalFormat df = new DecimalFormat("#.##");
        double roundedNumber = Double.parseDouble(df.format(vnd));
        return roundedNumber;
    }

    public static void main(String[] args) {
        double amountInVND = 500000; // Change this value to the amount you want to convert
        double amountInUSD = convertVNDtoUSD(amountInVND);
        System.out.println(amountInVND + " VND is equal to " + amountInUSD + " USD");

        double vndConverted = convertUSDtoVND(amountInUSD);
        System.out.println(amountInUSD + "USD is equal to " + vndConverted + " VND");
    }
}
