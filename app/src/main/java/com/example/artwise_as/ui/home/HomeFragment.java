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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.artwise_as.Menu2Activity;
import com.example.artwise_as.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Menu2Activity activity;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        onClick();

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
                Toast.makeText(getContext(), "¡Haz hecho clic en el botón micro!", Toast.LENGTH_SHORT).show();
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

    }



