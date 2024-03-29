package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.CategoryAdapter;
import com.xamuor.cashco.Model.CategoryDataModal;
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
public class CategoriesFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView categoryRV;
    private List<CategoryDataModal> ctgList;
    private CategoryAdapter adapter;
    private SharedPreferences ctgSp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categoryRV = view.findViewById(R.id.category_rv);
        ctgList = new ArrayList<>();
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new GridLayoutManager(getContext(), 5));
//        Call method to load categories

        //        Searchview
        android.support.v7.widget.SearchView searchCategory = view.findViewById(R.id.search_category);
        searchCategory.setOnQueryTextListener(this);
        searchCategory.setIconified(true);
        searchCategory.setQueryHint(getString(R.string.search_category));

        loadCategories();

//        Define sharePreferences
        ctgSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        return view;
    }
// Load categories from server
    private void loadCategories() {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("listCategory"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int c = 0; c < jsonArray.length(); c++) {
                        JSONObject jo = jsonArray.getJSONObject(c);
                        int ctgId = jo.getInt("ctg_id");
                        String ctgName = jo.getString("ctg_name");
                        String ctgDesc = jo.getString("ctg_desc");
                        CategoryDataModal modal = new CategoryDataModal(ctgId, ctgName, ctgDesc);
                        ctgList.add(modal);
                    }
                    adapter = new CategoryAdapter(getContext(), ctgList);
                    categoryRV.setAdapter(adapter);
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
                map.put("compId", String.valueOf(ctgSp.getInt("spCompId", 0)));
                return  map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String ctg) {
        String userInput = ctg.toLowerCase();
        List<CategoryDataModal> newList = new ArrayList<>();
        for (CategoryDataModal cdm : ctgList) {
            if ((cdm.getCtgName().toLowerCase().contains(userInput))) {
                newList.add(cdm);
            }
        }
        adapter.onUpdateList(newList);
        return true;
    }
}
