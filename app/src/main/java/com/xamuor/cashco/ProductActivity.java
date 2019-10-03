package com.xamuor.cashco;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xamuor.cashco.cashco.R;

import java.util.Objects;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    
    private TextView txtProductsTab, txtCategoriesTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

/*------------------------------ Define widgets here --------------------------------*/
        txtProductsTab = findViewById(R.id.txt_products_tab);
        txtCategoriesTab = findViewById(R.id.txt_categories_tab);
/*------------------------------ /.Define widgets here --------------------------------*/

        txtProductsTab.setOnClickListener(this);
        txtCategoriesTab.setOnClickListener(this);




//   back button in actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

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

//    Load Fragment Products
    private void loadProducts() {
        txtProductsTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
        txtCategoriesTab.setBackgroundResource(R.color.bg_tabs);
        txtProductsTab.setTextColor(getResources().getColor(R.color.txt_selected_tab));
        txtCategoriesTab.setTextColor(getResources().getColor(R.color.text_color));
    }

//    Load Fragment Categories
    private void loadCategories() {
        txtCategoriesTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
        txtProductsTab.setBackgroundResource(R.color.bg_tabs);
        txtCategoriesTab.setTextColor(getResources().getColor(R.color.txt_selected_tab));
        txtProductsTab.setTextColor(getResources().getColor(R.color.text_color));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_products_tab:
                loadProducts();
                break;
            case R.id.txt_categories_tab:
                loadCategories();
                break;
        }
    }
}
