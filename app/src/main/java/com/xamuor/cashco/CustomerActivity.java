package com.xamuor.cashco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.CustomerAdapter;
import com.xamuor.cashco.Model.CustomerDataModal;
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

public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //    Define widegets
    private RecyclerView customerRv;
    private CustomerAdapter adapter;
    private List<CustomerDataModal> custList;
    private SwitchCompat switchCustStatus;
    private SharedPreferences customerSp;
    private FloatingActionButton fabNewCustomer;
//    Only this is globally defined
    private double custBalance = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        customerRv = findViewById(R.id.rv_customers);
        customerRv.setHasFixedSize(true);
        customerRv.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        fabNewCustomer = findViewById(R.id.fab_new_customer);
        
        fabNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailActivity();
            }
        });
//        Initiating widgets...
//        switchCustStatus = findViewById(R.id.switch_cust_status);
        // calling the method for fetching customer-data
        custList = new ArrayList<>();
        loadCustomerData();

//        back button in actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        android.support.v7.widget.SearchView searchCategory = findViewById(R.id.search_customer_and_places);
        searchCategory.setOnQueryTextListener(this);
        searchCategory.setIconified(true);
        searchCategory.setQueryHint(getString(R.string.search_customers_and_places));

//        Define sharePreferences
        customerSp = getSharedPreferences("login", Context.MODE_PRIVATE);

    }

    // load customer-data from server
    private void loadCustomerData() {
        int cStatus = 0;
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("listCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int c = 0; c < jsonArray.length(); c++) {
                        JSONObject anyItem = jsonArray.getJSONObject(c);
                        String sellerPermit = anyItem.getString("seller");
                        int custId = anyItem.getInt("custId");
                        String businessName = anyItem.getString("bn");
                        String custFname = anyItem.getString("custName");
                        String custLname = anyItem.getString("custLastname");
                        String custPhone = anyItem.getString("phone");
                        String email = anyItem.getString("email");
                        String country = anyItem.getString("country");
                        String state = anyItem.getString("state");
                        String address1 = anyItem.getString("address1");
                        String address2 = anyItem.getString("address2");
                        String city = anyItem.getString("city");
                        String zipCode = anyItem.getString("zipCode");
                        String createdAt = anyItem.getString("createdAt");
                        CustomerDataModal dataModal = new CustomerDataModal(custId, sellerPermit, businessName, custFname, custLname, custPhone, email, country,
                                state, city, zipCode, address1, address2, createdAt);
                        custList.add(dataModal);
                    }
                    adapter = new CustomerAdapter(getBaseContext(), custList);
                    customerRv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder d = new AlertDialog.Builder(getBaseContext());
                d.setMessage(error.toString());
                d.show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(customerSp.getInt("spCompId", 0)));
                return map;
            }
        };

//        Policy for timeoutError
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getBaseContext()).add(request);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String customer) {
        String userInput = customer.toLowerCase();
        List<CustomerDataModal> newList = new ArrayList<>();
        for (CustomerDataModal cd : custList) {
            if ((cd.getCustFname().toLowerCase().contains(userInput)) || (cd.getCustPhone().toLowerCase().contains(userInput)) ||
                    (cd.getBusinessName().toLowerCase().contains(userInput))) {
                newList.add(cd);
            }
        }
        adapter.onUpdateList(newList);
        return true;
    }


//    Go to edit customer
    private void goToDetailActivity() {
        Intent intent = new Intent(this, CustomerDetailActivity.class);
        intent.putExtra("AddNewCustomer", 99);
        startActivity(intent);
    }
}
