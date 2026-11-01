package br.com.fiap.MoovUp.enums;

import java.util.List;

// Linhas de São Paulo com as estações na ordem do trajeto (listas do usuário, 2026).
// O código é a cor, que não se repete. Os monotrilhos (Prata e Ouro) entram como METRO.
public enum Linha {
    AZUL("Linha 1 - Azul", Modalidade.METRO,
            Estacao.JABAQUARA, Estacao.CONCEICAO, Estacao.SAO_JUDAS, Estacao.SAUDE, Estacao.PRACA_DA_ARVORE,
            Estacao.SANTA_CRUZ, Estacao.VILA_MARIANA, Estacao.ANA_ROSA, Estacao.PARAISO, Estacao.VERGUEIRO,
            Estacao.SAO_JOAQUIM, Estacao.JAPAO_LIBERDADE, Estacao.SE, Estacao.SAO_BENTO, Estacao.LUZ,
            Estacao.TIRADENTES, Estacao.ARMENIA, Estacao.PORTUGUESA_TIETE, Estacao.CARANDIRU, Estacao.SANTANA,
            Estacao.JARDIM_SAO_PAULO, Estacao.PARADA_INGLESA, Estacao.TUCURUVI),
    VERDE("Linha 2 - Verde", Modalidade.METRO,
            Estacao.VILA_PRUDENTE, Estacao.TAMANDUATEI, Estacao.SACOMA, Estacao.ALTO_DO_IPIRANGA,
            Estacao.SANTOS_IMIGRANTES, Estacao.CHACARA_KLABIN, Estacao.ANA_ROSA, Estacao.PARAISO,
            Estacao.BRIGADEIRO, Estacao.TRIANON_MASP, Estacao.CONSOLACAO, Estacao.CLINICAS, Estacao.SUMARE,
            Estacao.VILA_MADALENA),
    VERMELHA("Linha 3 - Vermelha", Modalidade.METRO,
            Estacao.CORINTHIANS_ITAQUERA, Estacao.ARTUR_ALVIM, Estacao.PATRIARCA, Estacao.GUILHERMINA_ESPERANCA,
            Estacao.VILA_MATILDE, Estacao.PENHA, Estacao.CARRAO, Estacao.TATUAPE, Estacao.BELEM,
            Estacao.BRESSER_MOOCA, Estacao.BRAS, Estacao.PEDRO_II, Estacao.SE, Estacao.ANHANGABAU,
            Estacao.REPUBLICA, Estacao.SANTA_CECILIA, Estacao.MARECHAL_DEODORO, Estacao.PALMEIRAS_BARRA_FUNDA),
    AMARELA("Linha 4 - Amarela", Modalidade.METRO,
            Estacao.LUZ, Estacao.REPUBLICA, Estacao.HIGIENOPOLIS_MACKENZIE, Estacao.PAULISTA,
            Estacao.OSCAR_FREIRE, Estacao.FRADIQUE_COUTINHO, Estacao.FARIA_LIMA, Estacao.PINHEIROS,
            Estacao.BUTANTA, Estacao.SAO_PAULO_MORUMBI, Estacao.VILA_SONIA),
    LILAS("Linha 5 - Lilás", Modalidade.METRO,
            Estacao.CAPAO_REDONDO, Estacao.CAMPO_LIMPO, Estacao.VILA_DAS_BELEZAS, Estacao.GIOVANNI_GRONCHI,
            Estacao.SANTO_AMARO, Estacao.LARGO_TREZE, Estacao.ADOLFO_PINHEIRO, Estacao.ALTO_DA_BOA_VISTA,
            Estacao.BORBA_GATO, Estacao.BROOKLIN, Estacao.CAMPO_BELO, Estacao.EUCALIPTOS, Estacao.MOEMA,
            Estacao.AACD_SERVIDOR, Estacao.HOSPITAL_SAO_PAULO, Estacao.SANTA_CRUZ, Estacao.CHACARA_KLABIN),
    LARANJA("Linha 6 - Laranja", Modalidade.METRO,
            Estacao.BRASILANDIA, Estacao.MARISTELA, Estacao.ITABERABA, Estacao.JOAO_PAULO_I,
            Estacao.FREGUESIA_DO_O, Estacao.SANTA_MARINA, Estacao.AGUA_BRANCA, Estacao.SESC_POMPEIA,
            Estacao.PERDIZES, Estacao.PUC_CARDOSO_DE_ALMEIDA, Estacao.FAAP_PACAEMBU,
            Estacao.HIGIENOPOLIS_MACKENZIE, Estacao.QUATORZE_BIS, Estacao.BELA_VISTA, Estacao.SAO_JOAQUIM),
    PRATA("Linha 15 - Prata", Modalidade.METRO,
            Estacao.VILA_PRUDENTE, Estacao.ORATORIO, Estacao.SAO_LUCAS, Estacao.CAMILO_HADDAD,
            Estacao.VILA_TOLSTOI, Estacao.VILA_UNIAO, Estacao.JARDIM_PLANALTO, Estacao.SAPOPEMBA,
            Estacao.FAZENDA_DA_JUTA, Estacao.SAO_MATEUS, Estacao.JARDIM_COLONIAL),
    OURO("Linha 17 - Ouro", Modalidade.METRO,
            Estacao.MORUMBI, Estacao.CHUCRI_ZAIDAN, Estacao.VILA_CORDEIRO, Estacao.CAMPO_BELO,
            Estacao.VEREADOR_JOSE_DINIZ, Estacao.BROOKLIN_PAULISTA, Estacao.AEROPORTO_CONGONHAS,
            Estacao.WASHINGTON_LUIS),

