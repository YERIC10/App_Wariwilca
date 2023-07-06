package com.example.app_wariwilca.ui.comentario;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.tv.AdRequest;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.util.Arrays;


public class Comentario extends Fragment {
    private FragmentComentarioBinding binding;
    String nombUsuario, comentario;
    EditText txtNombUsuario, txtComentario;
    Button btnGuardar;
    Usuario usuario;
    ProgressBar progressBar_Comentario;

    //This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String TAG = "Comentario";
    private FrameLayout adContainerView;
    private AdView adView;
    private Activity mActivity;


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


        //ANUNCIO
        // Log the Mobile Ads SDK version.
        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        adContainerView = root.findViewById(R.id.ad_view_container);

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        return root;
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(getContext());
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}