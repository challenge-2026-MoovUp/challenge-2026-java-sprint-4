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
    public boolean validar() {
        return (this.status == StatusTicket.DISPONIVEL || this.status == StatusTicket.RESERVADO)
                && this.dataExpiracao != null
                && LocalDate.now().isBefore(this.dataExpiracao);
    }

    @Override
    public boolean confirmarUsoTicket(String idTicket, Usuario usuario) {
        if (!super.confirmarUsoTicket(idTicket, usuario)) {
            return false;
        }

        this.invalidarAposUso();
        return true;
    }

    @Override
    public int calcularCustoEmPontos() {
        return (int) Math.floor(this.tarifaReais / 0.009);
    }

    @Override
    public boolean resgatar(Usuario usuario) {
        int custo = calcularCustoEmPontos();

        if (usuario == null) {
            System.out.println("Usuário não informado.");
            return false;
        }

        if (!validar()) {
            System.out.println("Ticket inválido ou expirado.");
            return false;
        }

        if (usuario.consultarSaldo() < custo) {
            System.out.println("Necessário: " + custo + "pts // Disponível na sua conta: " + usuario.consultarSaldo() + " pts");
            return false;
        }

        usuario.adicionarPontos(-custo);
        this.status = StatusTicket.RESERVADO;
        this.qrCodeHash = gerarQRCode();
        if (!usuario.consultarHistorico().contains(this)) {
            usuario.consultarHistorico().add(this);
        }
        usuario.getImpactoAmbiental().calcularValoresPorModalidade(this.modalidade);
        return true;
    }

    public void invalidarAposUso(){
        this.status = StatusTicket.UTILIZADO;
    }


}
