package com.example.artwise_as.ui.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.artwise_as.Menu2Activity;
import com.example.artwise_as.R;
import com.example.artwise_as.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Menu2Activity activity;
    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_AUDIO_PERMISSION = 1;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        onClick();
        microInit();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Menu2Activity) {
            activity = (Menu2Activity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Menu2Activity");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClick(){
        binding.btnComenzar.setOnClickListener(new View.OnClickListener() { // Define el OnClickListener
            @Override
            public void onClick(View v) { // Define el comportamiento al hacer clic
                // Aquí es donde pondrías el código que quieres ejecutar cuando se hace clic en el botón
                scaneo();
            }
        });
        binding.btnRepe.setOnClickListener(new View.OnClickListener() { // Define el OnClickListener
            @Override
            public void onClick(View v) { // Define el comportamiento al hacer clic
                // Aquí es donde pondrías el código que quieres ejecutar cuando se hace clic en el botón
                activity.repeOBRA();
            }
        });
        binding.btnInformacion.setOnClickListener(new View.OnClickListener() { // Define el OnClickListener
            @Override
            public void onClick(View v) { // Define el comportamiento al hacer clic
                // Aquí es donde pondrías el código que quieres ejecutar cuando se hace clic en el botón
                activity.infoOBRA();
            }
        });
        binding.btnMicro.setOnClickListener(new View.OnClickListener() { // Define el OnClickListener
            @Override
            public void onClick(View v) { // Define el comportamiento al hacer clic
                // Aquí es donde pondrías el código que quieres ejecutar cuando se hace clic en el botón
                Onmicro();
            }
        });
        binding.btnVelocidad.setOnClickListener(new View.OnClickListener() { // Define el OnClickListener
            @Override
            public void onClick(View v) { // Define el comportamiento al hacer clic
                // Aquí es donde pondrías el código que quieres ejecutar cuando se hace clic en el botón
                Toast.makeText(getContext(), "¡Haz hecho clic en el botón velocidad!", Toast.LENGTH_SHORT).show();
            }
        });

    }//fin onClick

    public void changeText(){
        if(binding.btnComenzar.getText().equals("Comenzar")){
            binding.btnComenzar.setText("Buscando");
        }else{
            binding.btnComenzar.setText("Comenzar");

        }
    }
    public void scaneo(){
        changeText();
        if(binding.btnComenzar.getText().equals("Buscando")){
            activity.startScanning();
        }else {
            activity.stopScanning();
        }

        }

    public void Onmicro(){

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(getContext(), "La aplicación requiere permiso de grabación de audio para funcionar", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_AUDIO_PERMISSION);
        }else{

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            try {
                speechRecognizer.startListening(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Recognition failed, check if the microphone is available.", Toast.LENGTH_SHORT).show();
            }
            //activacionVoz.setImageResource(R.drawable.icons8_microphone_50__3_);
            binding.btnMicro.setImageResource(R.drawable.baseline_mic_22);
        }

    }
     public void microInit(){
         speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
         speechRecognizer.setRecognitionListener(new RecognitionListener() {
             @Override
             public void onReadyForSpeech(Bundle params) {
                 Log.d("SpeechRecognizer", "onReadyForSpeech");
             }

             @Override
             public void onBeginningOfSpeech() {
                 Log.d("SpeechRecognizer", "onBeginningOfSpeech");
             }

             @Override
             public void onRmsChanged(float rmsdB) {
                 Log.d("SpeechRecognizer", "onRmsChanged");
             }

             @Override
             public void onBufferReceived(byte[] buffer) {
                 Log.d("SpeechRecognizer", "onBufferReceived");
             }

             @Override
             public void onEndOfSpeech() {
                 Log.d("SpeechRecognizer", "onEndOfSpeech");
             }

             @Override
             public void onError(int error) {
                 Log.e("SpeechRecognizer", "onError: " + error);
                 //txt.setText("Error code: " + error);
                 //activacionVoz.setImageResource(R.drawable.icons8_microphone_50__1_);
                 binding.btnMicro.setImageResource(R.drawable.baseline_mic_24);
             }

             @Override
             public void onResults(Bundle results) {
                 ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 if (result != null) {
                     String frase=result.get(0);
                     scaneo();
                 }else {
                     Log.e("SpeechRecognizer", "onError: ");
                 }

                 /*if (result != null) {
                     String frase=result.get(0);
                     frase.toLowerCase();
                     if(frase.contains("comenzar") || frase.contains("comienza") || frase.contains("comiénzame")){
                         if(scanButton.getText().equals("Comenzar")) {scaneo();}
                     }else if(frase.contains("terminar") || frase.contains("termina") || frase.contains("termíname")){
                         if(scanButton.getText().equals("Buscando")) {scaneo();}
                     } else if(frase.contains("repetir")||frase.contains("repite")||frase.contains("repíteme")){
                         repetrir();
                     }else if(frase.contains("reporte")||frase.contains("reportar")){
                         reporte();
                     }else if(frase.contains("opcional")){
                         Pattern p = Pattern.compile("\\bactiva\\b");
                         Matcher m = p.matcher(frase);
                         if(alertas.getText().equals("Información Opcional")){
                             if(frase.equals("información opcional") || m.find() || frase.contains("pon") || frase.contains("ponme") && !frase.contains("desactiva")){
                                 alertas();
                             }
                         }else if(alertas.getText().equals("¡Información Opcional!")) {
                             if( frase.contains("desactiva") || frase.contains("quita") || frase.contains("quítame") && !frase.contains("activa")){
                                 alertas();
                             }
                         }

                     }else if(frase.contains("modo apoyo")){
                         if(basico.getText().equals("Modo Lazarillo")){
                             basico();
                         }else if(frase.contains("desactivar") ||frase.contains("desactiva")){
                             basico();
                         }

                     }else if(frase.contains("modo lazarillo")){
                         if(basico.getText().equals("Modo Apoyo")){
                             basico();
                         }

                     } else if(frase.contains("configuracion")||frase.contains("configuraciones")){
                         optionLayout();
                     }else if(frase.contains("ayuda")||frase.contains("ayudas")){
                         ayudar();
                     }
                 } */
                 binding.btnMicro.setImageResource(R.drawable.baseline_mic_24);
             }

             @Override
             public void onPartialResults(Bundle partialResults) {
                 Log.d("SpeechRecognizer", "onPartialResults");
             }

             @Override
             public void onEvent(int eventType, Bundle params) {
                 Log.d("SpeechRecognizer", "onEvent");
             }
         });
     }


    }



