package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.CategoryEditAdapter;
import com.xamuor.cashco.Category;
import com.xamuor.cashco.Model.CategoryDataModal;
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
public class CategoryEditFragment extends Fragment {
    public static PosDatabase posDatabase;
    private RecyclerView rvForCategoryEdit;
    private CategoryEditAdapter adapter;
    private List<CategoryDataModal> ctgList;
    private SharedPreferences ctgEditSp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_category_edit, container, false);
//    Define widgets
       rvForCategoryEdit = view.findViewById(R.id.rv_list_category_for_edit);
       ctgList = new ArrayList<>();
       ctgEditSp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
       rvForCategoryEdit.setHasFixedSize(true);
       rvForCategoryEdit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();

       loadCategories();
//        Edit Category
      /*  btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ctgID = CategoryRelatedFragment.ctgId;
                String ctgName = editCtgName.getText().toString();
                String ctgDesc = editCtgDes.getText().toString();
                if (ctgName.isEmpty()) {
                    editCtgName.setError("Category name required.");
                } else {
                    onEditCategory(ctgID, ctgName, ctgDesc);
                }
            }
        });*/
//        Cancel Editing category
        /*btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryDataModal modal = new CategoryDataModal(CategoryRelatedFragment.ctgId, CategoryRelatedFragment.ctgName, CategoryRelatedFragment.ctgDesc);
                CategoryRelatedFragment.onRefreshCategoryRelated(modal, getContext());
            }
        });*/
    return view;
    }
// Send data to SERVER to edit category
    /*private void onEditCategory(final int id, final String name, @Nullable final String desc) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editCategory"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    editCtgName.getText().clear();
                    editCtgDes.getText().clear();
                    Toast.makeText(getContext(), "Category changed successfully!", Toast.LENGTH_SHORT).show();
                    onRefreshCategoryFragment();
                } else if (response.trim().equals("fail")) {
                    Toast.makeText(getContext(), "Sorry, category not change, please, try again.", Toast.LENGTH_SHORT).show();
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
                map.put("ctgId", String.valueOf(id));
                map.put("ctgName", name);
                map.put("ctgDesc", desc);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }*/

//    Refresh CategoryFragment
    private void onRefreshCategoryFragment() {
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoriesFragment myfragment = new CategoriesFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
        fragmentTransaction.commit();
    }

// Load categories from ROOM
    private void loadCategories() {

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
                           /* if (getActivity() != null) {
                                categoryList.add(getResources().getString(R.string.spn_categories));
                            }*/
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
/*//              Set categories in SPINNER from ROOM
                            for (Category ctg : existingCategories) {
                                int rCtgId = ctg.getCategoryID();
                                String rCtgName = ctg.getCategoryName();
                                categoryList.add(rCtgName);
                            }
                            if (getActivity() != null) {
                                ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
                                spnCategory.setAdapter(customerAdapter);
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
                    map.put("compId", String.valueOf(ctgEditSp.getInt("spCompId", 0)));
                    return map;
                }
            };
            Volley.newRequestQueue(getContext()).add(selectCustomerRequest);





        List<Category> category = posDatabase.myDao().getCategories(ctgEditSp.getInt("spCompId", 0));

        for (Category ctg : category) {
            int id = ctg.getCategoryID();
            String name = ctg.getCategoryName();
            String desc = ctg.getCategoryDesc();
            CategoryDataModal modal = new CategoryDataModal(id, name, desc);
            ctgList.add(modal);
        }

        adapter = new CategoryEditAdapter(getContext(), ctgList);
        rvForCategoryEdit.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
