package com.example.ecobrainapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        TextView tvPerfilNombre = findViewById(R.id.tvPerfilNombre);
        TextView tvPerfilCorreo = findViewById(R.id.tvPerfilCorreo);
        TextView tvPuntosTotales = findViewById(R.id.tvPuntosTotales);
        TextView tvRecompensa = findViewById(R.id.tvRecompensa);

        SharedPreferences prefs = getSharedPreferences("EcoBrainPrefs", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", 1);

        EcoBrainDBHelper dbHelper = new EcoBrainDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre, correo, puntos FROM usuario WHERE id_usuario = ?",
                new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            int puntos = cursor.getInt(2);
            tvPerfilNombre.setText(cursor.getString(0));
            tvPerfilCorreo.setText(cursor.getString(1));
            tvPuntosTotales.setText(puntos + " Puntos");

            // Meta actualizada a 1000 puntos para el Desayuno Sorpresa
            int meta = 1000;
            if (puntos >= meta) {
                tvRecompensa.setText("¡CANJEA YA TU DESAYUNO SORPRESA EN COOPERATIVA!");
                tvRecompensa.setTextColor(Color.parseColor("#00E5FF")); // Cian Neón
                tvRecompensa.setTextSize(18);
            } else {
                int faltantes = meta - puntos;
                tvRecompensa.setText("Faltan " + faltantes + " pts para el Desayuno Sorpresa");
                tvRecompensa.setTextColor(Color.parseColor("#C0CA33")); // Lima
            }
        }
        cursor.close();
    }
}