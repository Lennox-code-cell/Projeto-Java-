# Biblioteca Fácil

Sistema desktop de gerenciamento bibliotecário em Java, com arquitetura MVC, Swing, JDBC e MySQL.

## Estrutura do projeto

- `src/main/java/br/com/biblioteca/model` — entidades do domínio
- `src/main/java/br/com/biblioteca/view` — telas Swing
- `src/main/java/br/com/biblioteca/controller` — regras de negócio e orquestração
- `src/main/java/br/com/biblioteca/dao` — acesso ao banco usando JDBC
- `src/main/java/br/com/biblioteca/connection` — conexão com o MySQL
- `sql/biblioteca.sql` — script de criação do banco e dados iniciais

## Fluxo principal do sistema

1. Cadastrar autor e categoria
2. Cadastrar livro
3. Cadastrar exemplar
4. Cadastrar leitor
5. Registrar empréstimo
6. Registrar devolução
7. Consultar relatório por período

## Requisitos de execução

- Java JDK 25+
- MySQL instalado e em execução
- Driver MySQL Connector/J

## Como executar

1. Abra o MySQL e crie o banco com o arquivo `sql/biblioteca.sql`.
2. Configure as variáveis de ambiente antes de executar a aplicação. O endereço e o usuário têm valores padrão; a senha deve ser informada fora do código:
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
3. Compile o projeto:

```bat
build.cmd
```

4. Execute a aplicação:

```bat
run.bat
```

## Observações

Este projeto já está organizado em MVC, com entidades, DAOs, controllers e telas Swing. O objetivo é atender ao fluxo de biblioteca com controle de exemplares, empréstimos e relatórios.
