package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormProductActivity extends AppCompatActivity {

    private EditText inputName, inputPrice;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        inputName = findViewById(R.id.input_name);
        inputPrice = findViewById(R.id.input_price);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("MODE");

        if (mode.equals("MODE_EDIT")) {
            inputName.setText(intent.getStringExtra("PRODUCT_NAME"));
            inputPrice.setText(String.valueOf(intent.getIntExtra("PRODUCT_PRICE", 0)));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = inputName.getText().toString();
                int productPrice = Integer.parseInt(inputPrice.getText().toString());
                saveProduct(productName, productPrice);
            }
        });
    }

    private void saveProduct(String productName, int productPrice) {
        Intent intent = new Intent();
        int id = getIntent().getIntExtra("PRODUCT_ID", -1);

        if (id != -1) {
            intent.putExtra("MODE", "MODE_EDIT");
            intent.putExtra("PRODUCT_ID", id);
        } else {
            intent.putExtra("MODE", "MODE_ADD");
        }

        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("PRODUCT_PRICE", productPrice);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}