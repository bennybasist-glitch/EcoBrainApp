package com.example.ecobrainapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NoticiasActivity extends AppCompatActivity {

    private EcoBrainDBHelper dbHelper;
    private ListView lvNoticias;
    private ArrayList<String> noticiasList;
    private ArrayList<Integer> idsList;
    private ArrayAdapter<String> adapter;
    private String userRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        dbHelper = new EcoBrainDBHelper(this);
        SharedPreferences prefs = getSharedPreferences("EcoBrainPrefs", Context.MODE_PRIVATE);
        userRol = prefs.getString("rol", "Alumno");

        lvNoticias = findViewById(R.id.lvNoticias);
        Button btnAgregarNoticia = findViewById(R.id.btnAgregarNoticia);

        noticiasList = new ArrayList<>();
        idsList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noticiasList) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                android.widget.TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE); // Texto blanco para el fondo oscuro
                return view;
            }
        };
        lvNoticias.setAdapter(adapter);

        if ("Profesor".equals(userRol)) {
            btnAgregarNoticia.setVisibility(View.VISIBLE);
        } else {
            btnAgregarNoticia.setVisibility(View.GONE);
        }

        btnAgregarNoticia.setOnClickListener(v -> mostrarDialogoAgregar());

        lvNoticias.setOnItemLongClickListener((parent, view, position, id) -> {
            if ("Profesor".equals(userRol)) {
                mostrarOpcionesAdmin(position);
                return true;
            }
            return false;
        });

        cargarNoticias();
    }

    private void cargarNoticias() {
        noticiasList.clear();
        idsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_noticia, titulo, contenido FROM noticia ORDER BY id_noticia DESC", null);
        
        while (cursor.moveToNext()) {
            idsList.add(cursor.getInt(0));
            noticiasList.add(cursor.getString(1).toUpperCase() + "\n" + cursor.getString(2));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Noticia Ambiental");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etTitulo = new EditText(this);
        etTitulo.setHint("Título de la noticia");
        etTitulo.setTextColor(Color.BLACK); // Fijar color negro para lectura
        etTitulo.setHintTextColor(Color.GRAY);
        layout.addView(etTitulo);

        final EditText etContenido = new EditText(this);
        etContenido.setHint("Descripción del comunicado...");
        etContenido.setTextColor(Color.BLACK); // Fijar color negro para lectura
        etContenido.setHintTextColor(Color.GRAY);
        layout.addView(etContenido);

        builder.setView(layout);
        builder.setPositiveButton("Publicar", (dialog, which) -> {
            String titulo = etTitulo.getText().toString();
            String contenido = etContenido.getText().toString();
            if (!titulo.isEmpty() && !contenido.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT INTO noticia (titulo, contenido, fecha_publicacion) VALUES (?, ?, ?)",
                        new Object[]{titulo, contenido, "2024-08-30"});
                cargarNoticias();
                Toast.makeText(this, "Noticia publicada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarOpcionesAdmin(int position) {
        String[] opciones = {"Editar Noticia", "Eliminar Noticia", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gestión de Comunicado");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                mostrarDialogoEditar(position);
            } else if (which == 1) {
                confirmarEliminar(position);
            }
        });
        builder.show();
    }

    private void mostrarDialogoEditar(int position) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT titulo, contenido FROM noticia WHERE id_noticia = ?", 
                new String[]{String.valueOf(idsList.get(position))});
        
        if (cursor.moveToFirst()) {
            String oldTitulo = cursor.getString(0);
            String oldContenido = cursor.getString(1);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Editar Noticia");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            final EditText etTitulo = new EditText(this);
            etTitulo.setText(oldTitulo);
            etTitulo.setTextColor(Color.BLACK);
            layout.addView(etTitulo);

            final EditText etContenido = new EditText(this);
            etContenido.setText(oldContenido);
            etContenido.setTextColor(Color.BLACK);
            layout.addView(etContenido);

            builder.setView(layout);
            builder.setPositiveButton("Actualizar", (dialog, which) -> {
                String newTitulo = etTitulo.getText().toString();
                String newContenido = etContenido.getText().toString();
                SQLiteDatabase dbW = dbHelper.getWritableDatabase();
                dbW.execSQL("UPDATE noticia SET titulo = ?, contenido = ? WHERE id_noticia = ?",
                        new Object[]{newTitulo, newContenido, idsList.get(position)});
                cargarNoticias();
                Toast.makeText(this, "Noticia actualizada", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        }
        cursor.close();
    }

    private void confirmarEliminar(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar noticia")
                .setMessage("¿Estás seguro de borrar este comunicado?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM noticia WHERE id_noticia = ?", new Object[]{idsList.get(position)});
                    cargarNoticias();
                    Toast.makeText(this, "Noticia eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}