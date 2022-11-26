package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailProductActivity extends AppCompatActivity {

    TextView txtName, txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        txtName = findViewById(R.id.txt_name);
        txtPrice = findViewById(R.id.txt_price);

        Intent intent = getIntent();
        txtName.setText(intent.getStringExtra("PRODUCT_NAME"));
        txtPrice.setText(String.valueOf(intent.getIntExtra("PRODUCT_PRICE", 0)));
    }
}