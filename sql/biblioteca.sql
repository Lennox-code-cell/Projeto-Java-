CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

CREATE TABLE IF NOT EXISTS autor (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    nacionalidade VARCHAR(60)
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS livro (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(180) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    ano_publicacao SMALLINT,
    editora VARCHAR(120),
    id_autor INT NOT NULL,
    id_categoria INT NOT NULL,
    CONSTRAINT fk_livro_autor FOREIGN KEY (id_autor) REFERENCES autor(id_autor),
    CONSTRAINT fk_livro_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE IF NOT EXISTS exemplar (
    id_exemplar INT AUTO_INCREMENT PRIMARY KEY,
    id_livro INT NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    situacao VARCHAR(15) NOT NULL,
    CONSTRAINT fk_exemplar_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro)
);


CREATE DATABASE IF NOT EXISTS biblioteca
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE biblioteca;

CREATE TABLE IF NOT EXISTS autor (
                                     id_autor INT AUTO_INCREMENT PRIMARY KEY,
                                     nome VARCHAR(120) NOT NULL,
    nacionalidade VARCHAR(80)
    );

CREATE TABLE IF NOT EXISTS categoria (
                                         id_categoria INT AUTO_INCREMENT PRIMARY KEY,
                                         nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS livro (
                                     id_livro INT AUTO_INCREMENT PRIMARY KEY,
                                     titulo VARCHAR(180) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    ano_publicacao SMALLINT,
    editora VARCHAR(120),
    id_autor INT NOT NULL,
    id_categoria INT NOT NULL,
    CONSTRAINT fk_livro_autor
    FOREIGN KEY (id_autor) REFERENCES autor(id_autor),
    CONSTRAINT fk_livro_categoria
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
    );

CREATE TABLE IF NOT EXISTS exemplar (
                                        id_exemplar INT AUTO_INCREMENT PRIMARY KEY,
                                        id_livro INT NOT NULL,
                                        codigo VARCHAR(30) NOT NULL UNIQUE,
    situacao VARCHAR(20) NOT NULL DEFAULT 'disponivel',
    CONSTRAINT fk_exemplar_livro
    FOREIGN KEY (id_livro) REFERENCES livro(id_livro)
    );

CREATE TABLE IF NOT EXISTS leitor (
                                      id_leitor INT AUTO_INCREMENT PRIMARY KEY,
                                      nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(120),
    endereco VARCHAR(200),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS emprestimo (
                                          id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
                                          id_exemplar INT NOT NULL,
                                          id_leitor INT NOT NULL,
                                          data_emprestimo DATE NOT NULL,
                                          data_prevista DATE NOT NULL,
                                          data_devolucao DATE NULL,
                                          situacao VARCHAR(15) NOT NULL,
    CONSTRAINT fk_emprestimo_exemplar
    FOREIGN KEY (id_exemplar) REFERENCES exemplar(id_exemplar),
    CONSTRAINT fk_emprestimo_leitor
    FOREIGN KEY (id_leitor) REFERENCES leitor(id_leitor)
    );

INSERT INTO autor (nome, nacionalidade)
VALUES ('José da Silva', 'Brasileira')
    ON DUPLICATE KEY UPDATE nome = nome;

INSERT INTO categoria (nome, descricao)
VALUES ('Programação', 'Livros de desenvolvimento e tecnologia')
    ON DUPLICATE KEY UPDATE nome = nome;

INSERT INTO livro (titulo, isbn, ano_publicacao, editora, id_autor, id_categoria)
VALUES ('Java para Iniciantes', '9780000000002', 2024, 'Editora Base', 1, 1)
    ON DUPLICATE KEY UPDATE titulo = titulo;

INSERT INTO exemplar (id_livro, codigo, situacao)
VALUES (1, 'EX-001', 'disponivel')
    ON DUPLICATE KEY UPDATE codigo = codigo;

INSERT INTO leitor (nome, cpf, telefone, email, endereco, ativo)
VALUES ('Maria Oliveira', '52998224725', '11999999999', 'maria@email.com', 'Rua A, 100', TRUE)
    ON DUPLICATE KEY UPDATE cpf = cpf;

SELECT 'Banco Biblioteca criado com sucesso.' AS status;
