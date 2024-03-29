package com.xamuor.cashco.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.xamuor.cashco.CustomerDetail;
import com.xamuor.cashco.CustomerDetailActivity;
import com.xamuor.cashco.Model.CustomerDataModal;
import com.xamuor.cashco.cashco.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private List<CustomerDataModal> custList;
    private Intent intent;
    private Context context;
    public CustomerAdapter(Context context, List<CustomerDataModal> custList) {
        this.context = context;
        this.custList = custList;
    }

    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_data_modal, parent, false);
        return new CustomerAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CustomerAdapter.ViewHolder holder, final int position) {
        final CustomerDataModal modal = custList.get(position);
        holder.txtListCustBN.setText(modal.getBusinessName());
//        holder.txtListCustID.setText(modal.getCustId() + "");
        holder.txtListCustFN.setText(modal.getCustFname());
        holder.txtListCustLN.setText(modal.getCustLname().replace("null", ""));
        holder.txtListCustPhone.setText(modal.getCustPhone().replace("null", ""));
        holder.txtListCustEmail.setText(modal.getCustEmail().replace("null", ""));
        holder.txtListCustCountry.setText(modal.getCountry().replace("null", ""));
        holder.txtListCustState.setText(modal.getCust_state().replace("null", ""));
        holder.txtListCustAddr.setText(modal.getAddress1().replace("null", ""));
        holder.txtListCustRegDate.setText(modal.getRegDate().replace("null", ""));

//        Set background-color for rows
        if (position % 2 == 0) {
            /*holder.txtListCustBN.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustID.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustFN.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustLN.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustPhone.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustAB.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
            holder.txtListCustStatus.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));*/
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white_color));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.bg_tabs));
        }

        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*passIntentValues(modal.getSellerPermit(), modal.getBusinessName(), modal.getCustPhone(), modal.getCustEmail(),
                       modal.getCountry(), modal.getCust_state(), modal.getAddress1(), modal.getAddress2(), modal.getCity(), modal.getZipCode());*/

               intent = new Intent(context, CustomerDetailActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
//       SET VALUES in class CustomerDetail
                CustomerDetail.setSellerPermitNumber(modal.getSellerPermit().replace("null", ""));
                CustomerDetail.setBssName(modal.getBusinessName().replace("null", ""));
                CustomerDetail.setId(String.valueOf(modal.getCustId()));
                CustomerDetail.setFirstName(modal.getCustFname().replace("null", ""));
                CustomerDetail.setLastName(modal.getCustLname().replace("null", ""));
                CustomerDetail.setPhone(modal.getCustPhone());
                CustomerDetail.setEmail(modal.getCustEmail().replace("null", ""));
                CustomerDetail.setAddr1(modal.getAddress1());
                CustomerDetail.setAddr2(modal.getAddress2().replace("null", ""));
                CustomerDetail.setCountry(modal.getCountry().replace("null", ""));
                CustomerDetail.setCity(modal.getCity().replace("null", ""));
                CustomerDetail.setState(modal.getCust_state().replace("null", ""));
                CustomerDetail.setZipCode(modal.getZipCode().replace("null", ""));
                CustomerDetail.setCreatedAt(modal.getRegDate().replace("null", ""));

            }
        });
  /*      holder.txtCustFname.setText(modal.getCustFname().concat(" ").concat(modal.getCustLname().replace("null", "")));
//        check customer-status
        int custStatus = modal.getCustStatus();
        if (custStatus == 1) {
            holder.switchCustStatus.setChecked(true);

        }
        holder.cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerEditFragment.onRefreshCustomerDetailFragment(modal, context);
            }
        });
        holder.switchCustStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int statusValue = 0;
                boolean status = holder.switchCustStatus.isChecked();
                if (status) {
                    statusValue = 1;
                    onChangeCustomerStatus(modal.getCustId(), statusValue, modal.getCustFname());
                } else {
                    statusValue = 0;
                    onChangeCustomerStatus(modal.getCustId(), statusValue, modal.getCustFname());
                }

            }
        })*/;
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }

    public void onUpdateList(List<CustomerDataModal> newList) {
        custList = new ArrayList<>();
        custList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imageCustomer;
        private TextView txtListCustBN, txtListCustID, txtListCustFN, txtListCustLN, txtListCustPhone, txtListCustEmail, txtListCustCountry,
                txtListCustState, txtListCustAddr ,txtListCustRegDate;
        private TableRow tblRow2;
      /*  private CardView cardCustomer;
        private TextView txtCustFname;
        private SwitchCompat switchCustStatus;*/
        public ViewHolder(View itemView) {
            super(itemView);

            txtListCustBN = itemView.findViewById(R.id.txt_list_cust_bn);
//            txtListCustID = itemView.findViewById(R.id.txt_list_cust_id);
            txtListCustFN = itemView.findViewById(R.id.txt_list_cust_fn);
            txtListCustLN = itemView.findViewById(R.id.txt_list_cust_ln);
            txtListCustPhone = itemView.findViewById(R.id.txt_list_cust_phone);
            txtListCustEmail = itemView.findViewById(R.id.txt_list_email);
            txtListCustAddr = itemView.findViewById(R.id.txt_list_addr1);
            txtListCustCountry = itemView.findViewById(R.id.txt_list_country);
            txtListCustState = itemView.findViewById(R.id.txt_list_state);
            txtListCustRegDate = itemView.findViewById(R.id.txt_list_date);

            tblRow2 = itemView.findViewById(R.id.trow_label2);
//            imageCustomer = itemView.findViewById(R.id.image_customer);
          /*  txtCustFname = itemView.findViewById(R.id.txt_cust_fullname);
            cardCustomer = itemView.findViewById(R.id.card_customer);
            switchCustStatus = itemView.findViewById(R.id.switch_cust_status);
*/
           /* if (Users.getRole().equalsIgnoreCase("cashier")) {
                switchCustStatus.setEnabled(false);
            }*/

        }
    }

//    Change customer-status (Active or Inactive)
  /*private void onChangeCustomerStatus(final int custId, final int statusValue, final String customer) {
      StringRequest statusRequest = new StringRequest(Request.Method.POST, Routes.setUrl("customerStatus"), new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (response.trim().equals("active")) {
                Toast.makeText(context,  customer + " is active.", Toast.LENGTH_SHORT).show();
            } else if (response.trim().equals("inactive")){
                Toast.makeText(context, customer + " is inactive.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something is wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              AlertDialog.Builder d = new AlertDialog.Builder(context);
              d.setMessage(error.toString());
              d.show();
          }
      }) {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> map = new HashMap<>();
              map.put("custId", String.valueOf(custId));
              map.put("statusValue", String.valueOf(statusValue));
              return map;
          }
      };
      Volley.newRequestQueue(context).add(statusRequest);
  }*/

//  PASS invent-values
  /*  private void passIntentValues(@Nullable String sellerPermitNum, String businessName, String phone, String email,
                                  String country, String state, String addr1, String addr2, String city, String zip) {
        intent = new Intent(context, CustomerDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent != null) {
            intent.putExtra("seller", sellerPermitNum);
            intent.putExtra("bn", businessName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("country", country);
            intent.putExtra("state", state);
            intent.putExtra("addr1", addr1);
            intent.putExtra("addr2", addr2);
            intent.putExtra("city", city);
            intent.putExtra("zip", zip);
        }
        context.startActivity(intent);

    }
*/
}
