# Biblioteca

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
- Maven 3.9+
- MySQL instalado e em execução
- Look and Feel FlatLaf, fornecido pelo Maven
- JUnit 5 para os testes automatizados
- As variáveis de ambiente `DB_URL`, `DB_USER` e `DB_PASSWORD`

## Como executar

1. Configure o banco MySQL com `sql/biblioteca.sql` em uma instalação nova. Em uma instalação existente, revise o script antes de executá-lo; ele não é uma migração.
2. Configure as variáveis de ambiente antes de executar a aplicação. Nenhum valor padrão ou credencial é mantido no código:
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
3. Compile e empacote o projeto:

```powershell
mvn clean package
```

Os testes que não dependem do MySQL podem ser executados com:

```powershell
mvn test
```

4. Execute a aplicação:

```powershell
mvn exec:java -Dexec.mainClass=br.com.biblioteca.Main
```

O script `sql/migration_001_indexes.sql` adiciona um índice para acelerar
os relatórios filtrados pela data do empréstimo. A migração é opcional,
não é executada pela aplicação e deve ser aplicada uma única vez após backup.

## Observações

Este projeto já está organizado em MVC, com entidades, DAOs, controllers e telas Swing. O objetivo é atender ao fluxo de biblioteca com controle de exemplares, empréstimos e relatórios.
