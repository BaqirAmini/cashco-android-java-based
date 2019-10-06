package com.xamuor.cashco.Views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.InventoryAdapter;
import com.xamuor.cashco.Invoice;
import com.xamuor.cashco.Product;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    private Button btnCash, btnCreditCard, btnDebitCard, btnCheck, btnDone;
    private TextView txtAmountRcv, txtChangeDue, txtBalanceDue, txtTransactionCode;
    private EditText editTotalAmountDue, editAmountRcv, editChangeDue, editBalanceDue, editTransCode;
    private Spinner spnPayment;
    private SharedPreferences paymentSp;
//    these members may be needed later
    private double totalAmountDue;
    private double recievedAmount;
    private double changeDue;
    private double balanceAmount;
    private double tax;
    private double subTotal;
    private String paymentMethod;
//    variable single but use for two TRANSACTION_NUMBER & CHECK_NUMBER
    private String transactionNumber;
    private String paymentFraction;

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
          /*  String recieved = editAmountRcv.getText().toString();
            String changeDue = editChangeDue.getText().toString();
            String balanceDue = editBalanceDue.getText().toString();*/


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    paymentFraction = "Full";
                    onPayFull();
                    /*editAmountRcv.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            changeDue = recievedAmount - totalAmountDue;
                            editChangeDue.setText(changeDue + "");
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });*/
                    txtAmountRcv.setVisibility(View.VISIBLE);
                    editAmountRcv.setVisibility(View.VISIBLE);
                    txtChangeDue.setVisibility(View.VISIBLE);
                    editChangeDue.setVisibility(View.VISIBLE);
                    txtBalanceDue.setVisibility(View.VISIBLE);
                    editBalanceDue.setVisibility(View.VISIBLE);
//                    recievedAmount = recieved;
                } else {
                    paymentFraction = "Partial";
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

//        Define sharePreferences
        paymentSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        return view;
    }

//    Select PAYMENT-METHOD and Create sale
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_credit_card:
                paymentMethod = "Credit Card";
                btnDone.setVisibility(View.VISIBLE);
                onPayWithCard(null);
                break;
            case R.id.btn_debit_card:
                btnDone.setVisibility(View.VISIBLE);
                onPayWithCard(null);
                paymentMethod = "Debit Card";
                break;
            case R.id.btn_done:
                /*if (!editTransCode.getText().toString().isEmpty()) {
                    transactionNumber = editTransCode.getText().toString();
                }*/

                if (paymentMethod.equals("Cash")) {
                    onCreateSale();
                } else if (paymentMethod.equals("Credit Card") || paymentMethod.equals("Debit Card") || paymentMethod.equals("Check")){
                    if (!editTransCode.getText().toString().isEmpty()) {
                        transactionNumber = editTransCode.getText().toString();
                        onCreateSale();
                    }
                }


//                Toast.makeText(getActivity(), "Customer: " + CustomerIDForInvoice.getCustomerID() + " New Way: " + Users.getCustId(), Toast.LENGTH_SHORT).show();
//                Create sale in SERVER

                break;
            case R.id.btn_check:
                btnDone.setVisibility(View.VISIBLE);
                onPayWithCard("check");
                paymentMethod = "Check";
                break;
            default:
                btnDone.setVisibility(View.VISIBLE);
                paymentMethod = "Cash";
                transactionNumber = String.valueOf(0);
                onCash();
        }
    }

//    Cash selected
    private void onCash() {
        txtAmountRcv.setVisibility(View.VISIBLE);
        editAmountRcv.setVisibility(View.VISIBLE);
        txtChangeDue.setVisibility(View.VISIBLE);
        editChangeDue.setVisibility(View.VISIBLE);
        txtTransactionCode.setVisibility(View.INVISIBLE);
        editTransCode.setVisibility(View.INVISIBLE);
    }

//    If payment is not cash
    private void onPayWithCard(@Nullable String pMehtod) {
        if (Objects.equals(pMehtod, "check")) {
            txtTransactionCode.setVisibility(View.VISIBLE);
            editTransCode.setVisibility(View.VISIBLE);
            txtTransactionCode.setText(getString(R.string.txt_lbl_check_code));
            if (editTransCode.getText().toString().isEmpty()) {
                editTransCode.setError(getString(R.string.err_check_required));
            }
        } else if (Objects.equals(pMehtod, null)){
            txtTransactionCode.setVisibility(View.VISIBLE);
            editTransCode.setVisibility(View.VISIBLE);
            txtTransactionCode.setText(getString(R.string.txt_lbl_trans_code));
            if (editTransCode.getText().toString().isEmpty()) {
                editTransCode.setError(getString(R.string.err_trans_code_required));
            }
        }

    }

    //    To send bill-contents to server
