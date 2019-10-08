package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Category;
import com.xamuor.cashco.CustomerIDForInvoice;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditAnyItemFragment extends Fragment {

    private Spinner spnCategory;
    private EditText editProductName, editProductDesc, editProductQty, editProductBarcode, editProductCost, editProductRetailPrice;
    private SharedPreferences edititemSp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_any_item, container, false);

//    Define widgets
        spnCategory = view.findViewById(R.id.spn_category);
        editProductName = view.findViewById(R.id.edit_product_name);
        editProductDesc = view.findViewById(R.id.edit_product_desc);
        editProductQty = view.findViewById(R.id.edit_product_qty);
        editProductBarcode = view.findViewById(R.id.edit_product_barcode);
        editProductCost = view.findViewById(R.id.edit_product_cost);
        editProductRetailPrice = view.findViewById(R.id.edit_product_retail_price);

//   SharedPreferences
    edititemSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//    Call to load categories
        listCategoriesInSpinner();

// Call to fetch data for edition
        getDataFromBundle();

//   Select a category from spinner
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                   /* chkAllAmount.setEnabled(false);
                    spnPaymentType.setEnabled(false);
                    layoutForPayment.setVisibility(View.GONE);
                    btnPrint.setEnabled(false);
                    btnPrint.setBackgroundColor(getResources().getColor(R.color.disable_color));*/
                } else {
                    String custName = (String) adapterView.getItemAtPosition(i);
// we give the name of customer, ROOM returns back its custID
                    int custId = InvoiceFragment.posDatabase.myDao().getCustId(custName);
                     CustomerIDForInvoice.setCustomerID(custId);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    // Populate spinner with categories
    private void listCategoriesInSpinner() {
//   Delete once while loading to avoid duplicate data
//     InvoiceFragment.posDatabase.myDao().deleteCategories();
        StringRequest selectCustomerRequest = new StringRequest(Request.Method.POST, Routes.setUrl("listCategory"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//             InvoiceFragment.posDatabase.myDao().deleteCategories();
                if (response.trim().contains("not found")) {
                    Toast.makeText(getContext(), "Sorry, no customer existing, please register first.", Toast.LENGTH_LONG).show();
                } else {
                    try {
//                     Json-array for json data from server
                        JSONArray categories = new JSONArray(response);

//                     Customer object to insert data into ROOM
                     Category category = new Category();
//                     To populate customers into it for spinner
                        List<String> categoryList = new ArrayList<String>();
//                     Existing categories into ROOM database
                     List<Category> existingCategories = InvoiceFragment.posDatabase.myDao().getCategories(Users.getCompanyId());
//                      First Delete previous customers
                        if (getActivity() != null) {
                            categoryList.add(getResources().getString(R.string.spn_categories));
                        }
                        for (int c = 0; c < categories.length(); c++) {
                            JSONObject custObject = categories.getJSONObject(c);
                            int ctgId = custObject.getInt("ctg_id");
                            String ctgName = custObject.getString("ctg_name");
                            String ctgDesc = custObject.getString("ctg_desc");
//                            categoryList.add(String.valueOf(ctgId));
//                         set json-values into ROOM
                                category.setCompanyId(Users.getCompanyId());
                                category.setCategoryID(ctgId);
                                category.setCategoryName(ctgName);
                                category.setCategoryDesc(ctgDesc);
                                InvoiceFragment.posDatabase.myDao().insertCategory(category);
                        }
//              Set categories in SPINNER from ROOM
                     for (Category ctg : existingCategories) {
                         int rCtgId = ctg.getCategoryID();
                         String rCtgName = ctg.getCategoryName();
                             categoryList.add(rCtgName);
                     }
                     if (getActivity() != null) {
                         ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
                         spnCategory.setAdapter(customerAdapter);
                         customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(edititemSp.getInt("spCompId", 0)));
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(selectCustomerRequest);
    }


// Fetch data from another FRAGMENT
    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        editProductName.setText(bundle.getString("item_name"));
        editProductDesc.setText(Objects.requireNonNull(bundle.getString("item_desc")).replace("null", ""));
        editProductQty.setText(bundle.getString("item_qty"));
        editProductBarcode.setText(bundle.getString("item_barcode"));
        editProductCost.setText(String.valueOf(bundle.getDouble("item_cost")));
        editProductRetailPrice.setText(String.valueOf(bundle.getDouble("item_retail_price")));

    }
}
