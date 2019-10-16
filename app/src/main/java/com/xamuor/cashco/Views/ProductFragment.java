package com.xamuor.cashco.Views;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.InventoryAdapter;
import com.xamuor.cashco.Inventories;
import com.xamuor.cashco.Model.InventoryDataModal;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static PosDatabase posDatabase;
    private Intent intent;
    private RecyclerView inventoryRv;
    private InventoryAdapter adapter;
    private List<InventoryDataModal> productList;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        inventoryRv = view.findViewById(R.id.rcv_inventory);
        inventoryRv.setHasFixedSize(true);
        inventoryRv.setLayoutManager(new GridLayoutManager(getContext(), 5));
//        Room Database
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
        productList = new ArrayList<>();
//        Searchview
        android.support.v7.widget.SearchView searchProduct = view.findViewById(R.id.search_item);
        searchProduct.setOnQueryTextListener(this);
        searchProduct.setIconified(true);
        searchProduct.setQueryHint(getString(R.string.search_product));
        // call the method to load data
        loadInventoryData();
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        return view;



    }

    // Load Inventory data for a specific authenticated System-Admin
    public void loadInventoryData() {
       /* final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Data loading...");
        pd.show();
*/
        StringRequest inventoryReqeust = new StringRequest(Request.Method.POST, Routes.setUrl("loadProduct"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                pd.dismiss();
                    try {
//                        for ROOM DB
                        Inventories inventory = new Inventories();
//                        Fetch all inventories from ROOM
                        List<Inventories> inventories = InvoiceFragment.posDatabase.myDao().getInventories(sharedPreferences.getInt("spCompId", 0));

//                        fetch data of inventories from server
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int pId = jsonObject.getInt("item_id");
                            String pImage = jsonObject.getString("item_image");
                            String pName = jsonObject.getString("item_name");
                            Double pSellPrice = jsonObject.getDouble("sell_price");
                            int pQty = jsonObject.getInt("quantity");

//                            Set data into inventories of ROOM from server
                                inventory.setCompId(sharedPreferences.getInt("spCompId", 0));
                                inventory.setProductId(pId);
                                inventory.setProductImage(pImage);
                                inventory.setProductName(pName);
                                inventory.setSellPrice(pSellPrice);
                                inventory.setQty(pQty);
                                InvoiceFragment.posDatabase.myDao().insertIntoInventories(inventory);

                        }
                        for (Inventories inv : inventories) {
                            int id = inv.getProductId();
                            String image = inv.getProductImage();
                            String product = inv.getProductName();
                            double sellPrice = inv.getSellPrice();
                            int qty = inv.getQty();
                            InventoryDataModal dataModal = new InventoryDataModal(id, image, product, sellPrice, qty);
                            productList.add(dataModal);

                        }
                        adapter = new InventoryAdapter(productList, getContext());
                        inventoryRv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(sharedPreferences.getInt("spCompId", 0)));
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(inventoryReqeust);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String name) {
        String userInput = name.toLowerCase();

        List<InventoryDataModal> newList = new ArrayList<>();
        for (InventoryDataModal idm : productList) {
            if ((idm.getProductName().toLowerCase().contains(userInput))) {
                newList.add(idm);
            }
        }
        adapter.onUpdateList(newList);
        return true;
    }
}
