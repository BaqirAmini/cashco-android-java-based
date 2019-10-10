package com.xamuor.cashco.Model;

public class InvoiceDataModal {
    private int invoiceID;
    private String productName;
    private int productQty;
    private double productPrice;
    private double productSubtotal;
public InvoiceDataModal(){

}
    public InvoiceDataModal(int invoiceID, int productQty, String productName, double productPrice,  double productSubtotal) {
        this.invoiceID = invoiceID;
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productSubtotal = productSubtotal;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQty() {
        return productQty;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getProductSubtotal() {
        return productSubtotal;
    }
}
