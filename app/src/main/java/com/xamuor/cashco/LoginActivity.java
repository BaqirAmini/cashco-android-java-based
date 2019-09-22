package com.xamuor.cashco;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    Intent loginIntent;
    EditText editUserName, editPassword;
    Context context = LoginActivity.this;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Aman's comment: July 06 2019
        Should check if user is already logged in by setting a flag
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Hide actionbar for this activity
        Objects.requireNonNull(getSupportActionBar()).hide();
        editUserName = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        imgLogo = findViewById(R.id.img_logo);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });
    }
// To let System-admin log into the system
    private void onLogin() {
        /*
        Aman: Juy 06 2019
        Should check for user name and password before submitting date to data base at the client side
         */
        final String username = editUserName.getText().toString();
        final String password = editPassword.getText().toString();
       final StringRequest loginRequest = new StringRequest(Request.Method.POST, Routes.setUrl("login"), new Listener<String>() {
           @Override
           public void onResponse(String response) {
               if (response.trim().contains("fail")) {
                   if (username.isEmpty()) {
                       editUserName.setError("User Name required!");
                   }
                   if (password.isEmpty()) {
                       editPassword.setError("Password required!");
                   } else  {
                      /* editUserName.getText().clear();
                       editPassword.getText().clear();
                       editUserName.setError("Username or password wrong!");*/

//                      Go to next activity for validation
                     loginIntent = new Intent(context, ValidationActivity.class);
                     startActivity(loginIntent);
                   }
               }
               else {
                   try {
//                       Fetch users' details from server while login

                       /*
                       Aman: July 06
                       From where Users object is accessed
                        */
                       JSONObject jsonObject = new JSONObject(response);
                       Users.setCompanyId(jsonObject.getInt("compId"));
                       Users.setUserId(jsonObject.getInt("userId"));
                       Users.setFname(jsonObject.getString("fname"));
                       Users.setLname(jsonObject.getString("lname"));
                       Users.setPhone(jsonObject.getString("phone"));
                       Users.setRole(jsonObject.getString("role"));
                       Users.setStatus(jsonObject.getInt("status"));
                       Users.setPhoto(jsonObject.getString("photo"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   /*
                   Aman: July 06
                   Why InventoryActivity
                    */
                   loginIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                   startActivity(loginIntent);
                   finish();
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
               Map<String, String> map = new HashMap<String, String>();
               map.put("username", username);
               map.put("password", password);
               return map;

           }

       };
loginRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       Volley.newRequestQueue(getApplicationContext()).add(loginRequest);
    }
}
