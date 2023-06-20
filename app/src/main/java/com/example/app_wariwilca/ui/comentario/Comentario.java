package com.example.app_wariwilca.ui.comentario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_wariwilca.R;
import com.example.app_wariwilca.databinding.FragmentComentarioBinding;
import com.example.app_wariwilca.ui.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
public class Comentario extends Fragment{
    private @NonNull FragmentComentarioBinding binding;
    String nombUsuario, comentario;
    EditText txtNombUsuario, txtComentario;
    Button btnGuardar;
    Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComentarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNombUsuario = root.findViewById(R.id.txt_Nombre);
        txtComentario = root.findViewById(R.id.txt_Comentario);
        btnGuardar = root.findViewById(R.id.btn_guardar_comentario);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombUsuario = txtNombUsuario.getText().toString();
                comentario = txtComentario.getText().toString();

                if (nombUsuario.isEmpty() || comentario.isEmpty()){
                    Toast.makeText(getActivity(),"No se pueden guardar datos vacios", Toast.LENGTH_SHORT).show();

                }else {
                    usuario = new Usuario(nombUsuario, comentario);
                    FirebaseFirestore.getInstance().collection("Comentario_Usuarios").document().set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // PONER MENSAJE

                        }
                    });

                    txtComentario.getText().clear();
                    txtNombUsuario.getText().clear();
                }
            }
        });
        return root;
    }
}