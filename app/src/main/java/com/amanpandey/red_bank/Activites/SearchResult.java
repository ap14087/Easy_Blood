package com.amanpandey.red_bank.Activites;

import android.content.Intent;
import android.os.Bundle;

import android.text.SpannableString;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amanpandey.red_bank.Adapters.SearchAdapter;
import com.amanpandey.red_bank.DataModels.Donor;
import com.amanpandey.red_bank.DataModels.RequestDataModel;
import com.amanpandey.red_bank.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    List<Donor> donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        donorList = new ArrayList<>();

        String json;
        String city,blood_group;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        blood_group = intent.getStringExtra("blood_group");
        TextView heading = findViewById(R.id.heading);
        String str= "Donors in " + city + " with blood group " + blood_group;
        heading.setText(str);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>(){}.getType();
        List<Donor> dataModels = gson.fromJson(json,type);

        if (dataModels != null && dataModels.isEmpty()) {
            heading.setText("No results");
        }else if(dataModels != null){
            donorList.addAll(dataModels);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter adapter = new SearchAdapter(donorList,SearchResult.this);
        recyclerView.setAdapter(adapter);
    }
}