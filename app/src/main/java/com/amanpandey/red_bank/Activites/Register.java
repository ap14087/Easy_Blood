package com.amanpandey.red_bank.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amanpandey.red_bank.R;
import com.amanpandey.red_bank.Utils.Endpoints;
import com.amanpandey.red_bank.Utils.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    private TextInputEditText nameEt, cityEt,bloodGroupEt,passwordEt,mobileEt;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        bloodGroupEt = findViewById(R.id.blood_group);
        passwordEt = findViewById(R.id.password);
        mobileEt = findViewById(R.id.number);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(view -> {
            String name,city,blood_group,password,mobile;
            name = nameEt.getText().toString();
            city = cityEt.getText().toString();
            blood_group = bloodGroupEt.getText().toString();
            password = passwordEt.getText().toString();
            mobile = mobileEt.getText().toString();

            if(isValid(name,city,blood_group,password,mobile)){
                register(name,city,blood_group,password,mobile);
            }
        });

    }

    private void register(String name,String city,String blood_group,String password,String mobile){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city).apply();
                    Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this,MainActivity.class));
                    Register.this.finish();
                }else{
                    Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("city",city);
                params.put("blood_group",blood_group);
                params.put("password",password);
                params.put("number",mobile);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name,String city,String blood_group,String password,String mobile){
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("O-");
        valid_blood_groups.add("O+");

        if(name.isEmpty()){
            showMessage("Name is empty");
            return false;
        }else if(city.isEmpty()){
            showMessage("City name is required");
            return false;
        }else if(!valid_blood_groups.contains(blood_group)){
            showMessage("Blood group invalid choose from"+valid_blood_groups);
        }else if(mobile.length()!=10){
            showMessage("Invalid mobile number, number should be of 10 digits");
            return false;
        }
        return true;
    }

    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}