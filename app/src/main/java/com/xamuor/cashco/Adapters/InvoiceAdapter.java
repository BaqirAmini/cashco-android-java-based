package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.Objects;

public class InvoiceAdapter extends ArrayAdapter<InvoiceDataModal> {
    ArrayList<InvoiceDataModal> products;
    Context context;
    int myResource;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InvoiceDataModal> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.myResource = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invoice_datamodal_layout, null, true);
        }

        InvoiceDataModal product = getItem(position);
//        Initiate widgets of invoice-data-modal
        TextView txtItemName = convertView.findViewById(R.id.txt_item_name);
        TextView txtItemQty = convertView.findViewById(R.id.txt_item_qty);
        TextView txtItemPrice = convertView.findViewById(R.id.txt_item_price);
        TextView txtItemSubtotal = convertView.findViewById(R.id.txt_item_subtotal);

//        Widgets out of invoice data modal (fragment_invoice.xml)
        TextView txtSubtotal = ((Activity)context).findViewById(R.id.txt_val_subtotal);
        TextView txtTax = ((Activity)context).findViewById(R.id.txt_val_tax);
        TextView txtTotal = ((Activity)context).findViewById(R.id.txt_val_total);
        Button btnPayInvoice = ((Activity)context).findViewById(R.id.btn_pay_invoice);

//        define values inside widgets from invoiceDataModal
        if (product != null) {
            txtItemQty.setText(product.getProductQty()+"");
        }
        if (product != null) {
            txtItemName.setText(product.getProductName());
        }
        if (product != null) {
            txtItemPrice.setText(product.getProductPrice() + "");
        }
        if (product != null) {
            txtItemSubtotal.setText(product.getProductSubtotal() + "");
        }
//        txtSubtotal.setText("$"+ Objects.requireNonNull(product).getProductSubtotal());
       /* txtTax.setText("$" + 10);
        double tax = 10 * product.getProductSubtotal() / 100;
        txtTotal.setText("$" + (product.getProductSubtotal() + tax));*/
        return convertView;
    }
}
