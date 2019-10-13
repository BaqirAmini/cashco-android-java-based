package com.xamuor.cashco;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xamuor.cashco.Views.NewCustomerFragment;
import com.xamuor.cashco.cashco.R;

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtTabCompProfile, txtTabTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

/*------------------------------  Define Widgets ---------------------------------*/
        txtTabCompProfile = findViewById(R.id.txt_tab_company_profile);
        txtTabTrans = findViewById(R.id.txt_tab_transactions);
        txtTabCompProfile.setOnClickListener(this);
        txtTabTrans.setOnClickListener(this);
/*------------------------------ /. Define Widgets ---------------------------------*/
        goToEditFrag();
    }


//    LOAD fragment new-customer
    private void goToEditFrag() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewCustomerFragment nf = new NewCustomerFragment();  //your fragment
//        eaf.setArguments(bundle);
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.cust_detail_flayout, nf);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_tab_company_profile:
                txtTabCompProfile.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtTabTrans.setBackgroundResource(R.color.bg_tabs);
                txtTabCompProfile.setTextColor(getResources().getColor(R.color.txt_selected_tab));
                txtTabTrans.setTextColor(getResources().getColor(R.color.text_color));
                break;
            case R.id.txt_tab_transactions:
                txtTabCompProfile.setBackgroundResource(R.color.bg_tabs);
                txtTabTrans.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtTabTrans.setTextColor(getResources().getColor(R.color.txt_selected_tab));
                txtTabCompProfile.setTextColor(getResources().getColor(R.color.text_color));
                break;
        }
    }
}
