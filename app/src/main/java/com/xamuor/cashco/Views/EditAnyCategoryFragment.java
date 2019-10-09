package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditAnyCategoryFragment extends Fragment {
    private EditText editCtgName, editCtgDesc;
    private LinearLayout categoriesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_any_category, container, false);

//         Define widgets
        editCtgDesc = view.findViewById(R.id.edit_category_desc);
        editCtgName = view.findViewById(R.id.edit_category_name);
        categoriesLayout = view.findViewById(R.id.layout_for_edit_products);
        return view;
    }


    //    Now update the category
    private void onUpdateCategory() {
        Bundle bundle = getArguments();


        final int cid;
        final String ctgName, ctgDesc;
        if (editCtgName.getText().toString().isEmpty()) {
            editCtgName.setError(getString(R.string.prd_name_required));
        } else {

            cid = bundle.getInt("ctgID");
            ctgName = editCtgName.getText().toString();
            ctgDesc = editCtgDesc.getText().toString();

//            Category selected in SPINNER
            StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editCategory"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Toast.makeText(getActivity(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
                        categoriesLayout.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getActivity(), "Sorry, category not updated!", Toast.LENGTH_SHORT).show();
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
                    map.put("ctgId", String.valueOf(cid));
                    map.put("ctgName", ctgName);
                    map.put("ctgDesc", ctgDesc);
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
        }
    }

}
