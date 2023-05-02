package com.example.app_wariwilca.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.app_wariwilca.DatoMuseo;
import com.example.app_wariwilca.R;
import com.example.app_wariwilca.databinding.FragmentHomeBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Home extends Fragment {
    ImageView img_plano;
    StorageReference storageReference;
    ProgressBar progressBar;
    ImageButton btn_izq;
    private FragmentHomeBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storageReference = FirebaseStorage.getInstance().getReference();
        img_plano = root.findViewById(R.id.img_mapa);
        progressBar = root.findViewById(R.id.progree_imgPlano);
        btn_izq = root.findViewById(R.id.btn_Izquierda);
        btn_izq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DatoMuseo.class);
                startActivity(i);
            }
        });

        Cargar_Plano();

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Cargar_Plano(){

        if(img_plano != null){
            Glide.with(this).
                    load("https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Plano%2Fplano.jpg?alt=media&token=7c96d721-ee63-4884-a48f-d90483ef4261")
                    .into(img_plano);
        }else {
            Toast.makeText(getContext(), "Error al cargar la Imagen", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
    }
}