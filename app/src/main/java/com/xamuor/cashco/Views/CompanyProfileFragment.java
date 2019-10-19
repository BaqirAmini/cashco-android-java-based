package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.CustomerActivity;
import com.xamuor.cashco.CustomerDetail;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyProfileFragment extends Fragment implements View.OnClickListener {
    private Button btnEditCompProfile, btnSaveCompProfile, btnSaveNewCust;
    private EditText editCompSeller, editCompBn, editCompPhone, editCompEmail, editCompCountry, editCompState, editCompAddr1, editCompAddr2,
            editCompCity, editCompZipcode;
    private RadioGroup rdgCompEmployee;
    private RadioButton rdbCompEmployee, rdbNotEmployee;
    private SharedPreferences compProfileSp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);

//        Define widgets
        btnSaveCompProfile = getActivity().findViewById(R.id.btn_edit_comp_profile);
        btnEditCompProfile = getActivity().findViewById(R.id.btn_enable_edit_comp_profile);
        btnSaveNewCust = getActivity().findViewById(R.id.btn_save_new_customer);
        editCompSeller = view.findViewById(R.id.edit_comp_seller);
        editCompBn = view.findViewById(R.id.edit_comp_bn);
        editCompPhone = view.findViewById(R.id.edit_comp_phone);
        editCompEmail = view.findViewById(R.id.edit_comp_email);
        editCompCountry = view.findViewById(R.id.edit_comp_country);
        editCompState = view.findViewById(R.id.edit_comp_province);
        editCompAddr1 = view.findViewById(R.id.edit_comp_address1);
        editCompAddr2 = view.findViewById(R.id.edit__comp_address2);
        editCompCity = view.findViewById(R.id.edit_comp_city);
        editCompZipcode = view.findViewById(R.id.edit_comp_zip_code);
        rdgCompEmployee = view.findViewById(R.id.rdg_comp_is_employee);
        rdbCompEmployee = view.findViewById(R.id.rdb_emplyee);
        rdbNotEmployee = view.findViewById(R.id.rdb_not_employee);

        compProfileSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        btnEditCompProfile.setVisibility(View.VISIBLE);
        btnSaveCompProfile.setVisibility(View.VISIBLE);
        btnSaveNewCust.setVisibility(View.GONE);
        btnSaveCompProfile.setOnClickListener(this);
        btnEditCompProfile.setOnClickListener(this);

//  CALL to set values in editTextes
    setCompanyDetails();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enable_edit_comp_profile:
                enableEdition();
                break;
            case R.id.btn_edit_comp_profile:
                saveChanges();
        }
    }
    
// Enable company-profile edition
    private void enableEdition() {
        btnSaveCompProfile.setEnabled(true);
        btnSaveCompProfile.setBackgroundResource(R.color.txt_selected_tab);
        btnSaveCompProfile.getResources().getColor(R.color.white_color);

//    ENABLE editTextes and radiobutons
        editCompSeller.setEnabled(true);
        editCompBn.setEnabled(true);
        editCompPhone.setEnabled(true);
        editCompEmail.setEnabled(true);
        editCompCountry.setEnabled(true);
        editCompState.setEnabled(true);
        editCompAddr1.setEnabled(true);
        editCompAddr2.setEnabled(true);
        editCompCity.setEnabled(true);
        editCompZipcode.setEnabled(true);
        rdgCompEmployee.setEnabled(true);
        rdbNotEmployee.setEnabled(true);
        rdbCompEmployee.setEnabled(true);

//        Disable itself
        btnEditCompProfile.setEnabled(false);
        btnEditCompProfile.setBackgroundResource(R.color.bg_tabs);
        btnEditCompProfile.getResources().getColor(R.color.white_color);
    }

