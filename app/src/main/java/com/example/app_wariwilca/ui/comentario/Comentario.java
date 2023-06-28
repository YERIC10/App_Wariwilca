package com.example.app_wariwilca.ui.comentario;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.app_wariwilca.R;
import com.example.app_wariwilca.databinding.FragmentComentarioBinding;
import com.example.app_wariwilca.ui.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

public class Comentario extends Fragment {
    private FragmentComentarioBinding binding;
    String nombUsuario, comentario;
    EditText txtNombUsuario, txtComentario;
    Button btnGuardar;
    Usuario usuario;
    ProgressBar progressBar_Comentario;

    //This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
    /*private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String TAG = "Comentario";
    private FrameLayout adContainerView;
    private AdView adView;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComentarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNombUsuario = root.findViewById(R.id.txt_Nombre);
        txtComentario = root.findViewById(R.id.txt_Comentario);
        btnGuardar = root.findViewById(R.id.btn_guardar_comentario);
        progressBar_Comentario = root.findViewById(R.id.progressBar_comentario);

        // Obtener el drawable actual del ProgressBar
        Drawable drawable = progressBar_Comentario.getIndeterminateDrawable();

        // Establecer el color del drawable (puedes cambiar el color según tus preferencias)
        drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_IN);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombUsuario = txtNombUsuario.getText().toString();
                comentario = txtComentario.getText().toString();

                // Mostrar ProgressBar y ocultar botón
                progressBar_Comentario.setVisibility(View.VISIBLE);
                btnGuardar.setVisibility(View.INVISIBLE);

                if (nombUsuario.isEmpty() || comentario.isEmpty()) {
                    Toast.makeText(getActivity(), "No se pueden guardar datos vacios", Toast.LENGTH_SHORT).show();
                    progressBar_Comentario.setVisibility(View.INVISIBLE);
                    btnGuardar.setVisibility(View.VISIBLE);

                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setTitle("GUARDAR")
                            .setMessage("Estas seguro que deseas guardar tu comentario")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    usuario = new Usuario(nombUsuario, comentario);
                                    FirebaseFirestore.getInstance().collection("Comentario_Usuarios").document().set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            progressBar_Comentario.setVisibility(View.INVISIBLE);
                                            btnGuardar.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(), "Gracias por compartir tu comentario", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    txtComentario.getText().clear();
                                    txtNombUsuario.getText().clear();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar_Comentario.setVisibility(View.INVISIBLE);
                                    btnGuardar.setVisibility(View.VISIBLE);
                                }
                            })
                            .show();
                }
            }
        });
        return root;
    }
}
