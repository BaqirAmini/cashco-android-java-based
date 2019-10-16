package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCustomerFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences newCustSp;
    private EditText editBn, editSeller, editFn, editLn, editPhone, editEmail, editCountry, editState, editAddr1, editAddr2,
        editCity, editZipcode, editFaxNo, editNotes, editPriceLevel, editTaxNo;
    private ImageView imgCustPhoto;
    private RadioGroup rdgLimitPurchase, rdgEmployee;
    private RadioButton rdbEmployee, rdbNotEmployee, rdbPurchaseLimited, rdbPurchaseNotLimited;
    private Button btnSaveNewCustomer, btnEditCompProfile, btnEnableCompProfile;
    private int isPurchaseLimited = 0, isEmployee = 0;
    private String custPriceLevel;
    private Bitmap bitmap;
    private String custPhoto;
    private LinearLayout layoutNewCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_customer, container, false);

//        Define widgets
        newCustSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        btnSaveNewCustomer = getActivity().findViewById(R.id.btn_save_new_customer);
        btnEditCompProfile = getActivity().findViewById(R.id.btn_edit_comp_profile);
        btnEnableCompProfile = getActivity().findViewById(R.id.btn_enable_edit_comp_profile);
        btnEnableCompProfile.setVisibility(View.GONE);
        btnEditCompProfile.setVisibility(View.GONE);
        btnSaveNewCustomer.setVisibility(View.VISIBLE);

        btnSaveNewCustomer.setOnClickListener(this);
        editBn = view.findViewById(R.id.edit_business);
        editSeller = view.findViewById(R.id.edit_cust_seller);
        editFn = view.findViewById(R.id.edit_cust_fn);
        editLn = view.findViewById(R.id.edit_cust_ln);
        editPhone = view.findViewById(R.id.edit_cust_phone);
        editEmail = view.findViewById(R.id.edit_change_cust_email);
        editCountry = view.findViewById(R.id.edit_country);
        editState = view.findViewById(R.id.edit_province);
        editAddr1 = view.findViewById(R.id.edit_address1);
        editAddr2 = view.findViewById(R.id.edit_address2);
        editCity = view.findViewById(R.id.edit_city);
        editZipcode = view.findViewById(R.id.edit_zip_code);
        editFaxNo = view.findViewById(R.id.edit_fax_num);
        editNotes = view.findViewById(R.id.edit_notes);
        editPriceLevel = view.findViewById(R.id.edit_price_level);
        editTaxNo = view.findViewById(R.id.edit_tax_number);
        rdgLimitPurchase = view.findViewById(R.id.rdg_for_limit_purchase);
        rdgEmployee = view.findViewById(R.id.rdg_is_employee);
        rdbEmployee = view.findViewById(R.id.rdb_yes);
        rdbNotEmployee = view.findViewById(R.id.rdb_no);
        rdbPurchaseLimited = view.findViewById(R.id.rdb_limit);
        rdbPurchaseNotLimited = view.findViewById(R.id.rdb_not_limit);
        imgCustPhoto = view.findViewById(R.id.img_customer);
        layoutNewCustomer = view.findViewById(R.id.linear_new_customer);
        layoutNewCustomer.setVisibility(View.VISIBLE);

// Upload customer photo
        imgCustPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent();
                photoIntent.setType("image/*");
                photoIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoIntent, "Choose a product image ..."), 0);

            }
        });


        
        
//  Check if customer is an employee
        rdgEmployee.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdb_yes) {
                    isEmployee = 1;
                } else {
                    isEmployee = 0;
                }
            }
        });