// Save changes with company-profile
    private void saveChanges() {
//    Validate fields first
        if (editCompSeller.getText().toString().isEmpty()) {
            editCompSeller.setError(getString(R.string.seller_permit_no_required));
        }
        if (editCompBn.getText().toString().isEmpty()) {
            editCompBn.setError(getString(R.string.bn_required));
        }
        if (editCompPhone.getText().toString().isEmpty()) {
            editCompPhone.setError(getString(R.string.phone_required));
        }
        if (editCompPhone.getText().toString().length() < 10) {
            editCompPhone.setError(getString(R.string.min_phone_char));
        }

        if (editCompCountry.getText().toString().isEmpty()) {
            editCompCountry.setError(getString(R.string.country_required));
        }

        if (editCompState.getText().toString().isEmpty()) {
            editCompState.setError(getString(R.string.state_required));
        }
        if (editCompAddr1.getText().toString().isEmpty()) {
            editCompAddr1.setError(getString(R.string.addr1_required));
        }

        if (editCompCity.getText().toString().isEmpty()) {
            editCompCity.setError(getString(R.string.city_required));
        }

        if (editCompZipcode.getText().toString().length() > 4 || editCompZipcode.getText().toString().length() < 4) {
            editCompZipcode.setError(getString(R.string.zipcode_only));
        }
        
        if (!editCompSeller.getText().toString().isEmpty() && !editCompBn.getText().toString().isEmpty() && !editCompPhone.getText().toString().isEmpty() &&
        editCompPhone.getText().toString().length() >= 10 && !editCompCountry.getText().toString().isEmpty() && !editCompState.getText().toString().isEmpty() 
        && !editCompAddr1.getText().toString().isEmpty() && !editCompCity.getText().toString().isEmpty() && editCompZipcode.getText().toString().length() == 4) {
            onEditCustomer(editCompSeller.getText().toString(), editCompBn.getText().toString(), editCompPhone.getText().toString(), editCompEmail.getText().toString(),
                    editCompCountry.getText().toString(), editCompState.getText().toString(), editCompAddr1.getText().toString(), editCompAddr2.getText().toString(),
                    editCompCity.getText().toString(), editCompZipcode.getText().toString());
        }
    }

//    Set company details into editTextes
    private void setCompanyDetails() {
        editCompSeller.setText(CustomerDetail.getSellerPermitNumber());
        editCompBn.setText(CustomerDetail.getBssName());
        editCompPhone.setText(CustomerDetail.getPhone());
        editCompEmail.setText(CustomerDetail.getEmail());
        editCompCountry.setText(CustomerDetail.getCountry());
        editCompState.setText(CustomerDetail.getState());
        editCompAddr1.setText(CustomerDetail.getAddr1());
        editCompAddr2.setText(CustomerDetail.getAddr2());
        editCompCity.setText(CustomerDetail.getCity());
        editCompZipcode.setText(CustomerDetail.getZipCode());
    }

//  Edit Customer using company profile
    private void onEditCustomer(final String sellerPermitNumber, final String bn, final String phone, final String email, final String country, final String state,
                                final String addr1, final String addr2, final String city, final String zipCode) {

            StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editCustomer"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AlertDialog.Builder d;
                    try {
                        JSONObject jo = new JSONObject(response);
                        if (jo.getString("result").equalsIgnoreCase("fail")) {
                            String vMessage = jo.getString("message");
                            d = new AlertDialog.Builder(getContext());
                            d.setMessage(vMessage);
                            d.show();
                        } else if (jo.getString("result").equalsIgnoreCase("success")) {
                            Intent intent = new Intent(getContext(), CustomerActivity.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
//             String pImage = getProductImage(bitmap);
                    Map<String, String> map = new HashMap<>();
                    map.put("custId", CustomerDetail.getId());
                    map.put("compId", String.valueOf(compProfileSp.getInt("spCompId", 0)));
                    map.put("seller_permit_number", String.valueOf(sellerPermitNumber));
                    map.put("business_name", bn);
                    map.put("phone", phone);
                    map.put("email", email);
                    map.put("country", country);
                    map.put("state", state);
                    map.put("addr1", addr1);
                    map.put("addr2", addr2);
                    map.put("city", city);
                    map.put("zipCode", zipCode);
                    return map;
                }
            };
//        Policy for timeoutError
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(request);
        }

}
