package com.example.app_wariwilca.Modelo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.app_wariwilca.R;
import com.example.app_wariwilca.databinding.FragmentHomeBinding;
import com.example.app_wariwilca.Vista.HomeViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ImageView img_plano;
    StorageReference storageReference;
    ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storageReference = FirebaseStorage.getInstance().getReference();
        img_plano = root.findViewById(R.id.img_mapa);
        progressBar = root.findViewById(R.id.progree_imgPlano);

        Cargar_Plano();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Cargar_Plano(){
        progressBar.setMax(100);

        Glide.with(this).
                load("https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Plano%2Fplano.jpg?alt=media&token=7c96d721-ee63-4884-a48f-d90483ef4261")
                .into(img_plano);
    }
}