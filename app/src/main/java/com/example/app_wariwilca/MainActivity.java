package com.example.app_wariwilca;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.app_wariwilca.databinding.ActivityMainBinding;
import com.example.app_wariwilca.ui.Ajsutes.Ajustes;
import com.example.app_wariwilca.ui.home.Home;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private GoogleApiClient mGoogleApiClient;

    int RC_SIGN_IN = 1;
    String TAG = "GoogleSignIn";
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    boolean audicion;
    private MenuItem btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Boton para scanear qr
        binding.appBarMain.scaner.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scaner QR");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        // Configurar el AuthStateListener de Firebase para gestionar el inicio de sesión
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // El usuario ha iniciado sesión
                    gamesGetUserInfo(user);
                } else {
                    // El usuario ha cerrado sesión
                    TextView name = findViewById(R.id.txt_NombreUsuario);
                    name.setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.img_imagenUsuario);
                    imageView.setVisibility(View.GONE);
                    TextView email = findViewById(R.id.txt_EmailUsuario);
                    email.setVisibility(View.GONE);
                }
            }
        };

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //LLAMA A LOS MENUS POR MEDIO DE SUS ID
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_comentario)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        btnLogin = menu.findItem(R.id.item_Login);
        // TUTORIAL DE PRIMERA VEZ

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View view = findViewById(R.id.item_Login);
                final View view1 = findViewById(R.id.scaner);
                final View view2 = findViewById(R.id.chatbot);
                final View view3 = findViewById(R.id.btn_desplegable);
                final View view4 = findViewById(R.id.btn_Centro);
                final Drawable menu = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_desplegable);
                //Typeface prueba = Typeface.createFromAsset(getAssets(),"fonts/quicksand_medium.ttf");

                NavigationView navigationView = binding.navView;
                new TapTargetSequence(MainActivity.this)
                        .targets(
                                TapTarget.forView(view, "LOGIN", "Accede con tu cuenta de Gooogle")
                                        .outerCircleColor(R.color.black)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.fondo)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.fondo)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.white)
                                        .drawShadow(true)
                                        .cancelable(true)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(60),
                                TapTarget.forView(view1, "SCANNER", "Aqui puedes escanear cualquir objeto")
                                        .outerCircleColor(R.color.black)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.fondo)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.fondo)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.white)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(60),
                                TapTarget.forView(view2, "CHAT BOT", "Por aqui te podremos ayudar si tienes un problema con el Aplicativo")
                                        .outerCircleColor(R.color.black)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.fondo)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.fondo)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.white)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(60),

                                TapTarget.forView(view4, "MAPA DEL MUSEO", "Puedes hacer click en cualquier 'Aréa' de imagen" +
                                                " para obtener mejor informacion detallada ")
                                        .outerCircleColor(R.color.black)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.fondo)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.fondo)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.white)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(60),

                                TapTarget.forView(view3, "ADEMAS", "Te brindamos otras opciones disponibles")
                                        .outerCircleColor(R.color.black)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.fondo)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.fondo)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.white)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(60)
                                        .icon(menu)
                        ).listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {

                            }

                            @Override
                            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {

                            }
                        }).start();
            }
        });
        return  true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void ShowDialogFragment() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Bienvenido")
                .setMessage("Inicia sesión para continuar")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        singIn();
                        btnLogin.setEnabled(false);// Desactivar el botón btn_login
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnLogin.setEnabled(true); // Activar el botón btn_login
                    }
                })
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
    private void singIn(){
        Intent singintent = googleSignInClient.getSignInIntent();
        startActivityForResult(singintent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Resultado del inicio de sesión de Google
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        //Resultado del escaneo de QR
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String qrCode = scanResult.getContents();
            // Realizar alguna acción con el código QR escaneado
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // El inicio de sesión con Google fue exitoso, se autentica en Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            // Error en el inicio de sesión con Google
            Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El inicio de sesión en Firebase fue exitoso
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            gamesGetUserInfo(user);
                        } else {
                            // Error en el inicio de sesión en Firebase
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

    private void gamesGetUserInfo(FirebaseUser user) {
        // Obtener la información del usuario desde Firebase Auth
        if (user != null) {
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();
            String email = user.getEmail();

            Global.usuarioName = name;
            Global.usuarioImg = photoUrl;
            Global.usuarioEmail = email;

            // Actualizar la interfaz de usuario con la información obtenida
            TextView nameTextView = findViewById(R.id.txt_NombreUsuario);
            nameTextView.setText(name);
            nameTextView.setVisibility(View.VISIBLE);

            ImageView imageView = findViewById(R.id.img_imagenUsuario);
            Glide.with(this).load(photoUrl).into(imageView);
            imageView.setVisibility(View.VISIBLE);

            TextView emailTextView = findViewById(R.id.txt_EmailUsuario);
            emailTextView.setText(email);
            emailTextView.setVisibility(View.VISIBLE);

            // Cambiar el icono del botón btn_login por la imagen de perfil
            Drawable profileImage = imageView.getDrawable();
            if (profileImage instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) profileImage).getBitmap();
                BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
                btnLogin.setIcon(icon);
                // Realizar las operaciones adicionales con el bitmap
            } else {
                // El Drawable no es un BitmapDrawable, manejar el caso según corresponda
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_Login: {
                initGoogleSignIn();
                ShowDialogFragment();
                // Desactivar el botón btn_login
            }
            break;

            case R.id.item_Ajustes: {
                Intent intent = new Intent(this, Ajustes.class);
                startActivity(intent);
                return true;
            }

            case R.id.item_CerrarSesion: {
                showLogoutConfirmationDialog();
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cerrar sesión con Google
                        signOut();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // Aquí puedes realizar cualquier otra acción después de cerrar sesión con Google
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                    }
                });
    }

    public void loadPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        audicion = preferences.getBoolean("auditivo", false);

        if (audicion) {
            //CUANDO ES VERDADERA LA OPCION
            // TODO REALIZAR DE TEXTO  A VOZ
        }
    }
}