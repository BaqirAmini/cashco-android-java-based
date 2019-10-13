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

import com.xamuor.cashco.Model.CategoryDataModal;
import com.xamuor.cashco.ProductActivity;
import com.xamuor.cashco.Views.EditAnyCategoryFragment;
import com.xamuor.cashco.cashco.R;

import java.util.List;

public class CategoryEditAdapter extends RecyclerView.Adapter<CategoryEditAdapter.ViewHolder> {
    private Context context;
    private List<CategoryDataModal> ctgList;

    public CategoryEditAdapter(Context context, List<CategoryDataModal> prdList) {
        this.context = context;
        this.ctgList = prdList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_for_edit_data_modal, parent, false);
        return new CategoryEditAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryDataModal modal = ctgList.get(position);
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
                goToEditFrg(modal.getCtgId(), modal.getCtgName(), modal.getCtgDesc());
                holder.btnSave.setVisibility(View.VISIBLE);
            }
        });


        holder.txtCategory.setText(modal.getCtgName());
        holder.txtDesc.setText(modal.getCtgDesc().replace("null", ""));
        holder.txtDate.setText("10/8/2019");

    }

    @Override
    public int getItemCount() {
        return ctgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCategory, txtDesc, txtDate;
        private ImageView imgEdit;
        private TableRow trCategories;
        private Button btnSave;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.txt_category_for_edit);
            txtDesc = itemView.findViewById(R.id.txt_category_desc);
            txtDate = itemView.findViewById(R.id.txt_category_date);
            imgEdit = itemView.findViewById(R.id.img_edit_category);

//            btn SAVE to save changes after an item is edited
            btnSave = ((Activity)context).findViewById(R.id.btn_save_edited_category);

        }
    }

// Go to Product Edit Fragment
private void goToEditFrg(int id, String ctg, String desc) {

//    Send data to edit product items
    Bundle bundle = new Bundle();
    bundle.putInt("ctgID", id);
    bundle.putString("ctgName", ctg);
    bundle.putString("ctgDesc", desc);

    android.support.v4.app.FragmentManager fragmentManager = ((ProductActivity) context).getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    EditAnyCategoryFragment eaf = new EditAnyCategoryFragment();  //your fragment
    eaf.setArguments(bundle);
    // work here to add, remove, etc
    fragmentTransaction.replace(R.id.prd_floyout1, eaf);
    fragmentTransaction.commit();
}
}
