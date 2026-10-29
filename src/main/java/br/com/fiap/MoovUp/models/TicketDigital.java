package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TicketDigital extends Ticket {
    private String qrCodeHash;
    private Modalidade modalidade;

    public TicketDigital(String id, Modalidade modalidade){
        this.id = id;
        this.modalidade = modalidade;
        this.status = StatusTicket.DISPONIVEL;
        this.tarifaReais = modalidade.getTarifa();
        this.dataExpiracao = LocalDate.now().plusDays(2);
        this.custoEmPontos = calcularCustoEmPontos();
    }

    @Override
    public int calcularCustoEmPontos() {
        return (int) Math.floor(this.tarifaReais / 0.009);
    }

    // Chamado pelo service depois de validar o ticket e o saldo do usuário.
    @Override
    public void resgatar(Usuario usuario) {
        usuario.adicionarPontos(-calcularCustoEmPontos());
        this.idUsuario = usuario.getId();
        this.status = StatusTicket.RESERVADO;
        this.qrCodeHash = gerarQRCode();
    }
}
