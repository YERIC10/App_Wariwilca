package com.example.app_wariwilca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.app_wariwilca.ui.home.Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetalleObjetoMuseo extends AppCompatActivity {
    TextView txtDescrObjeto, txtNombreObjeto;
    ImageView imgObjetoMuseo, imgQr;
    Button bntDescargarQr;

    //  VARIABLE PARA OBTENER EL URL DE LA IMAGEN DEL OBJETO DE LA CLASE 'HOME'
    String imgObjeto, informObjeto;
    FirebaseFirestore firestore;

    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;

    ProgressBar progressBar_descr, progressBar_nomb;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_objeto_museo);
        imgObjetoMuseo = findViewById(R.id.img_objetomuseo);
        imgQr = findViewById(R.id.img_qr_objeto);
        txtDescrObjeto = findViewById(R.id.txt_DescripOjeto);
        txtNombreObjeto = findViewById(R.id.txt_NombreObjeto);
        bntDescargarQr = findViewById(R.id.btn_decargar_qr);

        //PROGRESS BAR
        progressBar_descr = findViewById(R.id.progrees_Descrip);
        progressBar_nomb = findViewById(R.id.progrees_nomb);

        // Obtener el drawable actual del ProgressBar
        Drawable drawable_info = progressBar_descr.getIndeterminateDrawable();
        Drawable drawable_qr = progressBar_nomb.getIndeterminateDrawable();

        // Establecer el color del drawable (puedes cambiar el color según tus preferencias)
        drawable_info.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        drawable_qr.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);

        bntDescargarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardarQR();
            }
        });

        Intent i = getIntent();
        imgObjeto = i.getStringExtra(Home.IMGOBJETO);
        informObjeto = i.getStringExtra(Home.INFORMACION_OBJETO);

        firestore = FirebaseFirestore.getInstance();
        Cargar_Datos();
    }

    public void Cargar_Datos() {
        progressBar_nomb.setVisibility(View.VISIBLE);
        progressBar_descr.setVisibility(View.VISIBLE);

        // Obtenemos la imagen desde Firebase usando Glide
        if (imgObjetoMuseo == null) {
            Toast.makeText(DetalleObjetoMuseo.this, "Error al cargar la Imagen", Toast.LENGTH_SHORT).show();
        } else {
            Glide.with(this)
                    .load(imgObjeto)
                    .into(imgObjetoMuseo);
        }
        // Obtenemos datos de Firebase de la coleccion "Informacion_Objetos" del campo "Descripcion" y "Nombre"
        firestore.collection("Informacion_Objetos").document(informObjeto).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String Nombre = documentSnapshot.getString("Nombre");
                            String descripcion = documentSnapshot.getString("Descripcion");
                            txtNombreObjeto.setText(Nombre);
                            txtDescrObjeto.setText(descripcion);
                            progressBar_descr.setVisibility(View.INVISIBLE);
                            progressBar_nomb.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(DetalleObjetoMuseo.this, "No se encontro resultado", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void GuardarQR() {

        bitmapDrawable = (BitmapDrawable) imgQr.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File Directory = new File(sdCard.getAbsolutePath()+"/Download");
        Directory.mkdir();

        String filname = String.format("%d.jpg", System.currentTimeMillis());
        File outfile = new File(Directory, filname);

        Toast.makeText(DetalleObjetoMuseo.this, "DESCARGADO", Toast.LENGTH_SHORT).show();

        try {
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}