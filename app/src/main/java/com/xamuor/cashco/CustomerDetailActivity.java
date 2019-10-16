package com.xamuor.cashco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xamuor.cashco.Views.CompanyProfileFragment;
import com.xamuor.cashco.Views.NewCustomerFragment;
import com.xamuor.cashco.Views.TransactionSummaryFragment;
import com.xamuor.cashco.cashco.R;

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtTabCompProfile, txtTabTrans;
    private Intent intent;
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

        intent = getIntent();
        if (intent.getIntExtra("AddNewCustomer", 0) == 99) {
            goToNewCustFrag();
        } else {
            goToCompFrag();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_company_profile, menu);
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


//    LOAD fragment new-customer
    private void goToNewCustFrag() {

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
                toToTransFrag();
                break;
        }
    }

    //  Go to Company profile
    private void goToCompFrag() {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CompanyProfileFragment myfragment = new CompanyProfileFragment();  //your fragment
        fragmentTransaction.replace(R.id.cust_detail_flayout, myfragment);
        fragmentTransaction.commit();
    }


    //  Go to Company profile
    private void toToTransFrag() {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionSummaryFragment myfragment = new TransactionSummaryFragment();  //your fragment
        fragmentTransaction.replace(R.id.cust_trans_flayout, myfragment);
        fragmentTransaction.commit();
    }
}
