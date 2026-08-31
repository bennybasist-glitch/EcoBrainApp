package com.example.ecobrainapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class EcoTipService extends Service {

    private static final String CHANNEL_ID = "EcoBrainTipsChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mostrarNotificacion("Consejo Eco-Brain", "¡Recuerda apagar los equipos electrónicos para reducir tu huella digital!");
        return START_NOT_STICKY;
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Eco Tips",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setAutoCancel(true);

        if (manager != null) {
            manager.notify(101, builder.build());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}