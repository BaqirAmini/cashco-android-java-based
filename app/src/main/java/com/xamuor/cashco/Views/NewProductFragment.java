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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Category;
import com.xamuor.cashco.Model.CategoryDataModal;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductFragment extends Fragment {
    private Bitmap bitmap;
    private EditText editProductName, editProductDesc, editProductPurchase, editProductSell, editProductQty, editProductBarcode;
    private RadioGroup rdgTax;
    private RadioButton rdbTaxable, rdbNotTaxable;
    private ImageView imgProductImage;
    private Button btnAddProduct, btnCancelProduct;
    private TextView txtCategoryName;
    private String pName, pDesc, pPurchase, pSell, pQty, pBarcode, isTaxable;
    private Spinner spnSelectCategory;
    private SharedPreferences newPrdSp;
    private int ctgSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
//     Initiate widgets
        editProductName = view.findViewById(R.id.edit_product_name);
        editProductDesc = view.findViewById(R.id.edit_product_desc);
        editProductPurchase = view.findViewById(R.id.edit_product_purchase);
        editProductSell = view.findViewById(R.id.edit_product_sell);
        editProductQty = view.findViewById(R.id.edit_product_qty);
        editProductBarcode = view.findViewById(R.id.edit_product_barcode);
        txtCategoryName = view.findViewById(R.id.txt_ctg_name);
        rdgTax = view.findViewById(R.id.rdg_for_tax);
        rdbTaxable = view.findViewById(R.id.rdb_taxable);
        rdbNotTaxable = view.findViewById(R.id.rdb_not_taxable);
        imgProductImage = view.findViewById(R.id.img_product_image);
        btnAddProduct = (getActivity()).findViewById(R.id.btn_add_product);
        btnCancelProduct = view.findViewById(R.id.btn_cancel_product);
        spnSelectCategory = view.findViewById(R.id.spn_select_category);
        newPrdSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);


// BY DEFUALT btnAddProduct is not visible
btnAddProduct.setVisibility(View.VISIBLE);
        /*----------------------------- CHOOSE A CATEGORY -------------------------------*/
        //   Select a category from spinner
        spnSelectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String ctgName = (String) adapterView.getItemAtPosition(i);
// we give the name of category, ROOM returns back its custID
                    int ctgID = InvoiceFragment.posDatabase.myDao().getCategoryId(ctgName);
                    Category.setsCtgId(ctgID);
                    ctgSelected = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*----------------------------- /.CHOOSE A CATEGORY -------------------------------*/

        listCategoriesInSpinner();

//     listener to add new product
        btnAddProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//             Get text or values
                pName = editProductName.getText().toString();
                pDesc = editProductDesc.getText().toString();
                pPurchase = editProductPurchase.getText().toString();
                pSell = editProductSell.getText().toString();
                pQty = editProductQty.getText().toString();
                pBarcode = editProductBarcode.getText().toString();
                if (rdbTaxable.isChecked()) {
                    isTaxable = rdbTaxable.getText().toString();
                } else {
                    isTaxable = rdbNotTaxable.getText().toString();
                }
                if (ctgSelected <= 0) {
                    Toast.makeText(getActivity(), "Sorry, category not selected.", Toast.LENGTH_LONG).show();
                }
                if (pName.isEmpty()) {
                    editProductName.setError("Product name required!");
                }
                if (pPurchase.isEmpty()) {
                    editProductPurchase.setError("Purchase price required!");
                }
                if (pSell.isEmpty()) {
                    editProductSell.setError("Sell price required!");
                }
                if (pQty.isEmpty()) {
                    editProductQty.setError("Product quantity required!");
                }
                if (pBarcode.isEmpty()) {
                    editProductBarcode.setError("Barcode required!");
                }
                if (!pName.isEmpty() && !pPurchase.isEmpty() && !pSell.isEmpty() && !pQty.isEmpty() && !pBarcode.isEmpty()) {
//                 Cast to Double or Int
                    double puchase = Double.parseDouble(pPurchase);
                    double sell = Double.parseDouble(pSell);
                    int qty = Integer.parseInt(pQty);
                    int barcode = Integer.parseInt(pBarcode);
                    sendProduct(pName, pDesc, puchase, sell, qty, barcode, isTaxable);
                }
            }
        });

        imgProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent();
                photoIntent.setType("image/*");
                photoIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoIntent, "Choose a product image ..."), 0);

            }
        });

//     Cancel Adding products
        btnCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pName = editProductName.getText().toString();
                pDesc = editProductDesc.getText().toString();
                //                data-modal is required to refresh CategoryRelatedFragment
                CategoryDataModal modal = new CategoryDataModal(CategoryRelatedFragment.ctgId, CategoryRelatedFragment.ctgName, pDesc);
                CategoryRelatedFragment.onRefreshCategoryRelated(modal, getContext());
            }
        });

        return view;
    }

    //    send product to server
    private void sendProduct(final String pName, @Nullable final String pDesc, final double puchase, final double sell, final int qty, final int barcode, final String taxable) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("newProduct"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getActivity(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                    editProductName.getText().clear();
                    editProductDesc.getText().clear();
                    editProductPurchase.getText().clear();
                    editProductSell.getText().clear();
                    editProductQty.getText().clear();
                    editProductBarcode.getText().clear();
                    btnAddProduct.setVisibility(View.GONE);
                    goToPrdEditFragment();

                } else if (response.trim().equals("fail")) {
                    Toast.makeText(getActivity(), "Sorry, product not added, try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                d.setMessage("Something wrong, try again.");
                d.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//             String pImage = getProductImage(bitmap);
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(newPrdSp.getInt("spCompId", 0)));
                map.put("ctgId", String.valueOf(Category.getsCtgId()));
                map.put("pName", pName);
                map.put("pDesc", pDesc);
//             map.put("pImage", pImage);
                map.put("pPurchase", String.valueOf(puchase));
                map.put("pSell", String.valueOf(sell));
                map.put("pQty", String.valueOf(qty));
                map.put("pBarcode", String.valueOf(barcode));
                map.put("pTaxable", String.valueOf(taxable));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    // Method for getting image from storage;
    private String getProductImage(Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        byte[] imageBytes = b.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgProductImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    Send product Image to server
    private void onSendProductImage() {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.onLoadImage("product_images"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
//             map.put("")
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
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
                            spnSelectCategory.setAdapter(customerAdapter);
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
                map.put("compId", String.valueOf(newPrdSp.getInt("spCompId", 0)));
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(selectCustomerRequest);
    }


// GO TO fragment product-list
 private void goToPrdEditFragment() {
     android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
     android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
     ProductEditFragment nf = new ProductEditFragment();  //your fragment
//        eaf.setArguments(bundle);
     // work here to add, remove, etc
     fragmentTransaction.replace(R.id.prd_floyout1, nf);
     fragmentTransaction.commit();
 }
}