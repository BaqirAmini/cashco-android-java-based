package com.xamuor.cashco.Views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.xamuor.cashco.InventoryActivity;
import com.xamuor.cashco.Invoice;
import com.xamuor.cashco.Product;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import static android.content.Context.PRINT_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    private Button btnCash, btnCreditCard, btnDebitCard, btnCheck, btnDone, btnCancel;
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
    private String paymentMethod = "Cash";
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
        btnCancel = view.findViewById(R.id.btn_cancel_payment);

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
        btnCancel.setOnClickListener(this);
        /* ----------------------------------/. Initialize Widgets -----------------------------*/

        /* ------------------------------------------- background for DEFAULT selected payment method -----------------------------*/
        btnCash.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_payment_method));
        /* ------------------------------------------- /. background for DEFAULT selected payment method -----------------------------*/

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
                    if (paymentMethod.equals("Cash")) {
                        txtAmountRcv.setVisibility(View.VISIBLE);
                        editAmountRcv.setVisibility(View.VISIBLE);
                        txtChangeDue.setVisibility(View.VISIBLE);
                        editChangeDue.setVisibility(View.VISIBLE);
                        txtBalanceDue.setVisibility(View.INVISIBLE);
                        editBalanceDue.setVisibility(View.INVISIBLE);

//          Cash method has not transaction number
                        transactionNumber = String.valueOf(0);

                        editAmountRcv.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (editAmountRcv.getText().toString().isEmpty()) {
                                    editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
                                } else if (Double.parseDouble(editAmountRcv.getText().toString()) <= 0) {

                                    editAmountRcv.setError(getString(R.string.err_full_amount_between).concat(String.valueOf(totalAmountDue)));
                                }

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (!editAmountRcv.getText().toString().isEmpty()) {
                                    double recieved = Double.parseDouble(editAmountRcv.getText().toString());
                                    if (recieved > totalAmountDue) {
                                        changeDue = recieved - totalAmountDue;
                                    } else {
                                        editAmountRcv.setError(getString(R.string.err_full_amount_between).concat(" ").concat(String.valueOf(totalAmountDue)));
                                    }

                                } else if (editAmountRcv.getText().toString().isEmpty()){
                                    editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                DecimalFormat precision = new DecimalFormat("0.00");
                                editChangeDue.setText(precision.format(changeDue));
                            }
                        });
                    }

//                    recievedAmount = recieved;
                } else {
                    paymentFraction = "Partial";
//                    ChangeDue not longer required
                    if (paymentMethod.equals("Cash")) {
                        txtChangeDue.setVisibility(View.INVISIBLE);
                        editChangeDue.setVisibility(View.INVISIBLE);
                        txtBalanceDue.setVisibility(View.VISIBLE);
                        editBalanceDue.setVisibility(View.VISIBLE);
                        txtAmountRcv.setVisibility(View.VISIBLE);
                        editAmountRcv.setVisibility(View.VISIBLE);

//          Cash method has not transaction number
                        transactionNumber = String.valueOf(0);
                        editAmountRcv.getText().clear();
                       editAmountRcv.addTextChangedListener(new TextWatcher() {
                           @Override
                           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               if (editAmountRcv.getText().toString().isEmpty()) {
                                   editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
                               } else if (Double.parseDouble(editAmountRcv.getText().toString()) <= 0) {

                                   editAmountRcv.setError(getString(R.string.err_partial_amount_between).concat(String.valueOf(totalAmountDue)));
                               }
                           }

                           @Override
                           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               if (!editAmountRcv.getText().toString().isEmpty()) {
                                   recievedAmount = Double.parseDouble(editAmountRcv.getText().toString());
                                   if (recievedAmount <= totalAmountDue) {
                                       balanceAmount = totalAmountDue - recievedAmount;
                                   } else {
                                       editAmountRcv.setError(getString(R.string.err_partial_amount_between).concat(" ").concat(String.valueOf(totalAmountDue)));
                                   }

                               } else if (editAmountRcv.getText().toString().isEmpty()){
                                   editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
                               }
                           }

                           @Override
                           public void afterTextChanged(Editable editable) {
                               DecimalFormat precision = new DecimalFormat("0.00");
                                editBalanceDue.setText(precision.format(balanceAmount));
                                balanceAmount = Double.parseDouble(editBalanceDue.getText().toString());
                           }
                       });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /* ------------------------/. Choose PAYMENT-METHODS --------------------------*/

        /*editAmountRcv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

//        Define sharePreferences
        paymentSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        return view;
    }

    //    Select PAYMENT-METHOD and Create sale
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_credit_card:
                paymentMethod = "Credit Card";
                btnDone.setVisibility(View.VISIBLE);
                onPayWithCard(null);
                onSelectedPaymentMethod(paymentMethod);
                spnPayment.setEnabled(false);
                break;
            case R.id.btn_debit_card:
                btnDone.setVisibility(View.VISIBLE);
                onPayWithCard(null);
                paymentMethod = "Debit Card";
                onSelectedPaymentMethod(paymentMethod);
                spnPayment.setEnabled(false);
                break;
            case R.id.btn_done:
                /*if (!editTransCode.getText().toString().isEmpty()) {
                    transactionNumber = editTransCode.getText().toString();
                }*/

                if (paymentMethod.equals("Cash")) {
                    onSelectedPaymentMethod(paymentMethod);
                    onCreateSale();
                } else if (paymentMethod.equals("Credit Card") || paymentMethod.equals("Debit Card") || paymentMethod.equals("Check")) {
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
                onSelectedPaymentMethod(paymentMethod);
                spnPayment.setEnabled(false);
                break;
            case R.id.btn_cash:
                btnDone.setVisibility(View.VISIBLE);
                paymentMethod = "Cash";
                transactionNumber = String.valueOf(0);
                onCash();
                onSelectedPaymentMethod(paymentMethod);
                spnPayment.setEnabled(true);
                break;
            case R.id.btn_cancel_payment:
                Intent intent = new Intent(getActivity(), InventoryActivity.class);
                startActivity(intent);
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
        } else if (Objects.equals(pMehtod, null)) {
            txtTransactionCode.setVisibility(View.VISIBLE);
            editTransCode.setVisibility(View.VISIBLE);
            txtTransactionCode.setText(getString(R.string.txt_lbl_trans_code));
            if (editTransCode.getText().toString().isEmpty()) {
                editTransCode.setError(getString(R.string.err_trans_code_required));
            }
        }

