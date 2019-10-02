package com.xamuor.cashco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xamuor.cashco.Views.CategoriesFragment;
import com.xamuor.cashco.Views.InvoiceFragment;
import com.xamuor.cashco.Views.ProductFragment;
import com.xamuor.cashco.cashco.R;

public class InventoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Intent testIntent;
    Context context = InventoryActivity.this;
    private DrawerLayout drawer;
    private ImageView imgNav;
    private TextView txtNavName, txtNavRole, txtParkTab, txtVoidTab, txtProductsTab, txtCategoriesTab, txtKeyboardTab;
    private MenuItem menuCustomer, menuInventory, menuCategory, menuReport, menuSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Initiate TextViews
        txtParkTab = findViewById(R.id.txt_park);
        txtVoidTab = findViewById(R.id.txt_void);
        txtProductsTab = findViewById(R.id.txt_products);
        txtCategoriesTab = findViewById(R.id.txt_categories);
        txtKeyboardTab = findViewById(R.id.txt_keyboard);
//       Handle tabs (below navbar)
//        Tab Void
        txtVoidTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtVoidTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtProductsTab.setBackgroundResource(R.color.bg_tabs);
                txtKeyboardTab.setBackgroundResource(R.color.bg_tabs);
                txtCategoriesTab.setBackgroundResource(R.color.bg_tabs);
                txtParkTab.setBackgroundResource(R.color.bg_tabs);

                txtVoidTab.setTextColor(Color.parseColor("#6200EE"));
                txtKeyboardTab.setTextColor(getResources().getColor(R.color.text_color));
                txtCategoriesTab.setTextColor(getResources().getColor(R.color.text_color));
                txtProductsTab.setTextColor(getResources().getColor(R.color.text_color));
                txtParkTab.setTextColor(getResources().getColor(R.color.text_color));
            }
        });


//        Tab Park
        txtParkTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtParkTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtProductsTab.setBackgroundResource(R.color.bg_tabs);
                txtKeyboardTab.setBackgroundResource(R.color.bg_tabs);
                txtVoidTab.setBackgroundResource(R.color.bg_tabs);
                txtCategoriesTab.setBackgroundResource(R.color.bg_tabs);
// Text Color
                txtParkTab.setTextColor(Color.parseColor("#6200EE"));
                txtKeyboardTab.setTextColor(getResources().getColor(R.color.text_color));
                txtCategoriesTab.setTextColor(getResources().getColor(R.color.text_color));
                txtProductsTab.setTextColor(getResources().getColor(R.color.text_color));
                txtVoidTab.setTextColor(getResources().getColor(R.color.text_color));
            }
        });

//        TAB Products
        txtProductsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Products Clicked", Toast.LENGTH_SHORT).show();
                ProductFragment pf = new ProductFragment();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frg_product, pf, "Product").commit();
                txtProductsTab.setTextColor(Color.parseColor("#6200EE"));
                txtKeyboardTab.setTextColor(getResources().getColor(R.color.text_color));
                txtCategoriesTab.setTextColor(getResources().getColor(R.color.text_color));
                txtVoidTab.setTextColor(getResources().getColor(R.color.text_color));
                txtParkTab.setTextColor(getResources().getColor(R.color.text_color));
                txtProductsTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtCategoriesTab.setBackgroundResource(R.color.bg_tabs);
                txtKeyboardTab.setBackgroundResource(R.color.bg_tabs);
                txtVoidTab.setBackgroundResource(R.color.bg_tabs);
                txtParkTab.setBackgroundResource(R.color.bg_tabs);
            }
        });

//        TAB Categories
        txtCategoriesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Products Clicked", Toast.LENGTH_SHORT).show();
                CategoriesFragment cf = new CategoriesFragment();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frg_product, cf, "Category").commit();
                txtCategoriesTab.setTextColor(Color.parseColor("#6200EE"));
                txtProductsTab.setTextColor(getResources().getColor(R.color.text_color));
                txtKeyboardTab.setTextColor(getResources().getColor(R.color.text_color));
                txtVoidTab.setTextColor(getResources().getColor(R.color.text_color));
                txtParkTab.setTextColor(getResources().getColor(R.color.text_color));

                txtCategoriesTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtProductsTab.setBackgroundResource(R.color.bg_tabs);
                txtKeyboardTab.setBackgroundResource(R.color.bg_tabs);
                txtVoidTab.setBackgroundResource(R.color.bg_tabs);
                txtParkTab.setBackgroundResource(R.color.bg_tabs);
            }
        });

