-- =====================================================================
-- MoovUp - alter.sql
-- Leva as tabelas que já existiam no Oracle (Sprint 3) até o estado
-- descrito no ddl.sql. Sem DROP de tabela. Rodar na ordem.
-- =====================================================================


-- ---------------------------------------------------------------------
-- 27/10 (JÁ EXECUTADO) - IDs de POSTAGEM/COMENTARIO viram VARCHAR2 + SENHA
-- A FK precisou ser apagada e recriada, porque o Oracle não deixa mudar
-- o tipo de uma coluna ligada por FK (ORA-02267).
-- ---------------------------------------------------------------------
-- ALTER TABLE COMENTARIO DROP CONSTRAINT FK_COMENTARIO_POSTAGEM;
-- ALTER TABLE POSTAGEM   MODIFY POSTAGEM_ID   VARCHAR2(36);
-- ALTER TABLE COMENTARIO MODIFY COMENTARIO_ID VARCHAR2(36);
-- ALTER TABLE COMENTARIO MODIFY POSTAGEM_ID   VARCHAR2(36);
-- ALTER TABLE COMENTARIO ADD CONSTRAINT FK_COMENTARIO_POSTAGEM
--     FOREIGN KEY (POSTAGEM_ID) REFERENCES POSTAGEM (POSTAGEM_ID) ON DELETE CASCADE;
-- ALTER TABLE USUARIO_MOOVUP ADD SENHA VARCHAR2(100) DEFAULT 'trocar123' NOT NULL;


-- ---------------------------------------------------------------------
-- 28/10 - 1) USUARIO_MOOVUP: SEGUIDORES sai (não existe no app)
-- ---------------------------------------------------------------------
ALTER TABLE USUARIO_MOOVUP DROP COLUMN SEGUIDORES;


-- ---------------------------------------------------------------------
-- 28/10 - 2) IDs de 20 caracteres não cabem um UUID (36).
-- Ficam VARCHAR2(50), o mesmo tamanho de USUARIO_MOOVUP.ID_USUARIO.
-- Aumentar o tamanho não perde dados.
-- ---------------------------------------------------------------------
-- O ticket deixa de apontar para o impacto: agora um ticket gera até
-- 2 impactos (um por viagem). A ligação fica VIAGEM -> IMPACTO.
ALTER TABLE TICKET_DIGITAL DROP CONSTRAINT FK_TICKET_IMPACTO;
ALTER TABLE TICKET_DIGITAL DROP COLUMN ID_IMPACTO;

ALTER TABLE TICKET_DIGITAL             MODIFY ID_USUARIO VARCHAR2(50);
ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS MODIFY ID_IMPACTO VARCHAR2(50);
ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS MODIFY ID_USUARIO VARCHAR2(50);
ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS MODIFY ID_TICKET  VARCHAR2(50);


-- ---------------------------------------------------------------------
-- 28/10 - 3) Grupo C: AUTOR (texto solto) vira ID_USUARIO com FK.
-- As duas tabelas estavam vazias. ON DELETE CASCADE: excluir o usuário
-- apaga as postagens e os comentários dele.
-- ---------------------------------------------------------------------
ALTER TABLE POSTAGEM RENAME COLUMN AUTOR TO ID_USUARIO;
ALTER TABLE POSTAGEM MODIFY ID_USUARIO VARCHAR2(50);
ALTER TABLE POSTAGEM ADD CONSTRAINT FK_POSTAGEM_USUARIO
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO_MOOVUP (ID_USUARIO) ON DELETE CASCADE;

ALTER TABLE COMENTARIO RENAME COLUMN AUTOR TO ID_USUARIO;
ALTER TABLE COMENTARIO MODIFY ID_USUARIO VARCHAR2(50);
ALTER TABLE COMENTARIO ADD CONSTRAINT FK_COMENTARIO_USUARIO
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO_MOOVUP (ID_USUARIO) ON DELETE CASCADE;


-- ---------------------------------------------------------------------
-- 28/10 - 4) Pedido do front: forma de recebimento, curtidas e viagens
-- ---------------------------------------------------------------------
ALTER TABLE TICKET_DIGITAL ADD FORMA_RECEBIMENTO VARCHAR2(10);

