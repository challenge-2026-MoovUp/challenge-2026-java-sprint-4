package br.com.fiap.MoovUp.dto;

// GET /usuarios/{id}/impacto: soma de todos os impactos do usuário
public record ImpactoResumoResponse(String idUsuario, int totalViagens, double totalKm, double totalCo2Evitado) {
}
