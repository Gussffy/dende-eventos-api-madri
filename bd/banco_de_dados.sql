-- =============================================================
--  Dendê Eventos – Script de criação do banco de dados (MySQL)
-- =============================================================

CREATE DATABASE IF NOT EXISTS dende_eventos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dende_eventos;

-- =============================================================
-- TABELA: usuario
-- Armazena usuários comuns e organizadores
-- Discriminação pelo campo tipo_usuario
-- id numérico como PK para performance em joins e FKs;
-- email é UNIQUE e atua como identificador de negócio
-- =============================================================
CREATE TABLE usuario
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(255) NOT NULL,
    data_nascimento DATE         NOT NULL,
    sexo            CHAR(1)      NOT NULL,           -- 'M', 'F', 'O'
    email           VARCHAR(255) NOT NULL UNIQUE,    -- identificador único
    senha           VARCHAR(255) NOT NULL,           -- armazenar hash
    tipo_usuario    VARCHAR(20)  NOT NULL,           -- 'COMUM' | 'ORGANIZADOR'
    ativo           TINYINT(1)   NOT NULL DEFAULT 1, -- 1 = ativo, 0 = inativo
    PRIMARY KEY (id)
);

-- =============================================================
-- TABELA: empresa  (entidade fraca – depende do organizador)
-- Um organizador pode ter ou não uma empresa vinculada.
-- A empresa não existe sem o seu organizador.
-- =============================================================
CREATE TABLE empresa
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    organizador_id BIGINT       NOT NULL UNIQUE, -- 1 organizador → 0..1 empresa
    cnpj           VARCHAR(18)  NOT NULL UNIQUE, -- ex: 12.345.678/0001-99
    razao_social   VARCHAR(255) NOT NULL,
    nome_fantasia  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_empresa_organizador
        FOREIGN KEY (organizador_id) REFERENCES usuario (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

-- =============================================================
-- TABELA: evento
-- Relacionamento com organizador e evento principal (sub-evento)
-- =============================================================
CREATE TABLE evento
(
    id                  BIGINT         NOT NULL AUTO_INCREMENT,
    organizador_id      BIGINT         NOT NULL,
    evento_principal_id BIGINT         NULL,                  -- sub-evento vinculado
    nome                VARCHAR(255)   NOT NULL,
    descricao           TEXT           NULL,
    pagina_web          VARCHAR(500)   NULL,
    tipo_evento         VARCHAR(30)    NOT NULL,              -- 'SOCIAL' | 'CORPORATIVO' | 'ACADEMICO' |
    -- 'CULTURAL_ENTRETENIMENTO' | 'RELIGIOSO' |
    -- 'ESPORTIVO' | 'FEIRA' | 'CONGRESSO' |
    -- 'OFICINA' | 'CURSO' | 'TREINAMENTO' |
    -- 'AULA' | 'SEMINARIO' | 'PALESTRA' |
    -- 'SHOW' | 'FESTIVAL' | 'EXPOSICAO' |
    -- 'RETIRO' | 'CULTO' | 'CELEBRACAO' |
    -- 'CAMPEONATO' | 'CORRIDA'
    modalidade          VARCHAR(10)    NOT NULL,              -- 'PRESENCIAL' | 'REMOTO' | 'HIBRIDO'
    local_evento        VARCHAR(500)   NOT NULL,              -- endereço ou link
    data_inicio         DATETIME       NOT NULL,              -- >= NOW() validado pela aplicação
    data_fim            DATETIME       NOT NULL,              -- >= data_inicio e >= NOW() validado pela aplicação e pelo CHECK constraint
    capacidade_maxima   INT            NOT NULL,
    preco_ingresso      DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    estorna_ingresso    TINYINT(1)     NOT NULL DEFAULT 0,
    taxa_estorno        DECIMAL(5, 2)  NOT NULL DEFAULT 0.00, -- % de estorno
    ativo               TINYINT(1)     NOT NULL DEFAULT 0,    -- 1 = ativo, 0 = inativo
    data_cadastro       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_evento_organizador
        FOREIGN KEY (organizador_id) REFERENCES usuario (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT fk_evento_principal
        FOREIGN KEY (evento_principal_id) REFERENCES evento (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE,
    CONSTRAINT chk_evento_datas
        CHECK (data_fim > data_inicio),
    CONSTRAINT chk_evento_duracao_minima
        CHECK (TIMESTAMPDIFF(MINUTE, data_inicio, data_fim) >= 30),
    CONSTRAINT chk_taxa_estorno
        CHECK (taxa_estorno >= 0 AND taxa_estorno <= 100)
);

-- =============================================================
-- TABELA: ingresso
-- Representa cada ingresso comprado por um usuário
-- =============================================================
CREATE TABLE ingresso
(
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    usuario_id        BIGINT         NOT NULL,                 -- usuário comum comprador
    evento_id         BIGINT         NOT NULL,                 -- evento ao qual pertence
    valor_pago        DECIMAL(10, 2) NOT NULL,                 -- valor no momento da compra
    status            VARCHAR(10)    NOT NULL DEFAULT 'ATIVO', -- 'ATIVO' | 'CANCELADO'
    valor_estornado   DECIMAL(10, 2) NOT NULL DEFAULT 0.00,    -- valor devolvido ao cancelar
    data_compra       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_cancelamento DATETIME       NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ingresso_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT fk_ingresso_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

-- =============================================================
-- ÍNDICES para performance nas consultas das User Stories
-- =============================================================

-- US1 / US3: busca por e-mail (unicidade já garante índice automático)
-- US5 / US6: filtrar usuários ativos/inativos
CREATE INDEX idx_usuario_ativo ON usuario (ativo);
CREATE INDEX idx_usuario_tipo ON usuario (tipo_usuario);

-- US10: listar eventos de um organizador
CREATE INDEX idx_evento_organizador ON evento (organizador_id);

-- US11: feed – eventos ativos, não finalizados, com vagas
CREATE INDEX idx_evento_ativo ON evento (ativo);
CREATE INDEX idx_evento_data_inicio ON evento (data_inicio);
CREATE INDEX idx_evento_nome ON evento (nome);

-- US12 / US13: ingressos por usuário e por evento
CREATE INDEX idx_ingresso_usuario ON ingresso (usuario_id);
CREATE INDEX idx_ingresso_evento ON ingresso (evento_id);
CREATE INDEX idx_ingresso_status ON ingresso (status);

