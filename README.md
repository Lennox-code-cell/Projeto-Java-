# Biblioteca

Sistema desktop de gerenciamento bibliotecário em Java, com arquitetura MVC, Swing, JDBC e MySQL.

## Estrutura do projeto

- `src/main/java/br/com/biblioteca/model` — entidades do domínio
- `src/main/java/br/com/biblioteca/view` — telas Swing
- `src/main/java/br/com/biblioteca/controller` — regras de negócio e orquestração
- `src/main/java/br/com/biblioteca/dao` — acesso ao banco usando JDBC
- `src/main/java/br/com/biblioteca/connection` — conexão com o MySQL
- `sql/biblioteca.sql` — script de criação das tabelas para um banco novo

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
- MySQL Server 8.0.16+ instalado e em execução
- MySQL Connector/J 9.3.0, fornecido pelo Maven
- Look and Feel FlatLaf, fornecido pelo Maven
- JUnit 5 para os testes automatizados
- As variáveis de ambiente `DB_USER` e `DB_PASSWORD`; `DB_URL` é opcional

## Como executar

1. Para uma instalação nova, crie o banco com `sql/biblioteca.sql`. O script não apaga dados, mas `CREATE TABLE IF NOT EXISTS` não atualiza tabelas já existentes; não o use como migração.
2. Esta versão usa **MySQL Server + MySQL Connector/J** e aceita somente URLs `jdbc:mysql://`. MariaDB é um produto distinto: apesar da compatibilidade de protocolo, o driver e a URL MariaDB não são usados por esta configuração. O `README` original especificava MySQL; por isso, o projeto foi padronizado em MySQL. Se seus dados estiverem em MariaDB, mantenha esse servidor/dados intactos e planeje uma migração com backup antes de mudar de produto.
3. Configure `DB_USER` e `DB_PASSWORD`; `DB_URL` é opcional e, quando ausente, usa `jdbc:mysql://127.0.0.1:3306/biblioteca`. Exemplo de configuração temporária no PowerShell (a senha é solicitada sem aparecer no prompt):

```powershell
$env:DB_URL = "jdbc:mysql://127.0.0.1:3306/biblioteca"
$env:DB_USER = "biblioteca_app"
$env:DB_PASSWORD = (New-Object System.Net.NetworkCredential("", (Read-Host "Senha do MySQL" -AsSecureString))).Password
```

As variáveis duram somente nessa janela do PowerShell. No IntelliJ, configure-as na configuração local de execução e não as versione. `DB_URL`, quando definida, deve começar com `jdbc:mysql://`. Se `DB_USER` ou `DB_PASSWORD` estiver ausente, ou o servidor não estiver acessível, a conexão falha com uma mensagem de configuração sem imprimir a senha.

**Ação de segurança obrigatória:** as credenciais anteriores foram publicadas no repositório. Troque imediatamente a senha do usuário MySQL exposto e revogue a credencial antiga; retirar a senha do código não invalida a cópia já exposta. Use um usuário da aplicação com somente os privilégios necessários (`SELECT`, `INSERT`, `UPDATE` e `DELETE` no banco `biblioteca`). Não reutilize a senha antiga.

4. Execute os testes unitários sem conexão real ao banco:

```powershell
mvn clean test
```

5. Compile e empacote:

```powershell
mvn clean package
```

6. Execute a aplicação:

```powershell
mvn exec:java -Dexec.mainClass=br.com.biblioteca.Main
```

O script `sql/migration_001_indexes.sql` adiciona um índice para acelerar
os relatórios filtrados pela data do empréstimo. A migração é opcional,
não é executada pela aplicação e deve ser aplicada uma única vez após backup.

O script de criação define valores padrão e restrições para as situações de
empréstimos e exemplares; não altera tabelas ou dados já existentes. Nenhum
script de migração do banco é executado automaticamente pela aplicação.

## Observações

Este projeto já está organizado em MVC, com entidades, DAOs, controllers e telas Swing. O objetivo é atender ao fluxo de biblioteca com controle de exemplares, empréstimos e relatórios.
