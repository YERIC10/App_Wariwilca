package com.example.app_wariwilca.Modelo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_wariwilca.R;

public class M_Informacion_Museo extends AppCompatActivity {

    Button btn_guardar;
    ImageView img_qr, img_objeto;
    TextView txt_objeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinformacion_museo);


    }
}