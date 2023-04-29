package com.example.app_wariwilca.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.app_wariwilca.MainActivity;
import com.example.app_wariwilca.R;

import java.util.Timer;
import java.util.TimerTask;

public class V_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Llama al layout activity -> El diseño Interfaz del Splash
        setContentView(R.layout.activity_vsplash);

         Timer tiempo;
        TimerTask carga = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(V_Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        tiempo = new Timer();
        tiempo.schedule(carga, 2000);
    }
}