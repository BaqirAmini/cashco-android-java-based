package com.xamuor.cashco.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.xamuor.cashco.Model.ProductEditDataModal;
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position % 2 != 0) {
            /*holder.txtproduct.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtInventory.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtRetailPrice.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtCreated.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtUpdated.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.imgEdit.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));*/
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
        }



        ProductEditDataModal modal = prdList.get(position);
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

        public ViewHolder(View itemView) {
            super(itemView);

            txtproduct = itemView.findViewById(R.id.txt_product_for_edit);
            txtInventory = itemView.findViewById(R.id.txt_inventory);
            txtRetailPrice = itemView.findViewById(R.id.txt_retail_price);
            txtCreated = itemView.findViewById(R.id.txt_created);
            txtUpdated = itemView.findViewById(R.id.txt_updated);
            circlimgProduct = itemView.findViewById(R.id.img_product);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}
