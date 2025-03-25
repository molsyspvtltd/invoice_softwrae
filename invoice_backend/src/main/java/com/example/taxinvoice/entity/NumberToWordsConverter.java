package com.example.taxinvoice.entity;

public class NumberToWordsConverter {

    private static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };

    private static final String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

    public static String convert(int number) {
        if (number == 0) return "Zero";
        if (number < 20) return units[number];
        if (number < 100) return tens[number / 10] + ((number % 10 != 0) ? " " + units[number % 10] : "");
        if (number < 1000) return units[number / 100] + " Hundred" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
        if (number < 100000) return convert(number / 1000) + " Thousand" + ((number % 1000 != 0) ? " " + convert(number % 1000) : "");
        return convert(number / 100000) + " Lakh" + ((number % 100000 != 0) ? " " + convert(number % 100000) : "");
    }
}
