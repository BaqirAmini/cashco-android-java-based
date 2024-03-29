package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xamuor.cashco.InventoryActivity;
import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.Utilities.PosDatabase;
import com.xamuor.cashco.Views.InvoiceFragment;
import com.xamuor.cashco.Views.PaymentFragment;
import com.xamuor.cashco.cashco.R;

import java.util.List;
import java.util.Objects;

public class InvoiceAdapter extends RecyclerView.Adapter <InvoiceAdapter.ViewHolder> {
    List<InvoiceDataModal> products;
    Context context;
    private int initialQty;
    private double totalValue = 0;
    SharedPreferences invSp;
    public InvoiceAdapter(List<InvoiceDataModal> itemList, Context context) {
        this.products = itemList;
        this.context = context;
    }

    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_datamodal_layout, parent, false);
        return new InvoiceAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(InvoiceAdapter.ViewHolder holder, int position) {
        final InvoiceDataModal product = products.get(position);

//        define values inside widgets from invoiceDataModal
        holder.txtItemQty.setText(product.getProductQty() + "");
        holder.txtItemName.setText(product.getProductName());
        holder.txtItemPrice.setText(product.getProductPrice() + "");
        holder.txtItemSubtotal.setText(product.getProductSubtotal() + "");

        holder.btnPayInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                double subTotal = Double.parseDouble(txtSubtotal.getText().toString());
                onPay();
            }
        });

//     Long click to edit qty, discount, or ...
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openInvoiceDialog(product.getProductName(), product.getProductQty(), product.getProductPrice(), product.getProductSubtotal(), product.getInvoiceID());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName;
        private TextView txtItemQty;
        private TextView txtItemPrice;
        private TextView txtItemSubtotal;
        private Button btnPayInvoice;

        public ViewHolder(View itemView) {
            super(itemView);

//        Initiate widgets of invoice-data-modal
             txtItemName = itemView.findViewById(R.id.txt_item_name);
             txtItemQty = itemView.findViewById(R.id.txt_item_qty);
             txtItemPrice = itemView.findViewById(R.id.txt_item_price);
             txtItemSubtotal = itemView.findViewById(R.id.txt_item_subtotal);
             btnPayInvoice = ((Activity)context).findViewById(R.id.btn_pay_invoice);

        }
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
                PosDatabase posDatabase = Room.databaseBuilder(context, PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
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
