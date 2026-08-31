package com.example.ecobrainapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar servicio en segundo plano (EcoTipService)
        Intent serviceIntent = new Intent(this, EcoTipService.class);
        startService(serviceIntent);

        CardView cardCerebro = findViewById(R.id.cardCerebro);
        CardView cardIARiesgos = findViewById(R.id.cardIARiesgos);
        CardView cardMultimedia = findViewById(R.id.cardMultimedia);
        CardView cardWeb = findViewById(R.id.cardWeb);
        CardView cardNoticias = findViewById(R.id.cardNoticias);
        Button btnPerfil = findViewById(R.id.btnPerfil);
        Button btnLogout = findViewById(R.id.btnLogout);

        cardCerebro.setOnClickListener(v -> startActivity(new Intent(this, CerebroActivity.class)));
        cardIARiesgos.setOnClickListener(v -> startActivity(new Intent(this, IARiesgosActivity.class)));
        cardMultimedia.setOnClickListener(v -> startActivity(new Intent(this, VideoTutorialActivity.class)));
        cardWeb.setOnClickListener(v -> startActivity(new Intent(this, WebPortalActivity.class)));
        cardNoticias.setOnClickListener(v -> startActivity(new Intent(this, NoticiasActivity.class)));
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        
        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("EcoBrainPrefs", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cerebro) {
            startActivity(new Intent(this, CerebroActivity.class));
            return true;
        } else if (id == R.id.action_ia_riesgos) {
            startActivity(new Intent(this, IARiesgosActivity.class));
            return true;
        } else if (id == R.id.action_perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            getSharedPreferences("EcoBrainPrefs", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}