    RUBI("Linha 7 - Rubi", Modalidade.TREM,
            Estacao.PALMEIRAS_BARRA_FUNDA, Estacao.AGUA_BRANCA, Estacao.LAPA, Estacao.PIQUERI, Estacao.PIRITUBA,
            Estacao.VILA_CLARICE, Estacao.JARAGUA, Estacao.VILA_AURORA, Estacao.PERUS, Estacao.CAIEIRAS,
            Estacao.FRANCO_DA_ROCHA, Estacao.BALTAZAR_FIDELIS, Estacao.FRANCISCO_MORATO, Estacao.BOTUJURU,
            Estacao.CAMPO_LIMPO_PAULISTA, Estacao.VARZEA_PAULISTA, Estacao.JUNDIAI),
    DIAMANTE("Linha 8 - Diamante", Modalidade.TREM,
            Estacao.JULIO_PRESTES, Estacao.PALMEIRAS_BARRA_FUNDA, Estacao.LAPA, Estacao.DOMINGOS_DE_MORAES,
            Estacao.IMPERATRIZ_LEOPOLDINA, Estacao.PRESIDENTE_ALTINO, Estacao.OSASCO, Estacao.COMANDANTE_SAMPAIO,
            Estacao.QUITAUNA, Estacao.GENERAL_MIGUEL_COSTA, Estacao.CARAPICUIBA, Estacao.SANTA_TEREZINHA,
            Estacao.ANTONIO_JOAO, Estacao.BARUERI, Estacao.JARDIM_BELVAL, Estacao.JARDIM_SILVEIRA,
            Estacao.JANDIRA, Estacao.SAGRADO_CORACAO, Estacao.ENGENHEIRO_CARDOSO, Estacao.ITAPEVI,
            Estacao.SANTA_RITA, Estacao.AMBUITA, Estacao.AMADOR_BUENO),
    ESMERALDA("Linha 9 - Esmeralda", Modalidade.TREM,
            Estacao.OSASCO, Estacao.PRESIDENTE_ALTINO, Estacao.CEASA, Estacao.VILLA_LOBOS_JAGUARE,
            Estacao.CIDADE_UNIVERSITARIA, Estacao.PINHEIROS, Estacao.HEBRAICA_REBOUCAS, Estacao.CIDADE_JARDIM,
            Estacao.VILA_OLIMPIA, Estacao.BERRINI, Estacao.MORUMBI_CLARO, Estacao.GRANJA_JULIETA,
            Estacao.JOAO_DIAS, Estacao.SANTO_AMARO, Estacao.SOCORRO, Estacao.JURUBATUBA, Estacao.AUTODROMO,
            Estacao.PRIMAVERA_INTERLAGOS, Estacao.GRAJAU, Estacao.MENDES_VILA_NATAL, Estacao.VARGINHA),
    TURQUESA("Linha 10 - Turquesa", Modalidade.TREM,
            Estacao.PALMEIRAS_BARRA_FUNDA, Estacao.LUZ, Estacao.BRAS, Estacao.JUVENTUS_MOOCA, Estacao.IPIRANGA,
            Estacao.TAMANDUATEI, Estacao.SAO_CAETANO_DO_SUL, Estacao.UTINGA, Estacao.PREFEITO_SALADINO,
            Estacao.SANTO_ANDRE, Estacao.CAPUAVA, Estacao.MAUA, Estacao.GUAPITUBA, Estacao.RIBEIRAO_PIRES,
            Estacao.RIO_GRANDE_DA_SERRA),
    CORAL("Linha 11 - Coral", Modalidade.TREM,
            Estacao.PALMEIRAS_BARRA_FUNDA, Estacao.LUZ, Estacao.BRAS, Estacao.TATUAPE,
            Estacao.CORINTHIANS_ITAQUERA, Estacao.DOM_BOSCO, Estacao.JOSE_BONIFACIO, Estacao.GUAIANASES,
            Estacao.ANTONIO_GIANETTI_NETO, Estacao.FERRAZ_DE_VASCONCELOS, Estacao.POA, Estacao.CALMON_VIANA,
            Estacao.SUZANO, Estacao.JUNDIAPEBA, Estacao.BRAZ_CUBAS, Estacao.MOGI_DAS_CRUZES, Estacao.ESTUDANTES),
    SAFIRA("Linha 12 - Safira", Modalidade.TREM,
            Estacao.BRAS, Estacao.TATUAPE, Estacao.ENGENHEIRO_GOULART, Estacao.USP_LESTE,
            Estacao.COMENDADOR_ERMELINO, Estacao.SAO_MIGUEL_PAULISTA, Estacao.JARDIM_HELENA,
            Estacao.ITAIM_PAULISTA, Estacao.JARDIM_ROMANO, Estacao.ENGENHEIRO_MANOEL_FEIO,
            Estacao.ITAQUAQUECETUBA, Estacao.ARACARE, Estacao.CALMON_VIANA),
    JADE("Linha 13 - Jade", Modalidade.TREM,
            Estacao.ENGENHEIRO_GOULART, Estacao.GUARULHOS_CECAP, Estacao.AEROPORTO_GUARULHOS);

    private final String nome;
    private final Modalidade modalidade;
    private final List<Estacao> estacoes;

    // "Estacao... estacoes" aceita quantas estações forem passadas
    Linha(String nome, Modalidade modalidade, Estacao... estacoes) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.estacoes = List.of(estacoes);
    }

    public String getNome() {
        return nome;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public List<Estacao> getEstacoes() {
        return estacoes;
    }
}
