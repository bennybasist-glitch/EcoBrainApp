package com.example.ecobrainapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private EcoBrainDBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new EcoBrainDBHelper(this);
        sharedPreferences = getSharedPreferences("EcoBrainPrefs", Context.MODE_PRIVATE);

        // Si ya hay sesión activa, ir a MainActivity
        if (sharedPreferences.contains("id_usuario")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
    }

    private void validarLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_usuario, nombre, rol FROM usuario WHERE correo = ? AND password = ?",
                new String[]{email, pass});

        if (cursor.moveToFirst()) {
            int idUsuario = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String rol = cursor.getString(2); // Asumiendo que agregamos rol al SELECT

            // Guardar en SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id_usuario", idUsuario);
            editor.putString("nombre", nombre);
            editor.putString("rol", rol);
            editor.apply();

            Toast.makeText(this, "¡Bienvenido " + nombre + "!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}