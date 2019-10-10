package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xamuor.cashco.InventoryActivity;
import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.Utilities.PosDatabase;
import com.xamuor.cashco.Views.InvoiceFragment;
import com.xamuor.cashco.Views.PaymentFragment;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.Objects;

public class InvoiceAdapter extends ArrayAdapter<InvoiceDataModal> {
    ArrayList<InvoiceDataModal> products;
    SharedPreferences invSp;
    Context context;
    int myResource;
    private int initialQty;
    private double totalValue = 0;
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
            final InvoiceDataModal product = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invoice_datamodal_layout, null, true);

//     Long click to edit qty, discount, or ...
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    openInvoiceDialog(product.getProductName(), product.getProductQty(), product.getProductPrice(), product.getProductSubtotal(), product.getInvoiceID());
                    return true;
                }
            });


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

            //        Initiate widgets of invoice-data-modal
            TextView txtItemName = convertView.findViewById(R.id.txt_item_name);
            TextView txtItemQty = convertView.findViewById(R.id.txt_item_qty);
            TextView txtItemPrice = convertView.findViewById(R.id.txt_item_price);
            TextView txtItemSubtotal = convertView.findViewById(R.id.txt_item_subtotal);



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
        }


        remove(position);




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

    private void openInvoiceDialog(String product, int qty, double price, double total, final int INVOICE_ID) {
        TextView txtProduct;
        final EditText editQty, editPrice, editDiscount, editTotal;
        Button btnMinus, btnPlus, btnDone, btnCancel;
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog_invoice);
        Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();

// Define widgets
        txtProduct = d.findViewById(R.id.txt_product_name);
        editQty = d.findViewById(R.id.edit_change_qty);
        editPrice = d.findViewById(R.id.edit_change_price);
        editDiscount = d.findViewById(R.id.edit_change_discount);
        editTotal = d.findViewById(R.id.edit_change_total);
        btnDone = d.findViewById(R.id.btn_done_change);
        btnCancel = d.findViewById(R.id.btn_cancel_change);
        btnPlus = d.findViewById(R.id.btn_plus);
        btnMinus = d.findViewById(R.id.btn_minus);

//    Set values in fields
        txtProduct.setText(product);
        editQty.setText(String.valueOf(qty));
        editPrice.setText(String.valueOf(price));
        editDiscount.setText(String.valueOf(0));
        editTotal.setText(String.valueOf(total));

         initialQty = Integer.parseInt(editQty.getText().toString());
// btn plus (+)
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialQty >= 1) {
                   initialQty++;
                   editQty.setText(String.valueOf(initialQty));
                    editQty.setText(String.valueOf(initialQty));

// Auto-change total
                    editQty.addTextChangedListener(new TextWatcher() {
                        int q = Integer.parseInt(editQty.getText().toString());
                        double price = Double.parseDouble(editPrice.getText().toString());

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            editTotal.setText(String.valueOf(initialQty * price));
                        }
                    });
                }
            }
        });

        // btn minus (-)
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialQty > 1) {
                    initialQty--;
                    editQty.setText(String.valueOf(initialQty));

// Auto-change total
                editQty.addTextChangedListener(new TextWatcher() {
                    int q = Integer.parseInt(editQty.getText().toString());
                    double price = Double.parseDouble(editPrice.getText().toString());

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        editTotal.setText(String.valueOf(initialQty * price));
                    }
                });
                } else {
                    Toast.makeText(context, context.getString(R.string.minimum_one), Toast.LENGTH_SHORT).show();
                }
            }
        });


// Change Price
        editPrice.addTextChangedListener(new TextWatcher() {
//            double total;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!editPrice.getText().toString().isEmpty()) {
                        double price = Double.parseDouble(editPrice.getText().toString());
                        int q = Integer.parseInt(editQty.getText().toString());
                        totalValue = price * q;
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTotal.setText(String.valueOf(totalValue));
            }
        });


// Set DISCOUNT
        editDiscount.addTextChangedListener(new TextWatcher() {
            double priceWithDiscount;
//            double total;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editDiscount.setText(String.valueOf(0));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editDiscount.getText().toString().isEmpty()) {
                    int q = Integer.parseInt(editQty.getText().toString());
                    double discountValue = Double.parseDouble(editDiscount.getText().toString());
                    double price = Double.parseDouble(editPrice.getText().toString());
                    priceWithDiscount = price - discountValue;
                    totalValue = priceWithDiscount * q;
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTotal.setText(String.valueOf(totalValue));

            }
        });

// Click btn done
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invSp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                PosDatabase posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
                int qty;
                double price;
                if (!editQty.getText().toString().isEmpty()) {
                    qty = Integer.parseInt(editQty.getText().toString());
                    price = Double.parseDouble(editPrice.getText().toString());
                    totalValue = Double.parseDouble(editTotal.getText().toString());
                    posDatabase.myDao().updateProduct(qty, price, totalValue, invSp.getInt("spCompId", 0), INVOICE_ID);
                    d.dismiss();
                    onRefreshInvoiceFragment();
                }
            }
        });

//  Click btn cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });


    }


    private void onRefreshInvoiceFragment() {
        android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InvoiceFragment myfragment = new InvoiceFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.frg_invoice, myfragment);
        fragmentTransaction.commit();
    }
}
