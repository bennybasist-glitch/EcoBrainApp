package com.example.ecobrainapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;

public class CerebroActivity extends AppCompatActivity {

    private EcoBrainDBHelper dbHelper;
    private CardView cardResultado;
    private TextView tvObjeto, tvCategoria, tvInstruccion;
    private ListView listSugerencias;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> nombresResiduos;
    private int idUsuarioLogueado;
    private long ultimoTiempoPuntos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerebro);

        dbHelper = new EcoBrainDBHelper(this);
        SharedPreferences prefs = getSharedPreferences("EcoBrainPrefs", Context.MODE_PRIVATE);
        idUsuarioLogueado = prefs.getInt("id_usuario", 1);

        cardResultado = findViewById(R.id.cardResultado);
        tvObjeto = findViewById(R.id.tvObjeto);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvInstruccion = findViewById(R.id.tvInstruccion);
        listSugerencias = findViewById(R.id.listSugerencias);

        nombresResiduos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresResiduos);
        listSugerencias.setAdapter(adapter);

        SearchView searchResiduo = findViewById(R.id.searchResiduo);
        searchResiduo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mostrarResultado(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    filtrarSugerencias(newText);
                } else {
                    listSugerencias.setVisibility(View.GONE);
                }
                return true;
            }
        });

        listSugerencias.setOnItemClickListener((parent, view, position, id) -> {
            String seleccionado = adapter.getItem(position);
            mostrarResultado(seleccionado);
            listSugerencias.setVisibility(View.GONE);
        });
    }

    private void filtrarSugerencias(String texto) {
        nombresResiduos.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombreObjeto FROM residuo WHERE nombreObjeto LIKE ?",
                new String[]{"%" + texto + "%"});

        while (cursor.moveToNext()) {
            nombresResiduos.add(cursor.getString(0));
        }
        cursor.close();

        if (nombresResiduos.isEmpty()) {
            listSugerencias.setVisibility(View.GONE);
        } else {
            adapter.notifyDataSetChanged();
            listSugerencias.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarResultado(String nombre) {
        // Bloqueo de seguridad: No permite ganar puntos más de una vez cada 15 segundos
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoTiempoPuntos < 15000) {
            Toast.makeText(this, "Validando depósito... Espera unos segundos para tu próximo reciclaje.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT nombreObjeto, categoria, instruccion FROM residuo WHERE nombreObjeto = ? LIMIT 1",
                new String[]{nombre});

        if (cursor.moveToFirst()) {
            tvObjeto.setText("Residuo: " + cursor.getString(0));
            tvCategoria.setText("Contenedor: " + cursor.getString(1));
            tvInstruccion.setText("Instrucción: " + cursor.getString(2));
            cardResultado.setVisibility(View.VISIBLE);

            db.execSQL("UPDATE usuario SET puntos = puntos + 10 WHERE id_usuario = ?", new Object[]{idUsuarioLogueado});
            ultimoTiempoPuntos = tiempoActual;

            Toast.makeText(this, "¡+10 Eco-Puntos! Dirígete al contenedor real para depositar.", Toast.LENGTH_LONG).show();
            reproducirSonido();
        }
        cursor.close();
    }

    private void reproducirSonido() {
        try {
            MediaPlayer mp = MediaPlayer.create(this, android.R.drawable.ic_lock_silent_mode_off);
            if (mp != null) {
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            }
        } catch (Exception ignored) {}
    }
}