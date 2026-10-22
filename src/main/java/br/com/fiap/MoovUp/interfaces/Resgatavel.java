package br.com.fiap.MoovUp.interfaces;

import br.com.fiap.MoovUp.models.Usuario;

public interface Resgatavel {
    boolean resgatar(Usuario usuario);
    int calcularCustoEmPontos();
}
