package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xamuor.cashco.Inventories;
import com.xamuor.cashco.InventoryActivity;
import com.xamuor.cashco.Model.InventoryDataModal;
import com.xamuor.cashco.Product;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.Views.InvoiceFragment;
import com.xamuor.cashco.Views.ProductFragment;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    public static int qty = 1;
    public int remainingQty = 0;
    private int initialQty = 0;
    private int q;
    private SharedPreferences invAdapterSp;
    private List<InventoryDataModal> productList;
    private Context context;
    private List<Inventories> inventoriesList;

    public InventoryAdapter(List<InventoryDataModal> dataModals, Context context) {
        this.productList = dataModals;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final InventoryAdapter.ViewHolder holder, final int position) {

       /* if (position % 2 == 0) {
            holder.cardItem.setCardBackgroundColor(Color.parseColor("#689ACA"));
        } else {
            holder.cardItem.setCardBackgroundColor(Color.parseColor("#FD9926"));
        }*/
       holder.cardItem.setCardBackgroundColor(context.getResources().getColor(R.color.bg_second_column));



        final InventoryDataModal modal = productList.get(position);
        holder.txtProduct.setText(modal.getProductName());
        holder.txtPrice.setText("$" + modal.getProductPrice());
//        holder.txtQty.setText(String.valueOf(modal.getProductQty()));
        Glide.with(context).load(Routes.onLoadImage("product_images/".concat(modal.getProductImage()))).into(holder.productImage);

//        See if any product qty is zero of not existing anymore
        remainingQty = /*Integer.parseInt(holder.txtQty.getText().toString())*/ 50;
        if (remainingQty <= 0) {
            Toast.makeText(context, modal.getProductName() + " not existing anymore.", Toast.LENGTH_SHORT).show();
            holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
        } else if (remainingQty <= 5) {
            holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//     Get the initial value of qty shown in the card
//                initialQty = Integer.parseInt(holder.txtQty.getText().toString());
                Product product = new Product();
                TextView txtProduct = (TextView) view.findViewById(R.id.txt_product);
                String myProduct = txtProduct.getText().toString();
                InventoryDataModal productList = new InventoryDataModal(modal.getProductId(), modal.getProductImage(), modal.getProductName(), modal.getProductPrice(), modal.getProductQty());
                InvoiceFragment.posDatabase.myDao().getProducts(invAdapterSp.getInt("spCompId", 0));
//                remainingQty--;
//               Initiate Product entity
                List<Product> existingList = InvoiceFragment.posDatabase.myDao().getItem(myProduct);
                if (existingList.size() > 0) {
//                  decrease one unit the qty on any click on the same item
                    initialQty--;
//                    update qty in inventories table in ROOM db with decremented value
                    InvoiceFragment.posDatabase.myDao().updateInventory(initialQty, invAdapterSp.getInt("spCompId", 0), modal.getProductId());
//                  Fetch back the update qty from inventories in ROOM db
                    remainingQty = InvoiceFragment.posDatabase.myDao().getQty(invAdapterSp.getInt("spCompId", 0), modal.getProductId());
                    if (remainingQty == 5) {
                        holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
                    } else if (remainingQty == 0) {
                        AlertDialog.Builder d = new AlertDialog.Builder(context);
                        d.setMessage("Sorry, " + modal.getProductName() + " has been finished!");
                        holder.itemView.setEnabled(false);
                    }
//                   Set the new qty in textview in the cards
//                    holder.txtQty.setText(remainingQty+"");

                    q = ++qty;
                    InvoiceFragment.posDatabase.myDao().updateItem(q, invAdapterSp.getInt("spCompId", 0), myProduct);
//                    holder..setText(remainingQty+"");

                    onRefreshInvoiceFragment();
                } else {
//                  decrease one unit the qty on any click on the same item
                    initialQty--;
//                    update qty in inventories table in ROOM db with decremented value
                    InvoiceFragment.posDatabase.myDao().updateInventory(initialQty, invAdapterSp.getInt("spCompId", 0), modal.getProductId());
//                  Fetch back the update qty from inventories in ROOM db
                    remainingQty = InvoiceFragment.posDatabase.myDao().getQty(invAdapterSp.getInt("spCompId", 0), modal.getProductId());
//                   Set the new qty in textview in the cards
//                    holder.txtQty.setText(remainingQty+"");

/* Print values in the invoice when an item is clicked */
                    qty = 1;
                    product.setAdapterPos(holder.getAdapterPosition());
                    product.setProductId(productList.getProductId());
                    product.setCompanyId(invAdapterSp.getInt("spCompId", 0));
                    product.setProductQty(qty);
                    product.setProductName(productList.getProductName());
                    product.setProductPrice(productList.getProductPrice());
                    ProductFragment.posDatabase.myDao().insert(product);
                    onRefreshInvoiceFragment();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtProduct, txtPrice, txtQty;
        CardView cardItem;


        public ViewHolder(View itemView) {
            super(itemView);
//                by default is hidden, when an item clicked it gets visible
            InvoiceFragment invoiceFragment = new InvoiceFragment();
            productImage = itemView.findViewById(R.id.img_product);
//            Rectify txt_product & txt_price in strings.xml because it is DUPLICATE.
            txtProduct = itemView.findViewById(R.id.txt_product);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtQty = itemView.findViewById(R.id.txt_qty);
            cardItem = itemView.findViewById(R.id.cv_product);
            invAdapterSp = context.getSharedPreferences("login", Context.MODE_PRIVATE);

/*//            Set permission for cashier
            if (Users.getRole().equalsIgnoreCase("cashier")) {
                itemView.setEnabled(false);

            }*/

        }
    }

    //    To change array-list based on query input
    public void onUpdateList(List<InventoryDataModal> newList) {
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    //    To refresh the invoiceFragment
    private void onRefreshInvoiceFragment() {
       /* FragmentManager fragmentManager =  ()getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchCustomerFragment myfragment = new SearchCustomerFragment();  //your fragment
        fragmentTransaction.replace(R.id.frg_search_customer, myfragment);
        fragmentTransaction.commit();*/



        android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InvoiceFragment myfragment = new InvoiceFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.frg_invoice, myfragment);
        fragmentTransaction.commit();
    }
}
