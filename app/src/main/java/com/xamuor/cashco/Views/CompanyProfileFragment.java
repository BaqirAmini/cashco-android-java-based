package com.xamuor.cashco.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xamuor.cashco.cashco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyProfileFragment extends Fragment {
    private Button btnEnableEditCust, btnEditCust, btnSaveNewCust;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);

//        Define widgets
        btnEditCust = getActivity().findViewById(R.id.btn_edit_comp_profile);
        btnEnableEditCust = getActivity().findViewById(R.id.btn_enable_edit_comp_profile);
        btnSaveNewCust = getActivity().findViewById(R.id.btn_save_new_customer);

        btnEnableEditCust.setVisibility(View.VISIBLE);
        btnEditCust.setVisibility(View.VISIBLE);
        btnSaveNewCust.setVisibility(View.GONE);

        return view;
    }

}
