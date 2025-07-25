# 📚 Biblioteca API

API REST desenvolvida para gerenciar livros, autores e categorias. Conta com um recurso de importação automática de livros diretamente da Amazon, utilizando **Web Scraping com Jsoup**.

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation (Jakarta)
- H2 Database
- Jsoup *(para scraping de dados da Amazon)*
- Lombok

---

## 📂 Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── com.biblioteca.api/
            ├── controller/
            ├── dto/
            │   ├── request/
            │   └── response/
            ├── model/
            ├── repository/
            ├── service/
            └── handler/
```

---

## 🔧 Funcionalidades

### 📘 Livros
- `GET /api/livros`: Lista todos os livros com filtros opcionais por autor, categoria e ano.
- `GET /api/livros/{id}`: Detalha um livro pelo ID.
- `POST /api/livros`: Cadastra um novo livro.
- `PUT /api/livros/{id}`: Atualiza os dados de um livro.
- `DELETE /api/livros/{id}`: Remove um livro.
- `GET /api/livros/search?titulo=`: Busca livros pelo título.
- `POST /api/livros/importar`: Importa um livro da Amazon via URL.

### 👤 Autores
- `GET /api/autores`: Lista todos os autores com paginação.
- `GET /api/autores/{id}`: Detalha um autor pelo ID.
- `POST /api/autores`: Cadastra um novo autor.
- `PUT /api/autores/{id}`: Atualiza um autor existente.
- `DELETE /api/autores/{id}`: Remove um autor.
- `GET /api/autores/{id}/livros`: Lista livros de um autor.

### 🏷️ Categorias
- `GET /api/categorias`: Lista todas as categorias.
- `POST /api/categorias`: Cadastra uma nova categoria.
- `GET /api/categorias/{id}/livros`: Lista livros pertencentes à categoria.

---

## 🔍 Scraping com Jsoup

O endpoint `POST /api/livros/importar` realiza scraping na página de um livro da Amazon e extrai:

- Título
- ISBN (10 ou 13 dígitos)
- Ano de publicação (inferido por regex)
- Preço (prioriza formato *Capa comum*)

A requisição simula um navegador real por meio de:
- `User-Agent` personalizado
- `referrer`
- Headers como `Accept-Language` e `Cache-Control`

> ⚠️ O scraping pode falhar caso a Amazon aplique CAPTCHA(Considere alterar o user agent para testar por enquanto)

---

## ⚠️ Tratamento de Erros

A aplicação possui um `GlobalExceptionHandler` que padroniza as respostas de erro para os seguintes casos:

- `EntityNotFoundException`: Recurso não encontrado
- `MethodArgumentNotValidException`: Erros de validação
- `RuntimeException`: Exceções inesperadas da aplicação
- `Exception`: Qualquer outro erro genérico

Formato da resposta de erro:

```json
{
  "timestamp": "2025-07-22T15:03:01.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Campo 'titulo' é obrigatório"
}
```

---

## 🛠️ Como Executar o Projeto

```bash
# Pré-requisitos: JDK 17+ e Maven
git clone https://github.com/seu-usuario/biblioteca-api.git
cd biblioteca-api
./mvnw spring-boot:run
```

Acesse em: [http://localhost:8080/api](http://localhost:8080/api)

## ✅Link para uso da colection do postman

https://.postman.co/workspace/Personal-Workspace~75f12a34-1995-4ff1-9f99-2e1350639dbe/collection/42279277-c1ab0b06-9fc5-4996-8d6f-23433d48077a?action=share&creator=42279277

---

## ✅ Exemplo de Importação de Livro

```http
POST /api/livros/importar
Content-Type: application/json

{
  "url": "https://www.amazon.com.br/dp/0132350882",
  "autorId": 1,
  "categoriaId": 1
}
```

---

## 👨‍💻 Autor

Desenvolvido por **Igor Mapurunga**
