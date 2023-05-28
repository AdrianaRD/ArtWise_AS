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
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_1), getResources().getString(R.string.answer_1)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_2), getResources().getString(R.string.answer_2)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_3), getResources().getString(R.string.answer_3)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_4), getResources().getString(R.string.answer_4)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_5), getResources().getString(R.string.answer_5)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_6), getResources().getString(R.string.answer_6)));
        pf_preguntasList.add(new PF_Preguntas(getResources().getString(R.string.question_7), getResources().getString(R.string.answer_7)));

    }
}