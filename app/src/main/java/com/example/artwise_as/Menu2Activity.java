package com.example.artwise_as;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.artwise_as.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwise_as.databinding.ActivityMenu2Binding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Menu2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenu2Binding binding;

    private String text;
    private String titulo;
    private String descripcion;
    private int conversionRSSI;
    private ArrayList<String> leidos = new ArrayList<String>();

    private boolean scanning;

    //permisos
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private static final int REQUEST_BLUETOOTH_CONNECT = 2;

    private BluetoothAdapter mBluetoothAdapter;

    private BroadcastReceiver mBluetoothReceiver;

    private BluetoothLeScanner bluetoothLeScanner;

    private Handler handler;

    //to speech variable
    private TextToSpeech tts;

    private FirebaseFirestore db;

    //instancia fragment
    private HomeFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenu2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu2.toolbar);
        binding.appBarMenu2.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkPermission();
        checkPermission2();
        bluetoothConection();
        toSpeecFuntion();
        fireBaseConecction();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void checkPermission(){
        //activacion de ubicacion
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }


        //activacion de bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Aquí se solicita el permiso de conexión Bluetooth si aún no se ha concedido.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_CONNECT);
                return;
            }
            startActivity(enableBtIntent);
        }
    }
    private void checkPermission2(){
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    public void bluetoothConection(){
        //instancia de los bluetooth
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
    }

    public void toSpeecFuntion(){
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Menu2Activity.this, "Idioma no soportado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Menu2Activity.this, "Error al inicializar TextToSpeech", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //**************
    public void repeOBRA(){
        if (titulo != null && titulo !="") {
            tts.speak(titulo, TextToSpeech.QUEUE_ADD, null, null);
        }else{
            tts.speak("debes comenzar trayecto.", TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void infoOBRA(){
        if (descripcion != null && descripcion !="") {
            tts.speak(descripcion, TextToSpeech.QUEUE_ADD, null, null);
        }else{
            tts.speak("debes comenzar trayecto.", TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    //*************************

    public void fireBaseConecction(){
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void startScanning() {
        if (!scanning) {
            // Iniciar el escaneo
            scanning = true;
            text = "Comenzamos.";
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //stopScanning();
                }
            }, 0); // Escanear durante 5 segundo
            bluetoothLeScanner.startScan(new BeaconScanCallback());
        }
        }


    public void stopScanning() {
            if(scanning){
            text = "Viaje terminado.";
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
            scanning = false;
            leidos.clear();
            titulo="";
            descripcion="";
            bluetoothLeScanner.stopScan(new BeaconScanCallback());

    }}


    //*******************************clase para leer beacons***********

    private class BeaconScanCallback extends ScanCallback {
        private List<ScanFilter> filters;

        public BeaconScanCallback(List<ScanFilter> filters) {
            this.filters = filters;
        }

        public BeaconScanCallback() {
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            String nombre=""+result.getDevice().getName();
            String cod=result.getDevice().getAddress();
            int distancia =result.getRssi();

                if(scanning){
                if (!leidos.contains(cod)) {
                    Log.d("MainActivity", "Beacon descubierto: " + result.getDevice().getAddress() + " " + result.getDevice().getName() + " " + result.getRssi());

                    // Obtener los datos del documento "cod" de la colección "BEACONS" de Firebase Firestore
                    db.collection("BEACONS")
                            .document(cod)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // Obtener los valores de los campos "titulo" y "descripcion"
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            titulo = document.getString("titulo");
                                            descripcion = document.getString("descripcion");
                                            conversionRSSI=Integer.parseInt(document.getString("rssi"));

                                            if(distancia>=conversionRSSI && !leidos.contains(cod)){
                                                leidos.add(cod);
                                                tts.speak(titulo, TextToSpeech.QUEUE_ADD, null, null);

                                            }

                                        } else {
                                            Log.d(TAG, "El documento no existe");
                                        }
                                    } else {
                                        Log.d(TAG, "Error al obtener los datos del documento", task.getException());
                                    }
                                }
                            });
                }}

        }

    }

}


