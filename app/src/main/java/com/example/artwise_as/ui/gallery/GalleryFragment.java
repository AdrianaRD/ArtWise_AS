package com.example.artwise_as.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwise_as.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    RecyclerView recyclerView;
    List<PF_Preguntas> pf_questionList;

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
        PF_Adapter pf_adapter = new PF_Adapter(pf_questionList);
        recyclerView.setAdapter(pf_adapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {

        pf_questionList = new ArrayList<>();
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_1), getResources().getString(R.string.answer_1)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_2), getResources().getString(R.string.answer_2)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_3), getResources().getString(R.string.answer_3)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_4), getResources().getString(R.string.answer_4)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_5), getResources().getString(R.string.answer_5)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_6), getResources().getString(R.string.answer_6)));
        pf_questionList.add(new PF_Preguntas(getResources().getString(R.string.question_7), getResources().getString(R.string.answer_7)));

    }
}