//    @SuppressLint("SetTextI18n")
    @SuppressLint("SetTextI18n")
    private void onCreateSale() {
      /*  final String transCode;
        if (!editTransCode.getText().toString().isEmpty()) {
            transCode = editTransCode.getText().toString();
        }*/
//        final double recievable = Double.parseDouble(editRecievable.getText().toString());
        double rvd = 0;
        double rvable = 0;
        if (paymentFraction.equals("Full")) {
            StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.trim().contains("success!")) {
                        Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_SHORT).show();
                        //                    The contents of invoice should be deleted
                        InvoiceFragment.posDatabase.myDao().delete();
                        btnDone.setBackgroundColor(getContext().getResources().getColor(R.color.bg_tabs));
                        btnDone.setEnabled(false);
//                      print the invoice
//                        onPrint();
                        //To refresh the fragment
                        onRefresh();
                    } else if (response.trim().contains("fail")) {
                        Toast.makeText(getContext(), "Sorry, sale not done, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                    d.setMessage(error.toString());
                    d.show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject;
                    double total = 0;
                    for (int i = 0; i < dataList.size(); i++) {
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", dataList.get(i).getProductId());
                            jsonObject.put("compId", String.valueOf(paymentSp.getInt("spCompId", 0)));
                            jsonObject.put("userId", Users.getUserId());
                            jsonObject.put("custId", Users.getCustId());
                            int qty = dataList.get(i).getProductQty();
                            double price = dataList.get(i).getProductPrice();
//                            double subtotal = qty * price;
                            jsonObject.put("qty", qty);
                            jsonObject.put("price", price);
                            jsonObject.put("subtotal", Invoice.getSubTotal());
                            //                        total
//                            total = total + subtotal;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("params", jsonArray.toString());
                    map.put("transCode", transactionNumber);
//                   map.put("recievable", String.valueOf(finalRvable));
//                    Here amountPaid and totalAmountDue are the same because it fully paid.
                    map.put("amount_paid", String.valueOf(totalAmountDue));
                    map.put("total_invoice", String.valueOf(totalAmountDue));
                    map.put("pay_method", paymentMethod);
                    return map;
                }
            };
            saleRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getContext()).add(saleRequest);
        } else if (paymentFraction.equals("Partial")) {
            if (editAmountRcv.getText().toString().isEmpty()) {
                editAmountRcv.setError("Sorry, recieved amount cannot be blank.");
            } else {
                final double recieved = Double.parseDouble(editAmountRcv.getText().toString());
                if (recieved < totalAmountDue) {
                    final double remainingAmount = totalAmountDue - recieved;
                    rvd = recieved;
                    rvable = remainingAmount;
                    editBalanceDue.setText(remainingAmount + "");
//                   Toast.makeText(getContext(), "Recieved: " + rvd + " Payment: " + paymentValue, Toast.LENGTH_SHORT).show();
                    final double finalRvd = rvd;
                    final double finalRvable = rvable;
                    StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().contains("success!")) {
                                Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_LONG).show();
//                       The contents of invoice should be deleted
                                InvoiceFragment.posDatabase.myDao().delete();
                                btnDone.setBackgroundColor(getContext().getResources().getColor(R.color.bg_tabs));
                                btnDone.setEnabled(false);
                                editAmountRcv.getText().clear();
//                                print the invoice
//                             onPrint();
                                //To refresh the fragment
                                onRefresh();
                            } else if (response.trim().contains("fail")) {
                                Toast.makeText(getContext(), "Sorry, sale not done, please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                            d.setMessage(error.toString());
                            d.show();

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject;
                            double total = 0;
                            for (int i = 0; i < dataList.size(); i++) {

                                jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("id", dataList.get(i).getProductId());
                                    jsonObject.put("compId", Users.getCompanyId());
                                    jsonObject.put("userId", Users.getUserId());
                                    jsonObject.put("custId", Users.getCustId());
                                    int qty = dataList.get(i).getProductQty();
                                    double price = dataList.get(i).getProductPrice();
                                    double subtotal = qty * price;
                                    jsonObject.put("qty", qty);
                                    jsonObject.put("price", price);
                                    jsonObject.put("subtotal", subtotal);
                                    //                        total
                                    total = total + subtotal;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(jsonObject);
                            }

                            Map<String, String> map = new HashMap<>();
                            map.put("params", jsonArray.toString());
                            map.put("transCode", transactionNumber);
                            map.put("amount_due", String.valueOf(remainingAmount));
                            map.put("amount_paid", String.valueOf(recieved));
                            map.put("total_invoice", String.valueOf(totalAmountDue));
                            map.put("pay_method", paymentMethod);
                            return map;
                        }
                    };
                    saleRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(getContext()).add(saleRequest);
//                    editAmountRcv.getText().clear();
                } else {
                    editAmountRcv.setError("Sorry, recieved amount can only be between 0 and " + totalAmountDue);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void onPayFull() {
        totalAmountDue = Invoice.getTotal();
        tax = Invoice.getTax();
        subTotal = Invoice.getSubTotal();
        String rvdAmount = editAmountRcv.getText().toString();
        if (!rvdAmount.isEmpty()) {
            recievedAmount = Double.parseDouble(rvdAmount);
            changeDue = recievedAmount - totalAmountDue;
            editChangeDue.setText(changeDue + "");
        } else {
            editAmountRcv.setText(totalAmountDue + "");
        }
        editTotalAmountDue.setText(String.valueOf(totalAmountDue));
    }

//    When sale is succefully done, it navigates back to dashboard
    private void onRefresh() {
        InventoryAdapter.qty = 1;
        android.support.v4.app.FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        InvoiceFragment inf = new InvoiceFragment();  //your fragment
//        ProductFragment pf = new ProductFragment();
        // Clear invoice fragment
        fragmentTransaction.replace(R.id.frg_invoice, inf);
//        go back to ProductFragment from PaymentFragment
//        fragmentTransaction.replace(R.id.frg_product, pf);
        fragmentTransaction.commit();
    }

}
