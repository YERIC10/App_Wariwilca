package com.example.app_wariwilca.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.app_wariwilca.DatoMuseo;
import com.example.app_wariwilca.MainActivity;
import com.example.app_wariwilca.R;
import com.example.app_wariwilca.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Home extends Fragment implements View.OnClickListener {
    public static final String IMGOBJETO = "url_img_objeto";
    public static final String INFORMACION_OBJETO = "infor_objeto";
    ImageView img_plano;
    StorageReference storageReference;
    ProgressBar progressBar;

    // BOTONES
    ImageButton btnDereTop, btnDereEnd, btnDereCentro;
    ImageButton btnIzqTop, btnIzqEnd, btnIzqCentro;
    ImageButton btnCentroTop, btnCentroEnd, btnCentro;
    private FragmentHomeBinding binding;

    //VARIABLES PARA OBTENER DATOS DE LOS OBJETOS
    String url_img_objeto, infor_objeto;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storageReference = FirebaseStorage.getInstance().getReference();
        img_plano = root.findViewById(R.id.img_mapa);
        progressBar = root.findViewById(R.id.progree_imgPlano);

        btnDereTop = root.findViewById(R.id.btn_DerTop);        btnDereTop.setOnClickListener(this);
        btnDereEnd = root.findViewById(R.id.btn_DerEnd);        btnDereEnd.setOnClickListener(this);
        btnDereCentro = root.findViewById(R.id.btn_DerCentro);  btnDereCentro.setOnClickListener(this);

        btnIzqTop = root.findViewById(R.id.btn_IzqTop);         btnIzqTop.setOnClickListener(this);
        btnIzqEnd = root.findViewById(R.id.btn_IzqEnd);         btnIzqEnd.setOnClickListener(this);
        btnIzqCentro = root.findViewById(R.id.btn_IzqCentro);   btnIzqCentro.setOnClickListener(this);

        btnCentroTop = root.findViewById(R.id.btn_CentroTop);   btnCentroTop.setOnClickListener(this);
        btnCentroEnd = root.findViewById(R.id.btn_CentroEnd);   btnCentroEnd.setOnClickListener(this);
        btnCentro = root.findViewById(R.id.btn_Centro);         btnCentro.setOnClickListener(this);


        // INTANCIA A CONECTARME CON GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

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

    @Override
    public void onClick(View view) {

        // Brindamos los datos de URL desde Firebase para enviarlos a la clase DatosMuseo
        switch (view.getId()){
            case R.id.btn_Centro:{
                url_img_objeto = "https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2Fdescarga.jpg?alt=media&token=88c01156-9111-4f69-97d1-87dfc6ce4c75";
                infor_objeto ="HWUDh6eLq0iG6Ceq3igo";

            }break;
            case R.id.btn_CentroEnd:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_1.jpg?alt=media&token=5ecbaf25-244b-450c-a658-4b34689e8d6e";
                infor_objeto ="bFsOVLfbBbr03ZBmfZOY";

            }break;
            case R.id.btn_CentroTop:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_3.jpg?alt=media&token=959846fe-de19-4f4b-8e8d-f019e1a90aab";
                infor_objeto ="gbPpuCJALRmye6GQmXjI";

            }break;
            case R.id.btn_DerCentro:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_4.jpg?alt=media&token=899996d7-bc10-482d-8d95-02de2b9eae9a";
                infor_objeto ="hzWAhi13PvscP3mknW36";

            }break;
            case R.id.btn_DerEnd:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_5.png?alt=media&token=c95ca9b0-774b-49ab-9b59-25ea8ae204b6";
                infor_objeto ="hzWAhi13PvscP3mknW36";

            }break;
            case R.id.btn_DerTop:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_6.jpg?alt=media&token=e746871a-e53d-4adf-ac17-7ed6600009cb";
                infor_objeto ="hzWAhi13PvscP3mknW36";

            }break;
            case R.id.btn_IzqCentro:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_7.jpg?alt=media&token=1f0bc69d-0375-493e-8d0d-d39c285f5d6b";
                infor_objeto ="gbPpuCJALRmye6GQmXjI";

            }break;
            case R.id.btn_IzqTop:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_8.jpg?alt=media&token=28613de2-1f0f-4d8a-8acf-ed806d0e3fbf";
                infor_objeto ="gbPpuCJALRmye6GQmXjI";

            }break;
            case R.id.btn_IzqEnd:{
                url_img_objeto ="https://firebasestorage.googleapis.com/v0/b/app-warivilca.appspot.com/o/Objetos_Museo%2FObjeto_9.jpg?alt=media&token=589bb899-3625-4759-82c7-260a627ebe46";
                infor_objeto ="HWUDh6eLq0iG6Ceq3igo";

            }break;
            default: break;
        }

        // LLAMAMOS A LA CLASE DATOS MUSEO Y ENVIAMOS LOS DATOS OBTENIDOS
        Intent i = new Intent(getActivity(), DatoMuseo.class);
        i.putExtra(IMGOBJETO, url_img_objeto);
        i.putExtra(INFORMACION_OBJETO, infor_objeto);

        startActivity(i);
    }
}