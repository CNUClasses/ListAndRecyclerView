package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView_Adapter rva;   //not Richmond VA, Recyclyer View Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get a ref to the viewpager
        rv=findViewById(R.id.rvContacts);
        //create an instance of the swipe adapter
        rva  = new RecyclerView_Adapter(this);
        //set this viewpager to the adapter
        rv.setAdapter(rva);

        // Setup layout manager for items with orientation
        // Also supports `LinearLayoutManager.HORIZONTAL`
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1,LinearLayoutManager.VERTICAL, false);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        // Attach layout manager to the RecyclerView
        rv.setLayoutManager(layoutManager);
    }
}