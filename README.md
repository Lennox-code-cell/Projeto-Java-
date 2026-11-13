# Biblioteca

Sistema desktop de gerenciamento de biblioteca em Java, desenvolvido com arquitetura MVC, Swing, JDBC e MySQL. O projeto centraliza o cadastro de autores, categorias, livros, exemplares, leitores, empréstimos e relatórios em uma interface desktop simples e funcional.

## Visão geral

A aplicação foi estruturada para facilitar a manutenção e a evolução do código:

- `model`: entidades do domínio
- `view`: telas Swing da interface gráfica
- `controller`: validações e orquestração das regras de negócio
- `dao`: acesso aos dados com JDBC
- `connection`: configuração e abertura de conexão com o MySQL

Além disso, o projeto conta com testes automatizados para regras de validação e fluxos principais de domínio.

## Funcionalidades principais

- Cadastro de autores
- Cadastro de categorias
- Cadastro de livros
- Cadastro de exemplares
- Cadastro e ativação/inativação de leitores
- Registro de empréstimos e devoluções
- Atualização automática da situação de empréstimos
- Relatórios por período e situação
- Validações de CPF, ISBN, e-mail, telefone e regras de negócio

## Estrutura do projeto

```text
POO/
├── src/
│   ├── main/java/br/com/biblioteca/
│   │   ├── Main.java
│   │   ├── connection/
│   │   ├── controller/
│   │   ├── dao/
│   │   ├── model/
│   │   ├── util/
│   │   └── view/
│   └── test/java/br/com/biblioteca/
├── sql/
│   ├── biblioteca.sql
│   └── migration_001_indexes.sql
├── pom.xml
├── README.md
├── run.bat
├── build.cmd
└── out/
```

## Requisitos

- Java JDK 25+
- Maven 3.9+
- MySQL Server 8.0.16+ em execução
- MySQL Connector/J 9.3.0 (gerenciado pelo Maven)
- FlatLaf 3.6.1 (gerenciado pelo Maven)
- JUnit 5 (gerenciado pelo Maven)

## Configuração do banco

Este projeto foi padronizado para MySQL e aceita apenas URLs no formato `jdbc:mysql://`.

Antes de executar a aplicação, crie o banco a partir do script:

```bash
mysql -u <usuario> -p < sql/biblioteca.sql
```

O script cria a base `biblioteca` e as tabelas principais. Ele não deve ser usado como migração em um banco já existente sem cuidado.

### Variáveis de ambiente

A aplicação lê as credenciais do banco via variáveis de ambiente:

- `DB_URL` (opcional; padrão: `jdbc:mysql://127.0.0.1:3306/biblioteca`)
- `DB_USER` (obrigatória)
- `DB_PASSWORD` (obrigatória)

Exemplo no PowerShell:

```powershell
$env:DB_URL = "jdbc:mysql://127.0.0.1:3306/biblioteca"
$env:DB_USER = "biblioteca_app"
$env:DB_PASSWORD = (New-Object System.Net.NetworkCredential("", (Read-Host "Senha do MySQL" -AsSecureString))).Password
```

Importante:

- nunca deixe credenciais embutidas em código ou na URL
- `DB_URL` não deve conter usuário/senha no próprio texto
- use um usuário com permissões mínimas necessárias para o banco da aplicação
- a senha do MySQL exposta em repositórios deve ser trocada imediatamente

## Como executar

### 1) Testar o projeto

```bash
mvn clean test
```

### 2) Compilar o pacote

```bash
mvn clean package
```

### 3) Executar a aplicação

```bash
mvn exec:java -Dexec.mainClass=br.com.biblioteca.Main
```

No Windows, também é possível usar os scripts auxiliares:

```cmd
run.bat
```

```cmd
build.cmd
```

## Fluxo principal do sistema

1. Cadastrar autor e categoria
2. Cadastrar livro
3. Cadastrar exemplar
4. Cadastrar leitor
5. Registrar empréstimo
6. Registrar devolução
7. Consultar relatórios por período e situação

## Migrações e manutenção do banco

O arquivo `sql/migration_001_indexes.sql` adiciona índices para aprimorar relatórios por data de empréstimo. Ele é opcional e deve ser aplicado manualmente, após backup do banco, quando necessário.

Nenhum script de migração é executado automaticamente pela aplicação.

## Observações

- O projeto segue o padrão MVC com separação clara de responsabilidades
- A interface gráfica é construída com Swing + FlatLaf
- Os acessos ao banco são feitos por JDBC e DAO
- O código foi organizado para facilitar manutenção, testes e expansão futura