-- PK composta: o mesmo usuário não curte a mesma postagem duas vezes.
CREATE TABLE CURTIDA (
    ID_USUARIO   VARCHAR2(50) NOT NULL,
    POSTAGEM_ID  VARCHAR2(36) NOT NULL,
    DATA_CURTIDA DATE         NOT NULL,
    CONSTRAINT PK_CURTIDA PRIMARY KEY (ID_USUARIO, POSTAGEM_ID),
    CONSTRAINT FK_CURTIDA_USUARIO  FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO_MOOVUP (ID_USUARIO) ON DELETE CASCADE,
    CONSTRAINT FK_CURTIDA_POSTAGEM FOREIGN KEY (POSTAGEM_ID)
        REFERENCES POSTAGEM (POSTAGEM_ID) ON DELETE CASCADE
);

-- Viagem é histórico de uso real, então sem CASCADE (igual a ticket e impacto).
CREATE TABLE VIAGEM (
    ID_VIAGEM        VARCHAR2(36) NOT NULL,
    ID_USUARIO       VARCHAR2(50) NOT NULL,
    ID_TICKET        VARCHAR2(50) NOT NULL,
    LINHA            VARCHAR2(30) NOT NULL,
    ESTACAO_EMBARQUE VARCHAR2(50) NOT NULL,
    PONTOS_GERADOS   NUMBER       NOT NULL,
    DATA_VIAGEM      DATE         NOT NULL,
    CONSTRAINT PK_VIAGEM PRIMARY KEY (ID_VIAGEM),
    CONSTRAINT FK_VIAGEM_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO_MOOVUP (ID_USUARIO),
    CONSTRAINT FK_VIAGEM_TICKET  FOREIGN KEY (ID_TICKET)  REFERENCES TICKET_DIGITAL (ID_TICKET),
    CONSTRAINT CK_VIAGEM_PONTOS  CHECK (PONTOS_GERADOS >= 0)
);

-- Cada viagem gera um impacto. Nulo nos 2 impactos antigos (de antes das viagens).
ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS ADD ID_VIAGEM VARCHAR2(36);
ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS ADD CONSTRAINT FK_IMPACTO_VIAGEM
    FOREIGN KEY (ID_VIAGEM) REFERENCES VIAGEM (ID_VIAGEM);


-- ---------------------------------------------------------------------
-- 28/10 - 5) CHECKs: o banco aceita os mesmos valores que os enums do Java
-- ---------------------------------------------------------------------
ALTER TABLE USUARIO_MOOVUP ADD CONSTRAINT CK_USUARIO_STATUS CHECK (STATUS IN ('A', 'I'));
ALTER TABLE USUARIO_MOOVUP ADD CONSTRAINT CK_USUARIO_SALDO  CHECK (SALDO_PONTOS >= 0);

ALTER TABLE POSTAGEM ADD CONSTRAINT CK_POSTAGEM_TIPO CHECK (TIPO_POSTAGEM IN ('TEXTO', 'IMAGEM', 'VIDEO'));

ALTER TABLE TICKET_DIGITAL ADD CONSTRAINT CK_TICKET_STATUS
    CHECK (STATUS IN ('DISPONIVEL', 'RESERVADO', 'UTILIZADO', 'EXPIRADO'));
ALTER TABLE TICKET_DIGITAL ADD CONSTRAINT CK_TICKET_MODALIDADE
    CHECK (MODALIDADE IN ('ONIBUS', 'METRO', 'TREM'));
ALTER TABLE TICKET_DIGITAL ADD CONSTRAINT CK_TICKET_FORMA
    CHECK (FORMA_RECEBIMENTO IN ('QR_CODE', 'RECARGA'));

ALTER TABLE IMPACTO_AMBIENTAL_MOOVPASS ADD CONSTRAINT CK_IMPACTO_MODALIDADE
    CHECK (MODALIDADE IN ('ONIBUS', 'METRO', 'TREM'));


-- ---------------------------------------------------------------------
-- Conferência: deve listar as colunas/constraints novas
-- ---------------------------------------------------------------------
-- SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, NULLABLE FROM USER_TAB_COLUMNS
--  WHERE TABLE_NAME IN ('USUARIO_MOOVUP','POSTAGEM','COMENTARIO','TICKET_DIGITAL',
--                       'IMPACTO_AMBIENTAL_MOOVPASS','CURTIDA','VIAGEM')
--  ORDER BY TABLE_NAME, COLUMN_ID;
-- SELECT TABLE_NAME, CONSTRAINT_NAME, CONSTRAINT_TYPE FROM USER_CONSTRAINTS
--  WHERE CONSTRAINT_NAME NOT LIKE 'SYS_%' ORDER BY TABLE_NAME;
