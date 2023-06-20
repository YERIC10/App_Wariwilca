package com.example.app_wariwilca.ui.Ajsutes;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;

import com.example.app_wariwilca.R;

import java.util.Locale;

public class Ajustes extends PreferenceActivity {

    public static TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // si no se encuentra ningún error, solo se ejecutará
                if(i!=TextToSpeech.ERROR){
                    // Para elegir el idioma del habla
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // Adición de OnClickListener
    }
}