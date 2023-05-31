package com.example.artwise_as.ui.gallery;

import androidx.annotation.NonNull;

public class PF_Preguntas {
    private final String pregunta;
    private final String respuesta;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public PF_Preguntas(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.expandable = false;
    }

    public String getPregunta() {
        return pregunta;
    }
    public String getRespuesta() {
        return respuesta;
    }

    @NonNull
    @Override
    public String toString() {
        return "PF_Preguntas{" +
                "pregunta='" + pregunta + '\'' +
                ", respuesta='" + respuesta + '\'' +
                '}';
    }
}
