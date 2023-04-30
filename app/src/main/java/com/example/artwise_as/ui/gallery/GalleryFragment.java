package com.example.artwise_as.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwise_as.PF_Adapter;
import com.example.artwise_as.PF_Preguntas;
import com.example.artwise_as.R;
import com.example.artwise_as.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    RecyclerView recyclerView;
    List<PF_Preguntas> pf_preguntasList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        initData();
        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        PF_Adapter pf_adapter = new PF_Adapter(pf_preguntasList);
        recyclerView.setAdapter(pf_adapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {

        pf_preguntasList = new ArrayList<>();
        pf_preguntasList.add(new PF_Preguntas("¿Necesito tener una conexión a Internet para usar la aplicación en el museo?",
                "Si, es necesario para el correcto funcionamiento de la apliación."));
        pf_preguntasList.add(new PF_Preguntas("¿Cómo puedo saber qué información se proporciona en la aplicación sobre cada obra de arte?",
                "La aplicación tiene una configuración estándar que muestra información básica sobre una obra cuando se detecta un beacon. Al hacer clic en el botón '+ Información', se puede obtener información más detallada y curiosa sobre la obra."));
        pf_preguntasList.add(new PF_Preguntas("¿La aplicación está disponible en varios idiomas?",
                "Actualmente solo está disponible en español, pero en futuras actualizaciones estará disponible en otros idiomas como inglés, francés, portugués y alemán."));
        pf_preguntasList.add(new PF_Preguntas("¿Cómo puedo reportar problemas técnicos o errores en la aplicación? ",
                "En la pantalla de incio hay un boton en la esquina inferior derecha con forma de sobre'" +
                        ", este permite a los usuarios reportar errores encontrados en la aplicación mediante correo electrónico. "));

        pf_preguntasList.add(new PF_Preguntas("¿Cómo puedo encontrar los beacons en el museo para poder usar la aplicación?",
                "Los beacons están ubicados estratégicamente junto a cada obra, lo que permite que la información sobre las obras se lea a medida que se visite el museo."));
        pf_preguntasList.add(new PF_Preguntas("¿La aplicación proporciona información sobre la historia y la cultura detrás de las obras de arte?",
                "Si, al hacer clic en el botón '+ Información', se mostrará información más detallada sobre cada obra. "));
        pf_preguntasList.add(new PF_Preguntas("¿La aplicación requiere que active la ubicación en mi dispositivo móvil para funcionar?  ",
                "Si, para el correcto funcionamiento de la aplicación es imprescindible tanto la conectividad de la ubicación como la del bluetooth."));
    }
}