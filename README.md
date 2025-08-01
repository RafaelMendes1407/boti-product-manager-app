# BotiProductManager

## 📌 Descrição
Este é o projeto de uma API Rest para gerenciamento de produtos. Ela permite o cadastro, consulta e leitura em lote de arquivos JSON.

## 🧱 Tecnologias Utilizadas
- Java 11
- Spring Boot 2.7
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Springdoc OpenAPI (Swagger)

## 🛠️ Instruções de Build e Execução
### 💻 Executando localmente
```
./gradlew build
```
Execute o jar gerado
```
java -jar build/libs/product-manager-app-0.0.1-snapshot.jar
```
### 📦 Docker - RECOMENDADO
Se quiser uma aplicação funcional mais rapidinho pode executar o
```
docker compose up
```
Com isso o ambiente ja estará preparado para testes locais.

## 🧪 Testes
A aplicação possui testes de unidade e testes de integração.
Os testes podem ser executados com o comando:
```
./gradlew jacocoTestReport
```
Esse comando vai gerar um report HTML que você pode abrir no seu navegador na pasta:
```
/build/reports/jacocoHtml/index.html
```

## 🗂️ Endpoints da API
Os endpoints da API podem ser verificados através do link depois que a aplicação ja estiver rodando.

[http://localhost:8080/productmanager/swagger-ui/index.html](http://localhost:8080/productmanager/swagger-ui/index.html)
### 🚀 Funcionalidades
- ✅ Cadastro de produtos (`POST /v1/products`)
- 🔍 Consulta de produtos com filtro por nome ou preço (`GET /v1/products`)
- 🔎 Busca de produto por ID (`GET /v1/products/{id}`)
- 📥 Ingestão de arquivos JSON em lote na inicialização

## 📁 Estrutura do Projeto
```
src/main/java/com/boti/productmanagerapp
├── adapters
│   ├── in
│   │   ├── controller
│   │   └── web
│   │       ├── dto
│   │       └── response
│   └── out
│       ├── batchprocess
│       ├── entities
│       ├── exceptions
│       ├── jpa
│       └── repository
├── application
│   ├── core
│   │   ├── domain
│   │   ├── exceptions
│   │   └── usecases
│   └── ports
│       ├── in
│       └── out
├── infrastructure
│   └── config
└── utils
    └── mappers
```
## ♻️ Padrões e Convenções

O projeto adota um design de código do Padrão de Ports and Adapters (Arquitetura Hexagonal).
Nesse design de código deixamos as regras de negócio bem separadas das camadas mais externas.

- Arquitetura Hexagonal
- Uso de DTOs, Mappers e Use Cases
- Tratamento de exceções
- Logs

## 🧩 Variáveis de Ambiente
|Variável|Descriçao|
|-------------------|-----------------------------------------------------------------------|
|INGESTION_POOL_SIZE| Define a quantidade de threads deve ser aberta para ingestão dos arquivos|
|INGESTION_FOLDER_LOCATION|Define a pasta de origem dos arquivos para ingestão de dados|
|DATASOURCE_URL| Url para conexão com o banco de dados|
|DATASOURCE_USERNAME| Usuário para conexão com o banco de dados|
|DATASOURCE_PASSWORD| Senha para conexão com o banco de dados|

## 🐳 Ambiente com Docker
No arquivo `docker-compose.yml` você ja terá um ambiente funcional em pouco tempo, caso necessário altere o mapeamento de volumes para ingestão de dados.