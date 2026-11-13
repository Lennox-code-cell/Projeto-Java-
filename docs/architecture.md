# Arquitetura da aplicação

[Voltar ao README](../README.md)

## Visão geral

O projeto é uma aplicação desktop Java organizada em camadas, com separação de responsabilidades inspirada no padrão MVC e no padrão DAO.

## Camadas

| Camada/pacote | Responsabilidade |
|---|---|
| `view` | Apresentar telas, tabelas, formulários e interações Swing. |
| `controller` | Validar entradas e coordenar os casos de uso. |
| `model` | Representar entidades e objetos do domínio. |
| `dao` | Executar operações SQL e mapear resultados para objetos Java. |
| `connection` | Centralizar a criação de conexões JDBC. |
| `util` | Reunir validações e funções auxiliares reutilizáveis. |

## Fluxo típico

1. O usuário interage com uma tela da camada `view`.
2. A tela solicita uma operação ao controller correspondente.
3. O controller valida os dados e aplica as regras sob sua responsabilidade.
4. O DAO executa a consulta ou alteração usando JDBC.
5. O resultado retorna ao controller e é apresentado pela view.

```text
Usuário
  |
  v
View (Swing)
  |
  v
Controller
  |
  v
DAO
  |
  v
DatabaseConnection
  |
  v
MySQL
```

## Ponto de entrada

A classe `br.com.biblioteca.Main` inicia a interface desktop. A conexão com o banco é centralizada em `br.com.biblioteca.connection.DatabaseConnection`.

## Configuração

A conexão usa `DB_USER` e `DB_PASSWORD`; `DB_URL` é opcional e possui uma URL MySQL padrão. Credenciais não devem ser armazenadas no código-fonte.

## Limites desta documentação

Este documento descreve a organização observada no código. Não afirma que todas as regras de negócio estejam cobertas por testes nem que a aplicação implemente uma arquitetura MVC estrita.
