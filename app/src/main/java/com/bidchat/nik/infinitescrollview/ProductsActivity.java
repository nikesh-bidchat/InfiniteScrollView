package com.bidchat.nik.infinitescrollview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Button buttonOpen = (Button) findViewById(R.id.products_button_open);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showProductsIntent = new Intent(ProductsActivity.this, ShowProductsActivity.class);
                startActivity(showProductsIntent);
            }
        });
    }
}
