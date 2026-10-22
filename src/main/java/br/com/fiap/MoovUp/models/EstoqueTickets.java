package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;

import java.util.List;
import java.util.ArrayList;

public class EstoqueTickets {
    private List<Ticket> tickets;
    private int capacidadeMaxima;
    private int qtdDisponivel;

    public EstoqueTickets(int capacidadeMaxima) {
        this.tickets = new ArrayList<>();
        this.capacidadeMaxima = capacidadeMaxima;
        this.qtdDisponivel = 0;
    }


    public void adicionarTicket(Ticket t){
        if (tickets.size() >= capacidadeMaxima) {
            System.out.println("Estoque no limite maximo.");
        } else {
            tickets.add(t);
            qtdDisponivel++;
        }
    }


    public void setCapacidadeMaxima(int capacidadeMaxima) {
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("A capacidade máxima deve ser maior que zero.");
        }
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public void alterarCapacidadeMaxima(int novaCapacidade) {
        setCapacidadeMaxima(novaCapacidade);
    }

    public Ticket buscarDisponivel(Modalidade modalidade){
        for (Ticket t : tickets) {
            if (t instanceof TicketDigital) {
                TicketDigital td = (TicketDigital) t;

                if (td.getModalidade().equals(modalidade)
                        && td.getStatus() == StatusTicket.DISPONIVEL
                        && td.validar()) {
                    return td;
                }
            }
        }

        System.out.println("Nenhum ticket disponivel para " + modalidade);
        return null;
    }

    public void alertarEstoqueBaixo(){
        int disponiveis = 0;

        for (Ticket t : tickets) {
            if (t.getStatus() == StatusTicket.DISPONIVEL) {
                disponiveis++;
            }
        }

        double percentual = (double) disponiveis / capacidadeMaxima;
        if (percentual < 0.2){
            System.out.println("ALERTA: Estoque baixo! apenas " + disponiveis + " tickets disponiveis.");
        }
    }

    public String relatorioEstoque(){
        int disponiveis = 0;
        int reservados = 0;
        int utilizados = 0;
        int expirados = 0;

        for (Ticket t : tickets) {
            if (t.getStatus() == StatusTicket.DISPONIVEL) {
                disponiveis++;
            } else if (t.getStatus() == StatusTicket.RESERVADO) {
                reservados++;
            } else if (t.getStatus() == StatusTicket.UTILIZADO) {
                utilizados++;
            } else if (t.getStatus() == StatusTicket.EXPIRADO) {
                expirados++;
            }
        }

        String relatorio = "Capacidade maxima : " + capacidadeMaxima + "\n" +
                "Disponiveis       : " + disponiveis + "\n" +
                "Reservados        : " + reservados + "\n" +
                "Utilizados        : " + utilizados + "\n" +
                "Expirados         : " + expirados;

        return relatorio;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getQtdDisponivel() {
        return qtdDisponivel;
    }

    public void setQtdDisponivel(int qtdDisponivel) {
        this.qtdDisponivel = qtdDisponivel;
    }
}
