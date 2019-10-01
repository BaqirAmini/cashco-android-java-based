package com.xamuor.cashco;

public  class Invoice {
    private static double subTotal, tax, total;

    public static double getSubTotal() {
        return subTotal;
    }

    public static void setSubTotal(double subTotal) {
        Invoice.subTotal = subTotal;
    }

    public static double getTax() {
        return tax;
    }

    public static void setTax(double tax) {
        Invoice.tax = tax;
    }

    public static double getTotal() {
        return total;
    }

    public static void setTotal(double total) {
        Invoice.total = total;
    }
}
