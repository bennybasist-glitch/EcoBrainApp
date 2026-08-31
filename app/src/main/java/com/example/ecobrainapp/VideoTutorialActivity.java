package com.example.ecobrainapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoTutorialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tutorial);

        VideoView videoView = findViewById(R.id.videoTutorial);

        // Video educativo sobre separación de residuos
        // Nota: VideoView requiere un enlace directo a un archivo de video (mp4, etc.)
        // Se utiliza un video de muestra para propósitos de la integración.
        String videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });
    }
}