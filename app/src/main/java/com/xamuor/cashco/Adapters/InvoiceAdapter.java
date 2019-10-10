package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xamuor.cashco.InventoryActivity;
import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.Views.PaymentFragment;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;

public class InvoiceAdapter extends ArrayAdapter<InvoiceDataModal> {
    ArrayList<InvoiceDataModal> products;
    Context context;
    int myResource;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InvoiceDataModal> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.myResource = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invoice_datamodal_layout, null, true);

//     Long click to edit qty, discount, or ...
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }


        remove(position);

        InvoiceDataModal product = getItem(position);
//        Initiate widgets of invoice-data-modal
        TextView txtItemName = convertView.findViewById(R.id.txt_item_name);
        TextView txtItemQty = convertView.findViewById(R.id.txt_item_qty);
        TextView txtItemPrice = convertView.findViewById(R.id.txt_item_price);
        TextView txtItemSubtotal = convertView.findViewById(R.id.txt_item_subtotal);

//        Widgets out of invoice data modal (fragment_invoice.xml)
        final TextView txtSubtotal = ((Activity)context).findViewById(R.id.txt_val_subtotal);
        final TextView txtTax = ((Activity)context).findViewById(R.id.txt_val_tax);
        final TextView txtTotal = ((Activity)context).findViewById(R.id.txt_val_total);
        final ListView listView = ((Activity)context).findViewById(R.id.list_invoice_content);
        Button btnPayInvoice = ((Activity)context).findViewById(R.id.btn_pay_invoice);


        btnPayInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                double subTotal = Double.parseDouble(txtSubtotal.getText().toString());
                onPay();
            }
        });

//        define values inside widgets from invoiceDataModal
        if (product != null) {
            txtItemQty.setText(product.getProductQty()+"");
        }
        if (product != null) {
            txtItemName.setText(product.getProductName());
        }
        if (product != null) {
            txtItemPrice.setText(product.getProductPrice() + "");
        }
        if (product != null) {
            txtItemSubtotal.setText(product.getProductSubtotal() + "");
        }

       /* final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(new ListViewAdapter(invoiceListView),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter recyclerView, int position) {
                        InvoiceAdapter.remove(position);
                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(context, "Position " + i, LENGTH_SHORT).show();
                }
            }

        });*/
//        txtSubtotal.setText("$"+ Objects.requireNonNull(product).getProductSubtotal());
       /* txtTax.setText("$" + 10);
        double tax = 10 * product.getProductSubtotal() / 100;
        txtTotal.setText("$" + (product.getProductSubtotal() + tax));*/
        return convertView;
    }

//    Take data and go to payment fragment
    private void onPay() {
//        Hide labels of tabs at the top
        TextView txtItems, txtCategories, txtKeyboard;
        txtItems = ((Activity)context).findViewById(R.id.txt_products);
        txtCategories = ((Activity)context).findViewById(R.id.txt_categories);
        txtKeyboard = ((Activity)context).findViewById(R.id.txt_keyboard);
        txtItems.setVisibility(View.GONE);
        txtCategories.setVisibility(View.GONE);
        txtKeyboard.setVisibility(View.GONE);


        FragmentManager fragmentManager =  ((InventoryActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PaymentFragment myfragment = new PaymentFragment();  //your fragment
        fragmentTransaction.replace(R.id.frg_product, myfragment);
        fragmentTransaction.commit();
    }

    public void remove(int position) {
    }

    /*private void onSwipePosition(View v) {

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(new ListViewAdapter(invoiceListView),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter recyclerView, int position) {
                        adapter.remove(position);
                    }
                });
        v.setOnTouchListener(touchListener);
        v.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(context, "Position " + i, LENGTH_SHORT).show();
                }
            }

        });
    }*/
}
