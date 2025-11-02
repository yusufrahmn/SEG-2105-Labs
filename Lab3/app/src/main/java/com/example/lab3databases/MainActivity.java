package com.example.lab3databases;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView productId;
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;

    ArrayList<String> productList;
    ArrayAdapter adapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        // info layout
        productId = findViewById(R.id.productId);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        //buttons
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // listview
        productListView = findViewById(R.id.productListView);

        // db handler
        dbHandler = new MyDBHandler(this);

        // button listeners
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                double price = Double.parseDouble(productPrice.getText().toString());
                Product product = new Product(name, price);
                dbHandler.addProduct(product);

                productName.setText("");
                productPrice.setText("");

//                Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String price = productPrice.getText().toString();

                String nameSearched = name.isEmpty() ? null : name;
                String priceSearched = price.isEmpty() ? null : price;
                
                productList.clear();
                Cursor cursor = dbHandler.findProducts(nameSearched, priceSearched);
                
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        productList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
                    }
                    Toast.makeText(MainActivity.this, "Found " + cursor.getCount() + " products", Toast.LENGTH_SHORT).show();
                }
                
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, productList);
                productListView.setAdapter(adapter);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                dbHandler.deleteProduct(name);
                
                productName.setText("");
                productPrice.setText("");
                
                Toast.makeText(MainActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });


        viewProducts();
    }

    private void viewProducts() {
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                productList.add(cursor.getString(1) + " (" +cursor.getString(2)+")");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }
}