//        TAB Keyboard
        txtKeyboardTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtKeyboardTab.setTextColor(Color.parseColor("#6200EE"));
                txtProductsTab.setTextColor(getResources().getColor(R.color.text_color));
                txtCategoriesTab.setTextColor(getResources().getColor(R.color.text_color));
                txtVoidTab.setTextColor(getResources().getColor(R.color.text_color));
                txtParkTab.setTextColor(getResources().getColor(R.color.text_color));

                txtKeyboardTab.setBackground(getResources().getDrawable(R.drawable.tab_design));
                txtProductsTab.setBackgroundResource(R.color.bg_tabs);
                txtCategoriesTab.setBackgroundResource(R.color.bg_tabs);
                txtVoidTab.setBackgroundResource(R.color.bg_tabs);
                txtParkTab.setBackgroundResource(R.color.bg_tabs);
            }
        });
        
//        Add the two fragments
        ProductFragment pf = new ProductFragment();
        InvoiceFragment inf = new InvoiceFragment();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frg_invoice, inf, "Invoice").commit();
        fm.beginTransaction().replace(R.id.frg_product, pf, "Product").commit();
        fm.popBackStack();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        navigationView.setItemIconTintList(null);
//        Access menu-options in navigation-drawer
      /*  menuCustomer = menu.findItem(R.id.nav_customer);
        menuInventory = menu.findItem(R.id.nav_inventory);
        menuCategory = menu.findItem(R.id.nav_category);
        menuReport = menu.findItem(R.id.nav_report);
        menuSettings = menu.findItem(R.id.nav_setting);*/
//        Set permission for three roles
        if (Users.getRole().equalsIgnoreCase("stock manager")) {
            menuCustomer.setVisible(false);
            menuReport.setVisible(false);
            menuSettings.setVisible(false);
        } else if (Users.getRole().equalsIgnoreCase("cashier")) {
            menuSettings.setVisible(false);
            menuInventory.setVisible(false);
            menuCategory.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);

//        Initiate widgets
        /*View headerView = navigationView.getHeaderView(0);
        imgNav = headerView.findViewById(R.id.img_nav_uphoto);
        txtNavName = headerView.findViewById(R.id.txt_nav_uname);
        txtNavRole = headerView.findViewById(R.id.txt_nav_urole);
        txtNavName.setText(Users.getFname().concat(" ").concat(Users.getLname()).replace("null", ""));
        txtNavRole.setText(Users.getRole());
        Glide.with(context).load(Routes.onLoadImage("user_photos/").concat(Users.getPhoto())).into(imgNav);*/
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

   /*     if (id == R.id.nav_customer) {
            testIntent = new Intent(this, DrawerMenuActivity.class);
            MenuID.setMenuId(1);
            startActivity(testIntent);
        } else if (id == R.id.nav_inventory) {
            testIntent = new Intent(this, DrawerMenuActivity.class);
            MenuID.setMenuId(2);
            startActivity(testIntent);

        } else if (id == R.id.nav_category) {
            testIntent = new Intent(this, DrawerMenuActivity.class);
            MenuID.setMenuId(3);
            startActivity(testIntent);

        } else if (id == R.id.nav_report) {
            MenuID.setMenuId(4);
            testIntent = new Intent(this, ReportActivity.class);
            startActivity(testIntent);

        } else */
        if (id == R.id.nav_customers) {
            testIntent = new Intent(this, CustomerActivity.class);
            startActivity(testIntent);
        }else if ( id == R.id.nav_log_out) {
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(context);
            logoutDialog.setTitle("Confirm Sign Out");
            logoutDialog.setMessage("Are you sure you want exit from the app?");
            logoutDialog.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent logoutInent = new Intent(context, LoginActivity.class);
                    startActivity(logoutInent);
                    finish();
                }
            });
            logoutDialog.setNegativeButton("Cancel", null);
            logoutDialog.show();
        } else if (id == R.id.nav_sale) {
            loadFrgInventory();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFrgInventory() {
        /*txtProductsTab = findViewById(R.id.txt_products);
        txtCategories = ((Activity)context).findViewById(R.id.txt_categories);
        txtKeyboard = ((Activity)context).findViewById(R.id.txt_keyboard);*/
        txtProductsTab.setVisibility(View.VISIBLE);
        txtCategoriesTab.setVisibility(View.VISIBLE);
        txtKeyboardTab.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager =  ((InventoryActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProductFragment myfragment = new ProductFragment();  //your fragment
        fragmentTransaction.replace(R.id.frg_product, myfragment);
        fragmentTransaction.commit();
    }
}
