# Biblioteca

[English version](README.en.md)

Sistema desktop de gerenciamento de biblioteca desenvolvido em **Java 25**, com **Swing**, **JDBC** e **MySQL**. O projeto aplica separação de responsabilidades inspirada no padrão MVC e utiliza Maven para dependências, compilação e testes.

> Projeto acadêmico em evolução. Consulte a documentação antes de configurar o banco ou executar a aplicação.

## Funcionalidades

- Cadastro e gerenciamento de autores e categorias.
- Cadastro e gerenciamento de livros e exemplares físicos.
- Cadastro e gerenciamento de leitores.
- Registro de empréstimos e devoluções.
- Consulta de empréstimos ativos e atrasados.
- Relatórios filtrados por período e situação.
- Validações de dados, incluindo CPF e ISBN.

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 25 | Linguagem e plataforma |
| Swing + FlatLaf | Interface desktop |
| JDBC | Acesso relacional |
| MySQL 8.0.16+ | Banco de dados |
| Maven | Dependências e build |
| JUnit 5 | Testes automatizados |

## Arquitetura

O código-fonte fica em `src/main/java/br/com/biblioteca`:

- `model`: entidades e objetos do domínio.
- `view`: telas e componentes Swing.
- `controller`: validações e coordenação dos casos de uso.
- `dao`: persistência e consultas JDBC.
- `connection`: configuração e abertura da conexão.
- `util`: funções auxiliares e validações.

O fluxo geral é: **View → Controller → DAO → MySQL**. Consulte [docs/architecture.md](docs/architecture.md).

## Requisitos

- JDK 25 ou superior.
- Maven 3.9 ou superior.
- MySQL Server 8.0.16 ou superior.
- Git (opcional, para clonar o projeto).

## Configuração

### 1. Obter o projeto

```bash
git clone https://github.com/Lennox-code-cell/biblioteca.git
cd biblioteca
```

### 2. Preparar o banco

O script inicial está em `sql/biblioteca.sql`. Ele cria o banco `biblioteca` e suas tabelas, mas **não inclui registros de demonstração**.

Em uma instalação nova, execute:

```bash
mysql -u SEU_USUARIO -p < sql/biblioteca.sql
```

Se o banco já existe e contém dados, não execute o script sem revisar seu conteúdo e fazer backup. Consulte [docs/database.md](docs/database.md).

### 3. Configurar as credenciais

A aplicação lê as variáveis de ambiente:

| Variável | Obrigatória | Descrição |
|---|---:|---|
| `DB_USER` | Sim | Usuário do MySQL |
| `DB_PASSWORD` | Sim | Senha do usuário |
| `DB_URL` | Não | URL JDBC; padrão `jdbc:mysql://127.0.0.1:3306/biblioteca` |

Exemplo no PowerShell (a senha será solicitada sem ficar escrita no comando):

```powershell
$env:DB_URL = "jdbc:mysql://127.0.0.1:3306/biblioteca"
$env:DB_USER = "biblioteca_app"
$secure = Read-Host "Senha do MySQL" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
```

Não publique senhas, arquivos `.env`, dumps reais ou dados pessoais. Use um usuário de banco com permissões limitadas ao schema da aplicação.

## Executar

Na raiz do projeto:

```bash
mvn clean test
mvn clean package
mvn exec:java -Dexec.mainClass=br.com.biblioteca.Main
```

No Windows, os scripts `build.cmd` e `run.bat` também podem ser utilizados.

## Estrutura

```text
biblioteca/
├── docs/
├── sql/
├── src/
│   ├── main/java/br/com/biblioteca/
│   └── test/java/br/com/biblioteca/
├── .github/workflows/
├── pom.xml
├── README.md
└── README.en.md
```

## Testes e integração contínua

Execute `mvn clean test` localmente. O GitHub Actions também executa a suíte de testes em pushes e pull requests direcionados à branch `main`.

## Documentação

- [Arquitetura](docs/architecture.md)
- [Banco de dados](docs/database.md)
- [English README](README.en.md)

## Segurança

Se alguma credencial real já foi commitada, troque-a imediatamente. Remover a senha do código atual não apaga cópias existentes no histórico Git. A limpeza do histórico exige planejamento, backup e coordenação com todos os clones; não foi realizada nesta organização.

## Status

O projeto está em desenvolvimento. Funcionalidades, documentação e testes podem mudar conforme novas versões forem implementadas.