// Check if purchase is limited or not limited
        rdgLimitPurchase.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdb_limit) {
                    isPurchaseLimited = 1;
                } else {
                    isPurchaseLimited = 0;
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        if (editBn.getText().toString().isEmpty()) {
            editBn.setError(getString(R.string.bn_required));
        }
        if (editFn.getText().toString().isEmpty()) {
            editFn.setError(getString(R.string.fn_required));
        }
        if (editPhone.getText().toString().isEmpty()) {
            editPhone.setError(getString(R.string.phone_required));
        }
        if (editPhone.getText().toString().length() < 10) {
            editPhone.setError(getString(R.string.min_phone_char));
        }
        if (editCountry.getText().toString().isEmpty()) {
            editCountry.setError(getString(R.string.country_required));
        }
        if (editState.getText().toString().isEmpty()) {
            editState.setError(getString(R.string.state_required));
        }
        if (editCity.getText().toString().isEmpty()) {
            editCity.setError(getString(R.string.city_required));
        }
        if (editAddr1.getText().toString().isEmpty()) {
            editAddr1.setError(getString(R.string.addr1_required));
        }

        if (!editBn.getText().toString().isEmpty() && !editFn.getText().toString().isEmpty() &&
            !editPhone.getText().toString().isEmpty() && editPhone.getText().toString().length() >= 10 &&
                !editCountry.getText().toString().isEmpty() && !editState.getText().toString().isEmpty() && !editCity.getText().toString().isEmpty()) {
            String custSellerPNO = editSeller.getText().toString();
            String custBusName = editBn.getText().toString();
            String custFname = editFn.getText().toString();
            String custLname = editLn.getText().toString();
                custPhoto = getCustImage(bitmap);
            String custPhone = editPhone.getText().toString();
            String custEmail = editEmail.getText().toString();
            String custCountry = editCountry.getText().toString();
            String custState = editState.getText().toString();
            String custAddr1 = editAddr1.getText().toString();
            String custAddr2 = editAddr2.getText().toString();
            String custCity = editCity.getText().toString();
            String custZipCode = editZipcode.getText().toString();
            String custFaxNum = editFaxNo.getText().toString();
            String custNotes = editNotes.getText().toString();
            if (!editPriceLevel.getText().toString().isEmpty()) {
                custPriceLevel = editPriceLevel.getText().toString();
            }

            String custTaxNum = editTaxNo.getText().toString();
//            Toast.makeText(getContext(), "Photo: " + custPhoto, Toast.LENGTH_SHORT).show();
            onSaveCustomer(custSellerPNO, custBusName, custFname, custLname, custPhoto, custPhone, custEmail, custCountry, custState, custAddr1, custAddr2, custCity, custZipCode, custFaxNum, custNotes, custPriceLevel, custTaxNum);


        }

    }

// Refresh Category-fragment
    /*private void onRefershCategory() {
        //                CustomerDetailFragment to show more detail for any customer
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryEditFragment myfragment = new CategoryEditFragment();  //your fragment
        fragmentTransaction.replace(R.id.prd_floyout1, myfragment);
        fragmentTransaction.commit();
    }*/

    private void onSaveCustomer(final String sellerPermitNumber, final String bn, final String fn, @Nullable final String ln, @Nullable final String img, final String phone, @Nullable final String email,
                                final String country, final String state, final String addr1 , @Nullable final String addr2, final String city, @Nullable final String zipCode,
                                @Nullable final String fax, @Nullable final String notes, @Nullable final String priceLevel, @Nullable final String taxNumber) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("newCustomer"), new Response.Listener<String>() {
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
                map.put("compId", String.valueOf(newCustSp.getInt("spCompId", 0)));
                map.put("seller_permit_number", String.valueOf(sellerPermitNumber));
                map.put("business_name", bn);
                map.put("first_name", fn);
                map.put("ln", ln);
                map.put("customer_photo", img);
                map.put("phone", phone);
                map.put("email", email);
                map.put("limit_purchase", String.valueOf(isPurchaseLimited));
                map.put("country", country);
                map.put("state", state);
                map.put("addr1", addr1);
                map.put("addr2", addr2);
                map.put("city", city);
                map.put("zipCode", zipCode);
                map.put("employee", String.valueOf(isEmployee));
                map.put("fax_number", fax);
                map.put("notes", notes);
                map.put("priceLevel", String.valueOf(priceLevel));
                map.put("tax_number", taxNumber);
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


//    While choosing a photo for customer, what should be the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgCustPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method for getting image from storage;
    private String getCustImage(@Nullable Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        }
        byte[] imageBytes = b.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
