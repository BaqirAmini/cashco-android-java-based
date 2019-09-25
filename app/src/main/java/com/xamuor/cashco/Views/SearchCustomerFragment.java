package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.SearchCustomerAdapter;
import com.xamuor.cashco.Customer;
import com.xamuor.cashco.Model.CategoryDataModal;
import com.xamuor.cashco.Model.SearchCustomerDataModal;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCustomerFragment extends Fragment implements SearchView.OnQueryTextListener  {
    public static PosDatabase posDatabase;
    private ListView listViewSearchCustomer;
    private SearchCustomerDataModal searchCustomerDataModal;
    public SearchCustomerAdapter adapter;
    private List<SearchCustomerDataModal> customerList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_customer, container, false);

        /* ---------------------------------------- Initialize WIDGETS ------------------------------*/
        listViewSearchCustomer = view.findViewById(R.id.lv_customer_search_result);
        /* ----------------------------------------/. Initialize WIDGETS ------------------------------*/
        /* ----------------------------------------/. Initialize WIDGETS ------------------------------*/

        /* ------------------------------ SearchView for CUSTOMERS ----------------------------------- */
//     Searchview
        android.support.v7.widget.SearchView searchCustomer = view.findViewById(R.id.search_customer);
        searchCustomer.setOnQueryTextListener(this);
        searchCustomer.setIconified(false);
        searchCustomer.setQueryHint(getString(R.string.search_customer));
        /* ------------------------------ /. SearchView for CUSTOMERS ----------------------------------- */

/* ------------------------------------------- ROOM Database ----------------------------------------*/
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
/* -------------------------------------------/. ROOM Database ----------------------------------------*/
// Call listCustomers()
        listCustomers();
        return view;
    }


//    Fetch customers from server
    private void listCustomers() {
     SearchCustomerFragment.posDatabase.myDao().deleteCustomer();
        StringRequest selectCustomerRequest = new StringRequest(Request.Method.POST, Routes.setUrl("listCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             SearchCustomerFragment.posDatabase.myDao().deleteCustomer();
                if (response.trim().contains("not found")) {
                    Toast.makeText(getContext(), "Sorry, no customer existing, please register first.", Toast.LENGTH_LONG).show();
                } else {
                    try {
//                     Json-array for json data from server
                        JSONArray customers = new JSONArray(response);

                        customerList = new ArrayList<>();

//                     Customer object to insert data into ROOM
                     Customer customer = new Customer();

//                     Existing customers into ROOM database
                     List<Customer> existingCustomer = SearchCustomerFragment.posDatabase.myDao().getCustomers(Users.getCompanyId());
//                      First Delete previous customers
                       /* if (getActivity() != null) {
                            customerList.add(getResources().getString(R.string.spn_choose_customer));
                        }*/
                        for (int c = 0; c < customers.length(); c++) {
                            JSONObject custObject = customers.getJSONObject(c);
                            int custID = custObject.getInt("cust_id");
                            String custPhoto = custObject.getString("cust_photo");
                            String custBN = custObject.getString("business_name");
                            String custPhone = custObject.getString("cust_phone");
                            String custName = custObject.getString("cust_name");
                            String custLastname = custObject.getString("cust_lastname");
                            String sellerPermitNumber = custObject.getString("SellerPermitNumber");

                            searchCustomerDataModal = new SearchCustomerDataModal(custPhoto, custBN, custPhone, custName, custLastname, sellerPermitNumber);
                            customerList.add(searchCustomerDataModal);
//                            customerList.add(String.valueOf(custID));
//                         set json-values into ROOM
                                customer.setCompanyId(Users.getCompanyId());
                                customer.setCustomerId(custID);
                                customer.setCustomerName(custName);
                                customer.setCustomerLastName(custLastname);
                                customer.setCustomerPhone(custPhone);
                                customer.setBusinessName(custBN);
                                customer.setCustomerPhoto(custPhoto);
                                SearchCustomerFragment.posDatabase.myDao().insertCustomer(customer);
                        }
                     /*for (Customer cust : existingCustomer) {
                            int rCustId = cust.getCustomerId();
                            String rSellerPermitNumber = cust.getSellerPermitNumber();
                            String rCustName = cust.getCustomerName();
                            String rBN = cust.getBusinessName();
                            String rCustLastName = cust.getCustomerLastName();
                            String rPhone = cust.getCustomerPhone();
                            String rPhoto = cust.getCustomerPhoto();

                         searchCustomerDataModal = new SearchCustomerDataModal(rPhoto, rBN, rCustName, rCustLastName, rSellerPermitNumber);
                            customerList.add(searchCustomerDataModal);
                        }*/

//                 Populate SearchCustomerAdapter from ROOD DB
                adapter = new SearchCustomerAdapter(getContext(), R.layout.search_customer_datamodal, customerList);
                listViewSearchCustomer.setAdapter(adapter);
                listViewSearchCustomer.deferNotifyDataSetChanged();
                    /* if (getActivity() != null) {
                         ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, customerList);
                         spnCustomers.setAdapter(customerAdapter);
                         customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     }*/
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
                map.put("compId", Users.getCompanyId() + "");
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(selectCustomerRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String customer) {
        String userInput = customer.toLowerCase();
        List<SearchCustomerDataModal> newList = new ArrayList<>();
        for (SearchCustomerDataModal scm : customerList) {
            if ((scm.getfName().toLowerCase().contains(userInput))) {
                newList.add(scm);
            }
        }
        adapter.onUpdateList(newList);
        return true;
    }
}