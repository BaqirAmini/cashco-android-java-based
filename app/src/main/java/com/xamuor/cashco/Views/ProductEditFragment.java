package com.xamuor.cashco.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.ProductAdapter;
import com.xamuor.cashco.Model.ProductEditDataModal;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductEditFragment extends Fragment {
    private RecyclerView rvProductEdit;
    private ProductAdapter adapter;
    private List<ProductEditDataModal> prdList;
    private SharedPreferences prdEditSp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.product_list_for_edit, container, false);

//    Define widgets....
        rvProductEdit = view.findViewById(R.id.rv_list_product_for_edit);
        rvProductEdit.setHasFixedSize(true);
        rvProductEdit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        prdList = new ArrayList<>();
        loadProducts();

//   Define Shared preferences
        prdEditSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
    return view;
    }


    // Load Inventory data for a specific authenticated System-Admin
    public void loadProducts() {
//        String dateString = formatter.format(new Date(Long.parseLong(YOUR TIMESTAMP VALUE)));
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("loadProduct"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                pd.dismiss();
                try {
/*
//                        for ROOM DB
                    Inventories inventory = new Inventories();
//                        Fetch all inventories from ROOM
                    List<Inventories> inventories = InvoiceFragment.posDatabase.myDao().getInventories(Users.getCompanyId());
*/

//                        fetch data of inventories from server
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int pId = jsonObject.getInt("item_id");
                        String pImage = jsonObject.getString("item_image");
                        String pName = jsonObject.getString("item_name");
                        String pDesc = jsonObject.getString("item_desc");
                        String barcode = jsonObject.getString("barcode_number");
                        String pPurchasePrice = jsonObject.getString("purchase_price");
                        String pSellPrice = jsonObject.getString("sell_price");
                        String created = jsonObject.getString("created_at");
                        String updated = jsonObject.getString("updated_at");

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
                        String c = dateOnly.format(cal.getTime());
                        String u = dateOnly.format(cal.getTime());
                        String pQty = jsonObject.getString("quantity");
                        ProductEditDataModal dataModal = new ProductEditDataModal(pId, pImage, pName, pDesc, pQty, barcode, pPurchasePrice, pSellPrice, c, u);
                        prdList.add(dataModal);
//         Set data into inventories of ROOM from server
                        /*inventory.setCompId(Users.getCompanyId());
                        inventory.setProductId(pId);
                        inventory.setProductImage(pImage);
                        inventory.setProductName(pName);
                        inventory.setSellPrice(pSellPrice);
                        inventory.setQty(pQty);
                        InvoiceFragment.posDatabase.myDao().insertIntoInventories(inventory);*/

                    }
                   /* for (Inventories inv : inventories) {
                        int id = inv.getProductId();
                        String image = inv.getProductImage();
                        String product = inv.getProductName();
                        double sellPrice = inv.getSellPrice();
                        int qty = inv.getQty();
                        InventoryDataModal dataModal = new InventoryDataModal(id, image, product, sellPrice, qty);
                        productList.add(dataModal);

                    }*/
                    adapter = new ProductAdapter(getContext(), prdList);
                    rvProductEdit.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                d.setMessage(error.toString());
                d.show();*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(prdEditSp.getInt("spCompId", 0)));
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
