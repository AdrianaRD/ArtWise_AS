package com.example.artwise_as.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.artwise_as.Menu2Activity;
import com.example.artwise_as.R;
import com.example.artwise_as.databinding.FragmentHomeBinding;
import java.util.ArrayList;



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
                changeSpeed();
            }
        });

    }//fin onClick

    public void changeText(){
        if(binding.btnComenzar.getText().equals(getResources().getString(R.string.label_comenzar))){
            binding.btnComenzar.setText(getResources().getString(R.string.label_buscando));
        }else{
            binding.btnComenzar.setText(getResources().getString(R.string.label_comenzar));

        }
    }
    public void scaneo(){
        changeText();
        if(binding.btnComenzar.getText().equals(getResources().getString(R.string.label_buscando))){
            activity.startScanning();
        }else {
            activity.stopScanning();
        }

        }

    public void changeSpeed() {
        String buttonText = binding.btnVelocidad.getText().toString();
        if(buttonText.equals("x1")){
            binding.btnVelocidad.setText("x1.5");
            activity.changeSpeedOne();
        }else if(buttonText.equals("x1.5")){
            binding.btnVelocidad.setText("x2");
            activity.changeSpeedTwo();
        }else if(buttonText.equals("x2")){
            binding.btnVelocidad.setText("x1");
            activity.changeSpeedThree();

        }

    }

    public void Onmicro(){
        activity.vibration();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(getContext(), getResources().getString(R.string.audio_perimmision), Toast.LENGTH_LONG).show();
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
                Log.d("SpeechRecognizer", "Recognition failed, check if the microphone is available.");
            }
            binding.btnMicro.setImageResource(R.drawable.baseline_mic_black);
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
                 binding.btnMicro.setImageResource(R.drawable.baseline_mic_24);
             }

             @Override
             public void onResults(Bundle results) {
                 ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 if (result != null) {
                     String frase=result.get(0);
                     frase.toLowerCase();
                     if(frase.contains(getResources().getString(R.string.label_voz_comenzar)) || frase.contains(getResources().getString(R.string.label_voz_comenzar1)) || frase.contains(getResources().getString(R.string.label_voz_comenzar2))){
                         if(binding.btnComenzar.getText().equals(getResources().getString(R.string.label_comenzar))) {scaneo();}
                     }else if(frase.contains(getResources().getString(R.string.label_voz_termina)) || frase.contains(getResources().getString(R.string.label_voz_termina1)) || frase.contains(getResources().getString(R.string.label_voz_termina2))){
                         if(binding.btnComenzar.getText().equals(getResources().getString(R.string.label_buscando))) {scaneo();}
                     }else if(frase.contains(getResources().getString(R.string.label_voz_repe))||frase.contains(getResources().getString(R.string.label_voz_repe1))||frase.contains(getResources().getString(R.string.label_voz_repe2))){
                         activity.repeOBRA();
                     }else if(frase.contains(getResources().getString(R.string.label_voz_info))){
                         activity.infoOBRA();
                     }else if(frase.contains(getResources().getString(R.string.label_voz_velocidad))){
                         changeSpeed();
                     }


                 }else {
                     Log.e("SpeechRecognizer", "onError: ");
                 }
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



