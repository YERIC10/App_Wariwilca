package com.example.app_wariwilca;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatoMuseo extends AppCompatActivity {

    ImageView imgObjetoMuseo, imgQr;
    TextView txtDescrObjeto;
    TextView txtNombreObjeto;
    Button bntDescargarQr;

    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dato_museo);

        imgObjetoMuseo = findViewById(R.id.img_objetomuseo);
        imgQr = findViewById(R.id.img_qr_objeto);
        txtDescrObjeto = findViewById(R.id.txt_DescripOjeto);
        txtNombreObjeto = findViewById(R.id.txt_NombreObjeto);
        bntDescargarQr = findViewById(R.id.btn_decargar_qr);
        bntDescargarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        firestore = FirebaseFirestore.getInstance();
        Cargar_Datos();
    }

    public void Cargar_Datos(){

        // Obtenemos la imagen desde Firebase usando Glide
        if(imgObjetoMuseo != null){
            Glide.with(this).
                    load("https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_1.jpg?alt=media&token=5ecbaf25-244b-450c-a658-4b34689e8d6e")
                    .into(imgObjetoMuseo);

        }else {
            Toast.makeText(this, "Error al cargar la Imagen", Toast.LENGTH_SHORT).show();
        }

        // Obtenemos datos de Firebase de la coleccion "Informacion_Objetos" del campo "Descripcion"

        firestore.collection("Informacion_Objetos").document("bFsOVLfbBbr03ZBmfZOY").
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String Nombre = documentSnapshot.getString("Nombre");
                            String descripcion = documentSnapshot.getString("Descripcion");
                            txtNombreObjeto.setText(Nombre);
                            txtDescrObjeto.setText(descripcion);
                        }else{
                            Toast.makeText(getBaseContext(), "No se encontro resultado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}