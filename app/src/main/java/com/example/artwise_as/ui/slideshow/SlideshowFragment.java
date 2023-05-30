package com.example.artwise_as.ui.slideshow;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwise_as.R;
import com.example.artwise_as.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

         RecyclerView recyclerView= binding.recyclerview;
         List <Item> items=new ArrayList<>();
        items.add(new Item("ArtWise",getResources().getString(R.string.label_text1),R.drawable.logo2));
        items.add(new Item(getResources().getString(R.string.label_titu),getResources().getString(R.string.label_text2),R.drawable.museo));
        items.add(new Item("Beacons",getResources().getString(R.string.label_text3),R.drawable.beacons));
        items.add(new Item(getResources().getString(R.string.label_titu2),getResources().getString(R.string.label_text4),R.drawable.monalisa));
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         recyclerView.setAdapter(new MyAdapter(items));
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}