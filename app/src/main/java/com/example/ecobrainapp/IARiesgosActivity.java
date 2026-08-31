package com.example.ecobrainapp;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class IARiesgosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ia_riesgos);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("IA y Riesgos Digitales");
        }
    }
}