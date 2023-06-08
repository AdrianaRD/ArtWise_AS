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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Menu2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenu2Binding binding;

    private String text;
    private String titulo;
    private String descripcion;

    private String tituloDB;
    private String descriptionDB;
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


    private BluetoothLeScanner bluetoothLeScanner;

    private Handler handler;

    //to speech variable
    private TextToSpeech tts;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenu2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu2.toolbar);
        binding.appBarMenu2.fab.setOnClickListener(view -> reporte());
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
        langaugeDataBase();

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void checkPermission() {
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

    private void checkPermission2() {
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    public void bluetoothConection() {
        //instancia de los bluetooth
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
    }

    public void toSpeecFuntion() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Menu2Activity.this, getResources().getString(R.string.soported_laguage), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Menu2Activity.this, getResources().getString(R.string.error_toexpect), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //**************
    public void repeOBRA() {
        if (titulo != null && titulo != "") {
            tts.speak(titulo, TextToSpeech.QUEUE_ADD, null, null);
        } else {
            tts.speak(getResources().getString(R.string.init_ride), TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void infoOBRA() {
        if (descripcion != null && descripcion != "") {
            tts.speak(descripcion, TextToSpeech.QUEUE_ADD, null, null);
        } else {
            tts.speak(getResources().getString(R.string.init_ride), TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    //*************************

    public void fireBaseConecction() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void startScanning() {
        if (!scanning) {
            // Iniciar el escaneo
            scanning = true;
            text = getResources().getString(R.string.label_start);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //stopScanning();
                }
            }, 0); // Escanear durante 5 segundo
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            }
            bluetoothLeScanner.startScan(new BeaconScanCallback());
        }
    }
    public void stopScanning() {
        if (scanning) {
            text = getResources().getString(R.string.label_finish);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
            scanning = false;
            leidos.clear();
            titulo = "";
            descripcion = "";
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            }
            bluetoothLeScanner.stopScan(new BeaconScanCallback());

        }
    }

    public void reporte() {
        text = getResources().getString(R.string.eviar_reporte);
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"samuelmatas1997@gmail.com"});
        String infoBeacon = getResources().getString(R.string.label_sin_nave);
        if (!leidos.isEmpty()) {
            infoBeacon = "" + leidos.get(leidos.size() - 1);
        }
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.label_report_error) + infoBeacon);
        i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.label_no_funtion));
        try {
            startActivity(Intent.createChooser(i, getResources().getString(R.string.label_envi_correo)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Menu2Activity.this, getResources().getString(R.string.label_correo_ins), Toast.LENGTH_SHORT).show();
        }
    }

    public void changeSpeedOne() {
        float speed = 1.5f;
        tts.setSpeechRate(speed);
        text = getResources().getString(R.string.label_velo15);
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void changeSpeedTwo() {
        float speed = 2.0f;
        tts.setSpeechRate(speed);
        text = getResources().getString(R.string.label_velo2);
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void changeSpeedThree() {
        float speed = 1.0f;
        tts.setSpeechRate(speed);
        text = getResources().getString(R.string.label_velonormal);
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void langaugeDataBase() {
        //saber idioma
        Configuration config = getResources().getConfiguration();

        // Obtener el idioma actual del dispositivo
        String deviceLanguage = config.locale.getLanguage();
        if (deviceLanguage.equals("en")) {
            tituloDB = "titulo_en";
            descriptionDB = "descripcion_en";
        } else {
            tituloDB = "titulo";
            descriptionDB = "descripcion";
        }
    }

    public void vibration() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    //*******************************clase para leer beacons***********

    private class BeaconScanCallback extends ScanCallback {
        public BeaconScanCallback() {
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
            String nombre = "" + result.getDevice().getName();
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
                                            conversionRSSI=Integer.parseInt(document.getString("rssi"));

                                            if(distancia>=conversionRSSI && !leidos.contains(cod)){
                                                titulo = document.getString(tituloDB);
                                                leidos.add(cod);
                                                tts.speak(titulo, TextToSpeech.QUEUE_ADD, null, null);
                                                descripcion = document.getString(descriptionDB);

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


