package com.example.Varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {
    private Button btnBack_insert;
    private Button btnAddCity;
    private EditText editTxtCityName;
    private EditText editTxtCountryName;
    private EditText editTxtPopulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();

        btnBack_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(InsertActivity.this, MainActivity.class);
                InsertActivity.this.startActivity(myIntent);
            }
        });

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });
    }

    public void init() {
        btnBack_insert = findViewById(R.id.btnBack_insert);
        btnAddCity = findViewById(R.id.btnAddCity);
        editTxtCityName = findViewById(R.id.editTxtCityName);
        editTxtCountryName = findViewById(R.id.editTxtCountryName);
        editTxtPopulation = findViewById(R.id.editTxtPopulation);
    }

    private void addCity() {
        String name = editTxtCityName.getText().toString();
        String country = editTxtCountryName.getText().toString();
        String population = editTxtPopulation.getText().toString();

        boolean valid = validation();

        if (valid) {
            Toast.makeText(this,
                    "Minden mezo kitoltese kotelezo.", Toast.LENGTH_SHORT).show();
            return;
        }

        int populationInt = Integer.parseInt(population);
        City city = new City(0, name, country, populationInt);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(MainActivity.BASE_URL, "POST",
                jsonConverter.toJson(city));
        task.execute();
    }

    private boolean validation() {
        if (editTxtCityName.getText().toString().isEmpty() ||
                editTxtCountryName.getText().toString().isEmpty() || editTxtPopulation.getText().toString().isEmpty())
            return true;
        else
            return false;
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        private String requestUrl;
        private String requestMethod;
        private String requestBody;

        public RequestTask(String requestUrl) {
            this.requestUrl = requestUrl;
            this.requestMethod = "GET";
        }

        public RequestTask(String requestUrl, String requestMethod) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
        }

        public RequestTask(String requestUrl, String requestMethod, String requestBody) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
            this.requestBody = requestBody;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestMethod) {
                    case "GET":
                        response = RequestHandler.get(MainActivity.BASE_URL);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestBody);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestBody);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}