package com.xamuor.cashco.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.xamuor.cashco.Model.ProductEditDataModal;
import com.xamuor.cashco.ProductActivity;
import com.xamuor.cashco.Views.EditAnyItemFragment;
import com.xamuor.cashco.cashco.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<ProductEditDataModal> prdList;

    public ProductAdapter(Context context, List<ProductEditDataModal> prdList) {
        this.context = context;
        this.prdList = prdList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_for_edit_data_modal, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductEditDataModal modal = prdList.get(position);
        if (position % 2 != 0) {
            /*holder.txtproduct.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtInventory.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtRetailPrice.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtCreated.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtUpdated.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.imgEdit.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));*/
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white_color));
        }

//        Click any edit icon to edit an item
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditFrg(modal.getPid(), modal.getpName(), modal.getpDesc(), modal.getpInventory(), modal.getBarcode(),  modal.getpCost(), modal.getpRetailPrice());
                holder.btnSave.setVisibility(View.VISIBLE);
            }
        });


        holder.txtproduct.setText(modal.getpName());
        holder.txtInventory.setText(modal.getpInventory());
        holder.txtRetailPrice.setText(modal.getpRetailPrice());
        holder.txtCreated.setText(modal.getpCreated());
        holder.txtUpdated.setText(modal.getpUpdated());
    }

    @Override
    public int getItemCount() {
        return prdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtproduct, txtInventory, txtRetailPrice, txtCreated, txtUpdated;
        private ImageView imgEdit;
        private CircleImageView circlimgProduct;
        private TableRow trProducts;
        private Button btnSave;

        public ViewHolder(View itemView) {
            super(itemView);

            txtproduct = itemView.findViewById(R.id.txt_product_for_edit);
            txtInventory = itemView.findViewById(R.id.txt_inventory);
            txtRetailPrice = itemView.findViewById(R.id.txt_retail_price);
            txtCreated = itemView.findViewById(R.id.txt_created);
            txtUpdated = itemView.findViewById(R.id.txt_updated);
            circlimgProduct = itemView.findViewById(R.id.img_product);
            imgEdit = itemView.findViewById(R.id.img_edit);

//            btn SAVE to save changes after an item is edited
            btnSave = ((Activity)context).findViewById(R.id.btn_save_edited_product);

        }
    }

// Go to Product Edit Fragment
private void goToEditFrg(int itemId, String itemName, String itemDesc, String qty, String barcode, String cost, String retailPrice) {

//    Send data to edit product items
    Bundle bundle = new Bundle();
    bundle.putInt("item_id", itemId);
    bundle.putString("item_name", itemName);
    bundle.putString("item_desc", itemDesc);
    bundle.putString("item_qty", qty);
    bundle.putDouble("item_cost", Double.parseDouble(cost));
    bundle.putDouble("item_retail_price", Double.parseDouble(retailPrice));
    bundle.putString("item_barcode", barcode);

    android.support.v4.app.FragmentManager fragmentManager = ((ProductActivity) context).getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    EditAnyItemFragment eaf = new EditAnyItemFragment();  //your fragment
    eaf.setArguments(bundle);
    // work here to add, remove, etc
    fragmentTransaction.replace(R.id.prd_floyout1, eaf);
    fragmentTransaction.commit();
}
}
