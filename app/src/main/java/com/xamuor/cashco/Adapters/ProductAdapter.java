package com.xamuor.cashco.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamuor.cashco.Model.ProductEditDataModal;
import com.xamuor.cashco.cashco.R;

import java.util.List;

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
        ProductEditDataModal modal = prdList.get(position);
    }

    @Override
    public int getItemCount() {
        return prdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
