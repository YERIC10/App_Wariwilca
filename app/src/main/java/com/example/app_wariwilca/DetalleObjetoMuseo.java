package com.example.app_wariwilca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_wariwilca.ui.home.Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class DetalleObjetoMuseo extends AppCompatActivity {
    private static final String CARPETA_PRINCIPAL = "Wariwilca/";
    private static final String CARPETA_IMAGEN = "ImagenesQR";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;

    private static int REQUEST_CODE = 1;
    TextView txtDescrObjeto;
    TextView txtNombreObjeto;
    OutputStream outputStream;
    ImageView imgObjetoMuseo, imgQr;
    Button bntDescargarQr;

    //  VARIABLE PARA OBTENER EL URL DE LA IMAGEN DEL OBJETO DE LA CLASE 'HOME'
    String imgObjeto, informObjeto;
    FirebaseFirestore firestore;


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

        bntDescargarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Invocamos a la funcion para descargar la imagen Qr

                if(ContextCompat.checkSelfPermission(DetalleObjetoMuseo.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    GuardarQR();
                }else{
                    PermisoDenegado();
                }
            }
        });

        Intent i = getIntent();
        imgObjeto = i.getStringExtra(Home.IMGOBJETO);
        informObjeto = i.getStringExtra(Home.INFORMACION_OBJETO);

        firestore = FirebaseFirestore.getInstance();

        Cargar_Datos();

    }

    public void Cargar_Datos() {

        // Obtenemos la imagen desde Firebase usando Glide
        if (imgObjetoMuseo != null) {
            Glide.with(this)
                    .load(imgObjeto)
                    .into(imgObjetoMuseo);
        } else {
            Toast.makeText(DetalleObjetoMuseo.this, "Error al cargar la Imagen", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(DetalleObjetoMuseo.this, "No se encontro resultado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void PermisoDenegado() {
        ActivityCompat.requestPermissions(DetalleObjetoMuseo.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GuardarQR();
            } else {
                Toast.makeText( DetalleObjetoMuseo.this, "Se requiere de permiso para guardar", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void GuardarQR() {

        Uri image;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q){
            image = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            image = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis()+ ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, DIRECTORIO_IMAGEN);
        Uri uri = contentResolver.insert(image, contentValues);

        try{
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgQr.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(DetalleObjetoMuseo.this, "Se Guardo Correctamente.", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(DetalleObjetoMuseo.this, "No se Guardo Correctamente.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}