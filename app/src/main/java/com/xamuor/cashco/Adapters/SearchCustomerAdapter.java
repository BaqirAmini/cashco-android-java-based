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
import android.widget.ImageView;
import android.widget.TextView;

import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.Model.SearchCustomerDataModal;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchCustomerAdapter extends ArrayAdapter<SearchCustomerDataModal> {
    List<SearchCustomerDataModal> customers;
    Context context;
    int myResource;

    public SearchCustomerAdapter(@NonNull Context context, int resource, @NonNull List<SearchCustomerDataModal> customers) {
        super(context, resource, customers);
        this.customers = customers;
        this.context = context;
        this.myResource = resource;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.search_customer_datamodal, null, true);
        }

        SearchCustomerDataModal customer = getItem(position);
//        Initiate widgets of invoice-data-modal
        ImageView imgCustPhoto = convertView.findViewById(R.id.img_customer_photo);
        TextView txtBN = convertView.findViewById(R.id.txt_cust_bn);
        TextView txtPhone = convertView.findViewById(R.id.txt_cust_phone);
        TextView txtFN = convertView.findViewById(R.id.txt_cust_fn);
        TextView txtLN = convertView.findViewById(R.id.txt_cust_ln);
//        define values inside widgets from invoiceDataModal

/*------------------------------- Set values into data modal ---------------------------------*/
        txtBN.setText(Objects.requireNonNull(customer).getBusinessName());
        txtPhone.setText(Objects.requireNonNull(customer).getPhone());
        txtFN.setText(Objects.requireNonNull(customer).getfName());
        txtLN.setText(Objects.requireNonNull(customer).getlName());
/*-------------------------------/. Set values into data modal ---------------------------------*/



        return convertView;
    }

//    Update list of customers while searching customers
    public void onUpdateList(List<SearchCustomerDataModal> customerList) {
        customers = new ArrayList<>();
        customers.addAll(customerList);
//        notifyDataSetChanged();
    }
}
