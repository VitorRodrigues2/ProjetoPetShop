-- Criando o banco
CREATE DATABASE petstop;
USE petstop;

-- Tabela de ANIMAIS
CREATE TABLE IF NOT EXISTS animais (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(50),
    idade BIGINT,
    vacinado BOOLEAN,
    UNIQUE (nome, especie) -- Para impedir que tenha cadastro de animais duplicados
);

-- Tabela de PRODUTOS
CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade BIGINT,
    preco DECIMAL(10,2),
    ativo BOOLEAN
);
