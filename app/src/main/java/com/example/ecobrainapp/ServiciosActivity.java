package com.example.ecobrainapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ServiciosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        Button btnLlamar = findViewById(R.id.btnLlamar);
        Button btnVerMapa = findViewById(R.id.btnVerMapa);

        // Acción: Llamar a SEMARNAT
        btnLlamar.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8000000247"));
            startActivity(callIntent);
        });

        // Acción: Ver ubicación en Google Maps
        btnVerMapa.setOnClickListener(v -> {
            // Coordenadas de SEMARNAT CDMX
            String geoUri = "geo:19.4402,-99.1822?q=SEMARNAT+Ejercito+Nacional+223";
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Fallback si no hay Google Maps instalado
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=SEMARNAT+Ejercito+Nacional+223")));
            }
        });
    }
}