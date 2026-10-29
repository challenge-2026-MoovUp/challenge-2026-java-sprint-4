package br.com.fiap.MoovUp.interfaces;

import br.com.fiap.MoovUp.models.Usuario;

public interface Resgatavel {
    void resgatar(Usuario usuario);
    int calcularCustoEmPontos();
}