//        Hide inputBoxes related to cash
        txtAmountRcv.setVisibility(View.INVISIBLE);
        editAmountRcv.setVisibility(View.INVISIBLE);
        txtChangeDue.setVisibility(View.INVISIBLE);
        editChangeDue.setVisibility(View.INVISIBLE);
        txtBalanceDue.setVisibility(View.INVISIBLE);
        editBalanceDue.setVisibility(View.INVISIBLE);


    }

    //    To send bill-contents to server
//    @SuppressLint("SetTextI18n")
    @SuppressLint("SetTextI18n")
    private boolean onCreateSale() {
      /*  final String transCode;
        if (!editTransCode.getText().toString().isEmpty()) {
            transCode = editTransCode.getText().toString();
        }*/
//        final double recievable = Double.parseDouble(editRecievable.getText().toString());
        double rvd = 0;
        double rvable = 0;
        if (paymentFraction.equals("Full")) {
            if (editAmountRcv.getText().toString().isEmpty()) {
                editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
            } else {
// SEND TO SERVER NOW
                StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().contains("success!")) {
                            Toast.makeText(getActivity(), "Sale was successful!", Toast.LENGTH_SHORT).show();
                            //                    The contents of invoice should be deleted
                            InvoiceFragment.posDatabase.myDao().delete();
//                            btnDone.setBackgroundColor(getContext().getResources().getColor(R.color.bg_tabs));
//                            btnDone.setEnabled(false);
//                      print the invoice
                        onPrint();
    //To refresh the fragment
                        onRefresh();
                        } else if (response.trim().contains("fail")) {
                            Toast.makeText(getActivity(), "Sorry, sale not done, please try again!", Toast.LENGTH_SHORT).show();
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
            }
        } else if (paymentFraction.equals("Partial")) {
            if (editAmountRcv.getText().toString().isEmpty()) {
                editAmountRcv.setError(getString(R.string.err_recieved_amount_required));
            } else {
//               NOW SEND TO SERVER
                    StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().contains("success!")) {
                                Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_LONG).show();
//                       The contents of invoice should be deleted
                                InvoiceFragment.posDatabase.myDao().delete();
//                                btnDone.setBackgroundColor(getContext().getResources().getColor(R.color.bg_tabs));
//                                btnDone.setEnabled(false);
//                                print the invoice
                             onPrint();
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
                            map.put("amount_due", String.valueOf(balanceAmount));
                            map.put("amount_paid", String.valueOf(recievedAmount));
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
                }
            }
        return true;
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
        ProductFragment pf = new ProductFragment();
        // Clear invoice fragment
        fragmentTransaction.replace(R.id.frg_invoice, inf);
//        go back to ProductFragment from PaymentFragment
        fragmentTransaction.replace(R.id.frg_product, pf);
        fragmentTransaction.commit();
    }

    private void onSelectedPaymentMethod(String method) {
        switch (method) {
            case "Credit Card":
                btnCash.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnDebitCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCheck.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCreditCard.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_payment_method));
                break;
            case "Debit Card":
                btnCash.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCreditCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCheck.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnDebitCard.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_payment_method));
                break;
            case "Check":
                btnCash.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnDebitCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCreditCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCheck.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_payment_method));
                break;
            default:
                btnCheck.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnDebitCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCreditCard.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                btnCash.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_payment_method));
        }
    }


    //    Print invoice which is here the listview
    private void onPrint() {
            PrintManager printManager = (PrintManager) getContext().getSystemService(PRINT_SERVICE);
            WebView webView = new WebView(getContext());
            PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
            assert printManager != null;
            printManager.print(String.valueOf(R.id.list_invoice_content), adapter, null);
        }
}
