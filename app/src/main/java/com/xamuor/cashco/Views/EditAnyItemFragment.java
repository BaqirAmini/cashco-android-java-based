package com.xamuor.cashco.Views;


import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Category;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.PosDatabase;
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
    public static PosDatabase posDatabase;
    private Spinner spnCategory;
    private EditText editProductName, editProductDesc, editProductQty, editProductBarcode, editProductCost, editProductRetailPrice;
    private SharedPreferences edititemSp;
    private Button btnSave;
    private int ctgSelected;
    private LinearLayout productLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_any_item, container, false);

//    Define widgets
        productLayout = view.findViewById(R.id.layout_for_edit_products);
        spnCategory = view.findViewById(R.id.spn_category);
        editProductName = view.findViewById(R.id.edit_product_name);
        editProductDesc = view.findViewById(R.id.edit_product_desc);
        editProductQty = view.findViewById(R.id.edit_product_qty);
        editProductBarcode = view.findViewById(R.id.edit_product_barcode);
        editProductCost = view.findViewById(R.id.edit_product_cost);
        editProductRetailPrice = view.findViewById(R.id.edit_product_retail_price);
        btnSave = ((Activity) getContext()).findViewById(R.id.btn_save_edited_product);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateProduct();
            }
        });

//   SharedPreferences
        edititemSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//    Call to load categories
        listCategoriesInSpinner();

// Call to fetch data for edition
        getDataFromBundle();

//    DB in ROOM
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();


//   Select a category from spinner
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String ctgName = (String) adapterView.getItemAtPosition(i);
// we give the name of category, ROOM returns back its custID
                    int ctgID = posDatabase.myDao().getCategoryId(ctgName);
                    Category.setsCtgId(ctgID);
                    ctgSelected = i;
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

    //    Now update the product Item
    private void onUpdateProduct() {
        Bundle bundle = getArguments();


        final int pid, barcode, qty;
        final String name, desc;
        final double cost, sellPrice;
        if (editProductName.getText().toString().isEmpty()) {
            editProductName.setError(getString(R.string.prd_name_required));
        } else if (editProductQty.getText().toString().isEmpty()) {
            editProductQty.setError(getString(R.string.prd_qty_required));
        } else if (editProductBarcode.getText().toString().isEmpty()) {
            editProductBarcode.setError(getString(R.string.prd_barcode_required));
        } else if (editProductCost.getText().toString().isEmpty()) {
            editProductCost.setError(getString(R.string.prd_cost_required));
        } else if (editProductRetailPrice.getText().toString().isEmpty()) {
            editProductRetailPrice.setError(getString(R.string.prd_retail_price_required));
        } else {

            pid = bundle.getInt("item_id");
            barcode = Integer.parseInt(editProductBarcode.getText().toString());
            name = editProductName.getText().toString();
            desc = editProductDesc.getText().toString();
            qty = Integer.parseInt(editProductQty.getText().toString());
            cost = Double.parseDouble(editProductCost.getText().toString());
            sellPrice = Double.parseDouble(editProductRetailPrice.getText().toString());

//            Category selected in SPINNER
            if (ctgSelected > 0) {
                StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editProduct"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(getActivity(), "Product updated successfully!", Toast.LENGTH_SHORT).show();
                            productLayout.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(getActivity(), "Sorry, product not updated!", Toast.LENGTH_SHORT).show();
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
                        map.put("ctgId", String.valueOf(Category.getsCtgId()));
                        map.put("itemId", String.valueOf(pid));
                        map.put("itemName", name);
                        map.put("itemDesc", desc);
                        map.put("itemBarcode", String.valueOf(barcode));
                        map.put("itemQty", String.valueOf(qty));
                        map.put("itemCost", String.valueOf(cost));
                        map.put("itemSellPrice", String.valueOf(sellPrice));
                        return map;
                    }
                };


//        Policy for timeoutError
                request.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getActivity()).add(request);

                Volley.newRequestQueue(getContext()).add(request);

//         Category not selected in SPINNER
            } else {
                StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editProduct"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(getActivity(), "Product updated successfully!", Toast.LENGTH_SHORT).show();
                            productLayout.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(getActivity(), "Sorry, product not updated!", Toast.LENGTH_SHORT).show();
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
                        map.put("itemId", String.valueOf(pid));
                        map.put("itemName", name);
                        map.put("itemDesc", desc);
                        map.put("itemBarcode", String.valueOf(barcode));
                        map.put("itemQty", String.valueOf(qty));
                        map.put("itemCost", String.valueOf(cost));
                        map.put("itemSellPrice", String.valueOf(sellPrice));
                        return map;
                    }
                };


//        Policy for timeoutError
                request.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getActivity()).add(request);

                Volley.newRequestQueue(getContext()).add(request);
            }

        }

    }
}
