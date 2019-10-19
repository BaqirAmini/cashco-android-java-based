package com.xamuor.cashco.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xamuor.cashco.cashco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyProfileFragment extends Fragment implements View.OnClickListener {
    private Button btnEditCompProfile, btnSaveCompProfile, btnSaveNewCust;
    private EditText editCompSeller, editCompBn, editCompPhone, editCompEmail, editCompCountry, editCompState, editCompAddr1, editCompAddr2,
            editCompCity, editCompZipcode;
    private RadioGroup rdgCompEmployee;
    private RadioButton rdbCompEmployee, rdbNotEmployee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);

//        Define widgets
        btnSaveCompProfile = getActivity().findViewById(R.id.btn_edit_comp_profile);
        btnEditCompProfile = getActivity().findViewById(R.id.btn_enable_edit_comp_profile);
        btnSaveNewCust = getActivity().findViewById(R.id.btn_save_new_customer);
        editCompSeller = view.findViewById(R.id.edit_comp_seller);
        editCompBn = view.findViewById(R.id.edit_comp_bn);
        editCompPhone = view.findViewById(R.id.edit_comp_phone);
        editCompEmail = view.findViewById(R.id.edit_comp_email);
        editCompCountry = view.findViewById(R.id.edit_comp_country);
        editCompState = view.findViewById(R.id.edit_comp_province);
        editCompAddr1 = view.findViewById(R.id.edit_comp_address1);
        editCompAddr2 = view.findViewById(R.id.edit__comp_address2);
        editCompCity = view.findViewById(R.id.edit_comp_city);
        editCompZipcode = view.findViewById(R.id.edit_comp_zip_code);
        rdgCompEmployee = view.findViewById(R.id.rdg_comp_is_employee);
        rdbCompEmployee = view.findViewById(R.id.rdb_emplyee);
        rdbNotEmployee = view.findViewById(R.id.rdb_not_employee);


        btnEditCompProfile.setVisibility(View.VISIBLE);
        btnSaveCompProfile.setVisibility(View.VISIBLE);
        btnSaveNewCust.setVisibility(View.GONE);
        btnSaveCompProfile.setOnClickListener(this);
        btnEditCompProfile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enable_edit_comp_profile:
                enableEdition();
                break;
            case R.id.btn_edit_comp_profile:
                saveChanges();
        }
    }
    
// Enable company-profile edition
    private void enableEdition() {
        btnSaveCompProfile.setEnabled(true);
        btnSaveCompProfile.setBackgroundResource(R.color.txt_selected_tab);
        btnSaveCompProfile.getResources().getColor(R.color.white_color);

//    ENABLE editTextes and radiobutons
        editCompSeller.setEnabled(true);
        editCompBn.setEnabled(true);
        editCompPhone.setEnabled(true);
        editCompEmail.setEnabled(true);
        editCompCountry.setEnabled(true);
        editCompState.setEnabled(true);
        editCompAddr1.setEnabled(true);
        editCompAddr2.setEnabled(true);
        editCompCity.setEnabled(true);
        editCompZipcode.setEnabled(true);
        rdgCompEmployee.setEnabled(true);
        rdbNotEmployee.setEnabled(true);
        rdbCompEmployee.setEnabled(true);

//        Disable itself
        btnEditCompProfile.setEnabled(false);
        btnEditCompProfile.setBackgroundResource(R.color.bg_tabs);
        btnEditCompProfile.getResources().getColor(R.color.white_color);
    }

// Save changes with company-profile
    private void saveChanges() {
//    Validate fields first
        if (editCompSeller.getText().toString().isEmpty()) {
            editCompSeller.setError(getString(R.string.seller_permit_no_required));
        }
        if (editCompBn.getText().toString().isEmpty()) {
            editCompBn.setError(getString(R.string.bn_required));
        }
        if (editCompPhone.getText().toString().isEmpty()) {
            editCompPhone.setError(getString(R.string.phone_required));
        }
        if (editCompPhone.getText().toString().length() < 10) {
            editCompPhone.setError(getString(R.string.min_phone_char));
        }

        if (editCompCountry.getText().toString().isEmpty()) {
            editCompCountry.setError(getString(R.string.country_required));
        }

        if (editCompState.getText().toString().isEmpty()) {
            editCompState.setError(getString(R.string.state_required));
        }
        if (editCompAddr1.getText().toString().isEmpty()) {
            editCompAddr1.setError(getString(R.string.addr1_required));
        }

        if (editCompCity.getText().toString().isEmpty()) {
            editCompCity.setError(getString(R.string.city_required));
        }

        if (editCompZipcode.getText().toString().length() > 4 || editCompZipcode.getText().toString().length() < 4) {
            editCompZipcode.setError(getString(R.string.zipcode_only));
        }
        
        if (!editCompSeller.getText().toString().isEmpty() && !editCompBn.getText().toString().isEmpty() && !editCompPhone.getText().toString().isEmpty() &&
        editCompPhone.getText().toString().length() >= 10 && !editCompCountry.getText().toString().isEmpty() && !editCompState.getText().toString().isEmpty() 
        && !editCompAddr1.getText().toString().isEmpty() && !editCompCity.getText().toString().isEmpty() && editCompZipcode.getText().toString().length() == 4) {
            Toast.makeText(getContext(), "Now, everything is OK.", Toast.LENGTH_SHORT).show();
        }
    }
}
