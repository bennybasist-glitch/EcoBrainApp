package com.example.ecobrainapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EcoBrainDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_ecobrain.db";
    private static final int DATABASE_VERSION = 1;

    public EcoBrainDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla Usuarios
        db.execSQL("CREATE TABLE usuario (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, correo TEXT, password TEXT, rol TEXT, puntos INTEGER DEFAULT 0)");

        // Tabla Residuos
        db.execSQL("CREATE TABLE residuo (" +
                "id_residuo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreObjeto TEXT, categoria TEXT, instruccion TEXT)");

        // Tabla Noticias
        db.execSQL("CREATE TABLE noticia (" +
                "id_noticia INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, contenido TEXT, fecha_publicacion TEXT)");

        // Insertar registros por defecto
        insertarDatosIniciales(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        // Usuarios iniciales: Administrador y Alumno
        db.execSQL("INSERT INTO usuario (nombre, correo, password, rol, puntos) VALUES " +
                "('Profe. Javier', 'admin@ecobrain.com', 'admin123', 'Profesor', 0)");
        
        db.execSQL("INSERT INTO usuario (nombre, correo, password, rol, puntos) VALUES " +
                "('Eco-Héroe Alumno', 'alumno@ecobrain.com', '12345', 'Alumno', 70)");

        // Residuos para el buscador
        db.execSQL("INSERT INTO residuo (nombreObjeto, categoria, instruccion) VALUES " +
                "('Botella de agua', 'Contenedor AMARILLO', 'Vacía el líquido, quita la tapa y aplástala.'), " +
                "('Cuaderno', 'Contenedor AZUL', 'Quita el espiral de metal antes de reciclar las hojas.'), " +
                "('Cáscara de fruta', 'Contenedor ORGÁNICO', 'Deposita en el bote café para hacer composta.'), " +
                "('Pilas o baterías', 'Punto LIMPIO', 'Busca el contenedor especial de pilas en la dirección.'), " +
                "('Lata de jugo', 'Contenedor AMARILLO', 'Enjuaga para evitar malos olores.'), " +
                "('Caja de cereal', 'Contenedor AZUL', 'Aplasta la caja para que ocupe menos espacio.')");

        // Noticias y Tips
        db.execSQL("INSERT INTO noticia (titulo, contenido, fecha_publicacion) VALUES " +
                "('Gran Concurso de Reciclaje', 'Este viernes premiaremos al salón que junte más PET.', '2026-08-28'), " +
                "('¿Qué es la IA Generativa?', 'Son herramientas que crean texto e imágenes, valida siempre la información.', '2026-08-28')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS residuo");
        db.execSQL("DROP TABLE IF EXISTS noticia");
        onCreate(db);
    }
}