package com.xamuor.cashco.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xamuor.cashco.cashco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDetailFragment extends Fragment implements View.OnClickListener {
    private TextView txtTabInvoices, txtTabPayments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        txtTabInvoices = view.findViewById(R.id.txt_tab_invoices);
        txtTabPayments = view.findViewById(R.id.txt_tab_payments);

        txtTabPayments.setOnClickListener(this);
        txtTabInvoices.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_tab_invoices:
                loadInvoices();
                break;
            case R.id.txt_tab_payments:
                loadPayments();
        }
    }



// Load invoices
    private void loadInvoices() {
        txtTabInvoices.setBackground(getResources().getDrawable(R.drawable.tab_design));
        txtTabPayments.setBackgroundResource(R.color.bg_tabs);
        txtTabInvoices.setTextColor(getResources().getColor(R.color.txt_selected_tab));
        txtTabPayments.setTextColor(getResources().getColor(R.color.text_color));
    }

// Load payments
    private void loadPayments() {
        txtTabPayments.setBackground(getResources().getDrawable(R.drawable.tab_design));
        txtTabInvoices.setBackgroundResource(R.color.bg_tabs);
        txtTabPayments.setTextColor(getResources().getColor(R.color.txt_selected_tab));
        txtTabInvoices.setTextColor(getResources().getColor(R.color.text_color));
    }
}


