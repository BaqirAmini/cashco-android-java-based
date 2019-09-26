package com.xamuor.cashco.Adapters;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xamuor.cashco.Model.CategoryDataModal;
import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.Model.SearchCustomerDataModal;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchCustomerAdapter extends RecyclerView.Adapter <SearchCustomerAdapter.ViewHolder>  {
    List<SearchCustomerDataModal> customerList;
    Context context;


    public SearchCustomerAdapter(Context context, List<SearchCustomerDataModal> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

//    IT WAS WRITTEN FOR ListView but now no longer needed because it is replaced by RecyclerView
    /*@SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.search_customer_datamodal, null, true);
        }

        SearchCustomerDataModal customer = getItem(position);
//        Initiate widgets of invoice-data-modal
        ImageView imgCustPhoto = convertView.findViewById(R.id.img_customer_photo);
        TextView txtBN = convertView.findViewById(R.id.txt_cust_bn);
        TextView txtPhone = convertView.findViewById(R.id.txt_cust_phone);
        TextView txtFN = convertView.findViewById(R.id.txt_cust_fn);
        TextView txtLN = convertView.findViewById(R.id.txt_cust_ln);

*//*------------------------------- Set values into data modal ---------------------------------*//*
        txtBN.setText(Objects.requireNonNull(customer).getBusinessName());
        txtPhone.setText(Objects.requireNonNull(customer).getPhone());
        txtFN.setText(Objects.requireNonNull(customer).getfName());
        txtLN.setText(Objects.requireNonNull(customer).getlName());
*//*-------------------------------/. Set values into data modal ---------------------------------*//*



        return convertView;
    }*/

//    Update list of customers while searching customers
    public void onUpdateList(List<SearchCustomerDataModal> cList) {
        customerList = new ArrayList<>();
        customerList.addAll(cList);
        notifyDataSetChanged();
    }

    @Override
    public SearchCustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_customer_datamodal, parent, false);
        return new SearchCustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchCustomerAdapter.ViewHolder holder, int position) {
        final SearchCustomerDataModal modal = customerList.get(position);
        /* --------------------- Set values in textViews ------------------*/
        holder.txtBN.setText(modal.getBusinessName());
        holder.txtPhone.setText(modal.getPhone());
        holder.txtFN.setText(modal.getfName());
        holder.txtLN.setText(modal.getlName());
        holder.rlSearchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.custSearchView.setQuery(modal.getfName().concat(" ").concat(modal.getlName()), false);
            }
        });
        /* ---------------------/. Set values in textViews ------------------*/
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public android.support.v7.widget.SearchView custSearchView;
        private RelativeLayout rlSearchCustomer;
        private ImageView imgCustPhoto;
        private TextView txtBN, txtPhone, txtFN, txtLN;
        public ViewHolder(View itemView) {
            super(itemView);
//   Initiate widgets of invoice-data-modal
            custSearchView = ((Activity) context).findViewById(R.id.search_customer);
            rlSearchCustomer = itemView.findViewById(R.id.rl_search_customer);
            imgCustPhoto = itemView.findViewById(R.id.img_customer_photo);
            txtBN = itemView.findViewById(R.id.txt_cust_bn);
            txtPhone = itemView.findViewById(R.id.txt_cust_phone);
            txtFN = itemView.findViewById(R.id.txt_cust_fn);
            txtLN = itemView.findViewById(R.id.txt_cust_ln);
        }
    }
}
