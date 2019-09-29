package com.xamuor.cashco.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xamuor.cashco.cashco.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    private Button btnCash, btnCreditCard, btnDebitCard, btnCheck, btnDone;
    private TextView txtAmountRcv, txtChangeDue, txtBalanceDue, txtTransactionCode;
    private EditText editTotalAmountDue, editAmountRcv, editChangeDue, editBalanceDue, editTransCode;
    private Spinner spnPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

    /* ---------------------------------- Initialize Widgets -----------------------------*/
        btnCash = view.findViewById(R.id.btn_cash);
        btnCreditCard = view.findViewById(R.id.btn_credit_card);
        btnDebitCard = view.findViewById(R.id.btn_debit_card);
        btnCheck = view.findViewById(R.id.btn_check);
        btnDone = view.findViewById(R.id.btn_done);

        txtAmountRcv = view.findViewById(R.id.txt_lbl_amount_recieved);
        txtChangeDue = view.findViewById(R.id.txt_lbl_change_due);
        txtBalanceDue = view.findViewById(R.id.txt_lbl_balance_due);
        txtTransactionCode = view.findViewById(R.id.txt_lbl_transaction_code);

        editTotalAmountDue = view.findViewById(R.id.edit_total_amount_due);
        editAmountRcv = view.findViewById(R.id.edit_amount_recieved);
        editChangeDue = view.findViewById(R.id.edit_change_due);
        editBalanceDue = view.findViewById(R.id.edit_balance_due);
        editTransCode = view.findViewById(R.id.edit_transaction_code);

        spnPayment = view.findViewById(R.id.spn_payment_due);

//        Interface onClickListener is used so add these buttons
        btnCash.setOnClickListener(this);
        btnCreditCard.setOnClickListener(this);
        btnDebitCard.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnDone.setOnClickListener(this);
    /* ----------------------------------/. Initialize Widgets -----------------------------*/
/* ------------------------ Choose PAYMENT-METHODS --------------------------*/

    /* -------------------------------- Select Payment i.e. Partial or Full -----------------------------*/
        spnPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    txtAmountRcv.setVisibility(View.VISIBLE);
                    editAmountRcv.setVisibility(View.VISIBLE);
                    txtChangeDue.setVisibility(View.VISIBLE);
                    editChangeDue.setVisibility(View.VISIBLE);
                    txtBalanceDue.setVisibility(View.VISIBLE);
                    editBalanceDue.setVisibility(View.VISIBLE);
                } else {
//                    ChangeDue not longer required
                    txtChangeDue.setVisibility(View.INVISIBLE);
                    editChangeDue.setVisibility(View.INVISIBLE);

                    txtAmountRcv.setVisibility(View.VISIBLE);
                    editAmountRcv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/* ------------------------/. Choose PAYMENT-METHODS --------------------------*/

        return view;
    }

//    Select PAYMENT-METHOD and Create sale
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_credit_card:
                btnDone.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_debit_card:
                Toast.makeText(getActivity(), "Debit Card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_check:
                Toast.makeText(getActivity(), "Check", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_done:
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), "Cash", Toast.LENGTH_SHORT).show();
        }
    }

//    Cash selected
    /*private void onCash() {
        txtAmountRcv.setVisibility(View.VISIBLE);
        editAmountRcv.setVisibility(View.VISIBLE);
        txtChangeDue.setVisibility(View.VISIBLE);
        editChangeDue.setVisibility(View.VISIBLE);
    }*/
    
}
