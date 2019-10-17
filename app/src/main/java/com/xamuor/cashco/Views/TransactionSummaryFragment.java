package com.xamuor.cashco.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xamuor.cashco.cashco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionSummaryFragment extends Fragment implements View.OnClickListener {
    private Button btnEditCompProfile, btnSaveCompProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_summary, container, false);
        btnEditCompProfile = getActivity().findViewById(R.id.btn_enable_edit_comp_profile);
        btnSaveCompProfile = getActivity().findViewById(R.id.btn_edit_comp_profile);

        btnSaveCompProfile.setVisibility(View.GONE);
        btnEditCompProfile.setVisibility(View.GONE);




        gotoFragTransactionDetail();
        return view;
    }



    //  Go to Company profile
    private void gotoFragTransactionDetail() {
        FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionDetailFragment myfragment = new TransactionDetailFragment();  //your fragment
        fragmentTransaction.replace(R.id.cust_trans_flayout, myfragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {

    }
}
