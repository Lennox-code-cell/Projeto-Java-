#!/usr/bin/env python3
"""
Gera importar_2000_livros.sql usando a API pública do Open Library.
Busca 1.500 livros em português e 500 em outros idiomas.
Cada livro recebe 5 exemplares. ISBN-13 é validado; nenhum ISBN é inventado.

Execute dentro da pasta do projeto:
    python gerar_catalogo_biblioteca_openlibrary.py
"""

import json
import time
import urllib.parse
import urllib.request
import urllib.error
from pathlib import Path

META = {
    "pt": 1500,
    "outros": 500,
}
POR_PAGINA = 100
INTERVALO_SEGUNDOS = 1.5
MAX_TENTATIVAS = 5
ARQUIVO_SAIDA = Path("importar_2000_livros.sql")


def isbn13_valido(isbn):
    isbn = "".join(c for c in str(isbn) if c.isdigit())
    if len(isbn) != 13:
        return False
    soma = sum(int(d) * (1 if i % 2 == 0 else 3) for i, d in enumerate(isbn[:12]))
    digito = (10 - soma % 10) % 10
    return digito == int(isbn[12])


def sql(valor):
    if valor is None:
        return "NULL"
    texto = str(valor).strip()
    if not texto:
        return "NULL"
    texto = texto.replace("\\", "\\\\").replace("'", "''")
    return "'" + texto + "'"


def primeiro_isbn13(lista):
    for item in lista or []:
        if isbn13_valido(item):
            return "".join(c for c in str(item) if c.isdigit())
    return None


def requisitar_pagina(params):
    url = "https://openlibrary.org/search.json?" + urllib.parse.urlencode(params)
    req = urllib.request.Request(
        url,
        headers={
            "User-Agent": "BibliotecaAcademica/1.0 (catalog-import; educational project)",
            "Accept": "application/json",
        },
    )
    for tentativa in range(MAX_TENTATIVAS):
        try:
            with urllib.request.urlopen(req, timeout=45) as resposta:
                return json.loads(resposta.read().decode("utf-8"))
        except urllib.error.HTTPError as erro:
            if erro.code in (429, 500, 502, 503, 504):
                espera = min(30, 3 * (tentativa + 1))
                print(f"  API limitou a consulta (HTTP {erro.code}). Nova tentativa em {espera}s...")
                time.sleep(espera)
                continue
            raise
        except (urllib.error.URLError, TimeoutError) as erro:
            espera = min(30, 3 * (tentativa + 1))
            print(f"  Falha de rede: {erro}. Nova tentativa em {espera}s...")
            time.sleep(espera)
    raise RuntimeError("A API continuou indisponível após várias tentativas.")


def coletar(tipo, quantidade, encontrados):
    coletados = []
    idiomas_busca = ("por",) if tipo == "pt" else ("spa", "fre", "ger", "ita")
    idiomas_aceitos = set(idiomas_busca)
    max_paginas = 100

    for idioma_busca in idiomas_busca:
        pagina = 1
        while len(coletados) < quantidade and pagina <= max_paginas:
            params = {
                "q": f"language:{idioma_busca}",
                "limit": POR_PAGINA,
                "page": pagina,
                "fields": "title,author_name,isbn,publisher,first_publish_year,subject,language",
            }
            print(f"  Consultando {tipo} ({idioma_busca}): página {pagina} "
                  f"({len(coletados)}/{quantidade})")
            dados = requisitar_pagina(params)
            docs = dados.get("docs", [])

            if not docs:
                break

            for doc in docs:
                isbn = primeiro_isbn13(doc.get("isbn"))
                titulo = (doc.get("title") or "").strip()
                autores = doc.get("author_name") or []
                if not isbn or not titulo or not autores or isbn in encontrados:
                    continue

                idiomas = doc.get("language") or []
                if not idiomas_aceitos.intersection(idiomas):
                    continue

                assuntos = doc.get("subject") or []
                categoria = assuntos[0] if assuntos else ("Literatura" if tipo == "pt" else "Literatura internacional")
                categoria = categoria[:80]
                ano = doc.get("first_publish_year")
                if not isinstance(ano, int) or ano < 1000 or ano > 9999:
                    ano = None

                coletados.append({
                    "isbn": isbn,
                    "titulo": titulo[:180],
                    "autor": str(autores[0])[:120],
                    "editora": str((doc.get("publisher") or ["Não informada"])[0])[:120],
                    "ano": ano,
                    "categoria": categoria,
                })
                encontrados.add(isbn)
                if len(coletados) >= quantidade:
                    break

            pagina += 1
            time.sleep(INTERVALO_SEGUNDOS)

    return coletados


