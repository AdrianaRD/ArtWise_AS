package com.example.artwise_as.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwise_as.R;

import java.util.List;

public class PF_Adapter extends RecyclerView.Adapter<PF_Adapter.PF_VH> {

    List<PF_Preguntas> pf_questionList;

    public PF_Adapter(List<PF_Preguntas> pf_questionList) {
        this.pf_questionList = pf_questionList;
    }

    @NonNull
    @Override
    public PF_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_filas_fragment, parent, false);
        return new PF_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PF_VH holder, int position) {
        PF_Preguntas pf_preguntas = pf_questionList.get(position);
        holder.questionTxt.setText(pf_preguntas.getPregunta());
        holder.answerTxt.setText(pf_preguntas.getRespuesta());

        boolean isExpandable = pf_preguntas.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return pf_questionList.size();
    }

    public class PF_VH extends RecyclerView.ViewHolder {

        TextView questionTxt, answerTxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public PF_VH(@NonNull View itemView) {
            super(itemView);

            questionTxt = itemView.findViewById(R.id.pregunta);
            answerTxt = itemView.findViewById(R.id.respuesta);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(view -> {
                PF_Preguntas pf_preguntas = pf_questionList.get(getBindingAdapterPosition());
                pf_preguntas.setExpandable(!pf_preguntas.isExpandable());
                notifyItemChanged(getBindingAdapterPosition());
            });
        }
    }
}
