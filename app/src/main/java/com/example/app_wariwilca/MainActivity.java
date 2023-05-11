package com.example.app_wariwilca;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_wariwilca.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    int RC_SIGN_IN = 1;
    String TAG = "GoogleSignIn";
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //LLAMA A LOS MENUS POR MEDIO DE SUS ID
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    private void ShowDialogFragment() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡BIENVENIDO!")
                .setNegativeButton("Cancel", (dialog, which) ->{dialog.dismiss();})
                .setPositiveButton("Iniciar Sesion", (dialog, which) ->{dialog.dismiss();singIn();})
                .show();
    }
        // Integramos el acceso con Google a la app
    private void initGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("945572842216-rf20ibbhi6g6b9sfk33rfibn12qbadc7.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void singIn(){
        Intent singintent = googleSignInClient.getSignInIntent();
        startActivityForResult(singintent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account =  task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: "+ account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.w(TAG, "Google sing in failed", e);
            }
        }
    }

    //intercambiamos las credenciales de Firebase y usarla para la autenticación
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    gamesGetUserInfo(user);
                    Toast.makeText(getBaseContext(),"ACCESO",Toast.LENGTH_SHORT).show();
                }else{
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    public static class Global {
        public static String usuarioName;
        public static Uri usuarioImg;
        public static String usuarioEmail;
    }
    private void gamesGetUserInfo(FirebaseUser user){

        // NOMBRE USUARIO
        Global.usuarioName  = user.getDisplayName();
        TextView name = findViewById(R.id.txt_NombreUsuario);
        name.setVisibility(View.VISIBLE);
        name.setText(Global.usuarioName);

        // IMAGEN USUARIO
        Global.usuarioImg  = user.getPhotoUrl();
        ImageView imageView = findViewById(R.id.img_imagenUsuario);
        Glide.with(this).load(Global.usuarioImg).into(imageView);
        imageView.setVisibility(View.VISIBLE);

        // EMAIL
        Global.usuarioEmail  = user.getEmail();
        TextView email = findViewById(R.id.txt_EmailUsuario);
        email.setVisibility(View.VISIBLE);
        email.setText(Global.usuarioEmail);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.btn_login:{
                initGoogleSignIn();
                ShowDialogFragment();
            }break;
        }

        return super.onOptionsItemSelected(item);
    }
}