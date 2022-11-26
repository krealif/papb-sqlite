package com.example.inventory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Product> listProduct = new ArrayList<>();
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        String mode = data.getStringExtra("MODE");

                        Product product = new Product();
                        product.setId(data.getIntExtra("PRODUCT_ID", -1));
                        product.setName(data.getStringExtra("PRODUCT_NAME"));
                        product.setPrice(data.getIntExtra("PRODUCT_PRICE", 0));

                        if (mode.equals("MODE_EDIT")) {
                            dbHelper.updateProduct(product);
                        } else if (mode.equals("MODE_ADD")) {
                            dbHelper.insertProduct(product);
                        }

                        Toast.makeText(MainActivity.this, "Produk berhasil disimpan ke database ", Toast.LENGTH_SHORT).show();
                        listProduct.clear();
                        getAllProducts();
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerAdapter = new RecyclerAdapter(this, listProduct);
        getAllProducts();
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnClickListener(new RecyclerAdapter.OnClickListener() {
            @Override
            public void onBtnClick(Product product, View view) {
                int id = view.getId();
                if (id == R.id.btn_edit) {
                    Intent intent = new Intent(view.getContext(), FormProductActivity.class);
                    intent.putExtra("MODE", "MODE_EDIT");
                    intent.putExtra("PRODUCT_ID", product.getId());
                    intent.putExtra("PRODUCT_NAME", product.getName());
                    intent.putExtra("PRODUCT_PRICE", product.getPrice());
                    launcher.launch(intent);

                } else if (id == R.id.btn_delete) {
                    dbHelper.deleteProduct(product);
                    listProduct.clear();
                    getAllProducts();
                }
            }

            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
                intent.putExtra("PRODUCT_NAME", product.getName());
                intent.putExtra("PRODUCT_PRICE", product.getPrice());
                startActivity(intent);
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FormProductActivity.class);
                intent.putExtra("MODE", "MODE_ADD");
                launcher.launch(intent);
            }
        });
    }

    private void getAllProducts() {
        listProduct.addAll(dbHelper.getAllProducts());
        recyclerAdapter.notifyDataSetChanged();
    }
}