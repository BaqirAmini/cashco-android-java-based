package com.xamuor.cashco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xamuor.cashco.cashco.R;

import java.util.Objects;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

//        Hide Navbar
        Objects.requireNonNull(getSupportActionBar()).hide();
//        btn retry to navigation to login-page
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ValidationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
