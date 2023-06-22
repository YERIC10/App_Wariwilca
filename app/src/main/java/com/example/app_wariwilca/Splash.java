package com.example.app_wariwilca;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // Tiempo de espera en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isNetworkConnected(this)) {
            // Hay conexión a Internet
            // Realiza las acciones que requieren conexión a Internet

            // Simulación de espera antes de iniciar la siguiente actividad
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DELAY);

        } else {
            // No hay conexión a Internet
            // Toma las medidas necesarias cuando no hay conexión a Internet

            // Mostrar el cuadro de diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR")
                    .setMessage("Sin conexión a Internet. Vuelve a intentarlo")
                    .setIcon(R.drawable.ic_sin_conexion)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Reiniciar la aplicación
                            Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish(); // Finaliza la actividad actual primero
                            startActivity(restartIntent); // Inicia la nueva actividad
                        }
                    })
                    .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false) // Impedir que el cuadro de diálogo se cierre al tocar fuera de él
                    .show();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}