def montar_sql(livros):
    linhas = [
        "-- Catálogo gerado a partir de registros da Open Library.",
        "-- Confira os dados antes de importar. Cada título recebe cinco exemplares.",
        "USE biblioteca;",
        "START TRANSACTION;",
        "",
    ]

    for livro in livros:
        linhas.append(
            "INSERT INTO autor (nome, nacionalidade) "
            f"SELECT {sql(livro['autor'])}, 'Não informada' "
            "WHERE NOT EXISTS (SELECT 1 FROM autor "
            f"WHERE nome = {sql(livro['autor'])});"
        )
        linhas.append(
            "INSERT INTO categoria (nome, descricao) "
            f"SELECT {sql(livro['categoria'])}, 'Categoria importada do catálogo Open Library' "
            "WHERE NOT EXISTS (SELECT 1 FROM categoria "
            f"WHERE nome = {sql(livro['categoria'])});"
        )
        linhas.append(
            "INSERT INTO livro (titulo, isbn, ano_publicacao, editora, id_autor, id_categoria) "
            "SELECT "
            f"{sql(livro['titulo'])}, {sql(livro['isbn'])}, "
            f"{sql(livro['ano'])}, {sql(livro['editora'])}, "
            "(SELECT id_autor FROM autor WHERE nome = "
            f"{sql(livro['autor'])} LIMIT 1), "
            "(SELECT id_categoria FROM categoria WHERE nome = "
            f"{sql(livro['categoria'])} LIMIT 1) "
            "WHERE NOT EXISTS (SELECT 1 FROM livro WHERE isbn = "
            f"{sql(livro['isbn'])});"
        )
        for numero in range(1, 6):
            codigo = f"OL-{livro['isbn']}-{numero:02d}"
            linhas.append(
                "INSERT INTO exemplar (id_livro, codigo, situacao) "
                "SELECT id_livro, "
                f"{sql(codigo)}, 'Disponivel' FROM livro "
                f"WHERE isbn = {sql(livro['isbn'])} "
                "AND NOT EXISTS (SELECT 1 FROM exemplar WHERE codigo = "
                f"{sql(codigo)});"
            )
        linhas.append("")

    linhas.extend(["COMMIT;", ""])
    return "\n".join(linhas)


def main():
    print("Gerador de catálogo real — Open Library")
    print("A busca pode levar alguns minutos. Não feche esta janela.\n")

    encontrados = set()
    livros_pt = coletar("pt", META["pt"], encontrados)
    print(f"\nPortuguês encontrados: {len(livros_pt)} de {META['pt']}")

    livros_outros = coletar("outros", META["outros"], encontrados)
    print(f"Outros idiomas encontrados: {len(livros_outros)} de {META['outros']}")

    total = len(livros_pt) + len(livros_outros)
    if len(livros_pt) != META["pt"] or len(livros_outros) != META["outros"]:
        print(f"\nA API retornou apenas {total} livros válidos com ISBN-13 único.")
        print("Nenhum SQL foi gerado para evitar importar um catálogo incompleto.")
        print("Tente novamente mais tarde; os limites da API podem variar.")
        return

    ARQUIVO_SAIDA.write_text(
        montar_sql(livros_pt + livros_outros),
        encoding="utf-8"
    )
    print(f"\nConcluído: {total} livros e {total * 5} exemplares.")
    print(f"Arquivo criado: {ARQUIVO_SAIDA.resolve()}")
    print("Ainda NÃO foi feita nenhuma alteração no banco de dados.")


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nBusca interrompida pelo usuário. Nenhum SQL completo foi gerado.")
    except Exception as erro:
        print(f"\nErro: {erro}")
        print("Nenhum SQL completo foi gerado.")
