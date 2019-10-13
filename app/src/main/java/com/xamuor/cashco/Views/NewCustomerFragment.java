package com.xamuor.cashco.Views;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamuor.cashco.cashco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCustomerFragment extends Fragment {
    private SharedPreferences newCtgSp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_customer, container, false);

        return view;
    }

// Refresh Category-fragment
    /*private void onRefershCategory() {
        //                CustomerDetailFragment to show more detail for any customer
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryEditFragment myfragment = new CategoryEditFragment();  //your fragment
        fragmentTransaction.replace(R.id.prd_floyout1, myfragment);
        fragmentTransaction.commit();
    }*/

}
