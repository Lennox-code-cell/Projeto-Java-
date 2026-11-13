# Banco de dados

[Voltar ao README](../README.md)

## SGBD e conexão

O projeto utiliza MySQL e JDBC. A URL padrão configurada no código é:

```text
jdbc:mysql://127.0.0.1:3306/biblioteca
```

A aplicação lê `DB_USER` e `DB_PASSWORD` do ambiente. `DB_URL` pode substituir a URL padrão.

## Estrutura identificada

O script `sql/biblioteca.sql` define as seguintes tabelas:

| Tabela | Finalidade | Relacionamentos principais |
|---|---|---|
| `autor` | Autores cadastrados | Um autor pode estar associado a vários livros. |
| `categoria` | Categorias de livros | Uma categoria pode classificar vários livros. |
| `livro` | Dados bibliográficos | Cada livro referencia um autor e uma categoria. |
| `exemplar` | Cópias físicas | Cada exemplar referencia um livro. |
| `leitor` | Pessoas cadastradas | Um leitor pode possuir vários empréstimos. |
| `emprestimo` | Operações de empréstimo | Cada empréstimo referencia um exemplar e um leitor. |

## Relacionamentos

- `autor 1:N livro`
- `categoria 1:N livro`
- `livro 1:N exemplar`
- `leitor 1:N emprestimo`
- `exemplar 1:N emprestimo` ao longo do histórico

## Scripts SQL

### `sql/biblioteca.sql`

Script de criação inicial. Cria o banco `biblioteca`, as tabelas, chaves estrangeiras, restrições e índices declarados no arquivo.

**O script não possui INSERTs nem dados de demonstração.** Uma base recém-criada estará sem registros até que sejam cadastrados pela aplicação ou por um script de carga separado.

Não execute o script indiscriminadamente sobre uma base de produção ou uma base local com dados importantes. Faça backup e revise os comandos antes.

### `sql/migration_001_indexes.sql`

Cria um índice adicional em `emprestimo(data_emprestimo)`. É uma migração opcional e deve ser aplicada uma única vez, após verificar se o índice já existe e realizar backup.

## Dados de demonstração

Quando houver um conjunto de dados fictícios versionado, ele deverá ficar em arquivo separado, por exemplo `sql/dados_demonstracao.sql`. Não inclua dados pessoais reais, senhas ou informações de usuários.

