# Golden Raspberry Awards API

API RESTful para consulta dos indicados e vencedores da categoria **Pior Filme** do Golden Raspberry Awards.

## Tecnologias

- Java 25
- Spring Boot 4.1
- Spring Data JPA
- Apache Commons CSV
- H2 (em memória)
- Gradle
- Docker (opcional)

## Arquitetura

O projeto segue **Clean Architecture**:

| Camada | Responsabilidade |
|--------|------------------|
| `domain` | Modelos, value objects, regras de negócio (`ProducerIntervalCalculator`) e portas de saída |
| `application` | Casos de uso (`LoadMoviesUseCase`, `GetProducerIntervalsUseCase`) |
| `infrastructure` | Persistência JPA, importação CSV, configurações |
| `presentation` | Controllers REST e tratamento de erros |

### Fluxo do endpoint de intervalos

```
GET /v1/producers/interval
  → GetProducerIntervalsUseCase
  → MovieRepositoryPort.findWinners()
  → ProducerIntervalCalculator (agrupa vitórias em memória e calcula min/max)
  → ProducerIntervalResult (JSON)
```

### Modelo de dados

| Camada | Estrutura |
|--------|-----------|
| Domain | `Movie`, `Studio`, `Producer` (value objects) |
| Persistência | Tabela `movies` com colunas JSON `studio_names` e `producer_names` |

Studios e producers ficam como listas JSON no próprio filme — **uma tabela só**.

### Decisões principais

- Domain mantém `Movie`, `Studio`, `Producer`; JPA persiste nomes em JSON via `@JdbcTypeCode(SqlTypes.JSON)`
- `MovieRepositoryPort` centraliza persistência; `findWinners()` traz só filmes vencedores do banco
- Agregação e cálculo de intervalos ficam no `ProducerIntervalCalculator` em memória — volume fixo do CSV (~206 filmes) não justifica views, SQL nativo ou agregação no banco
- Carga inicial via `MovieDataLoader` → `LoadMoviesUseCase`: lê `Movielist.csv` e persiste via `MovieRepositoryPort`
- API retorna o modelo de domínio `ProducerIntervalResult` diretamente, sem DTOs de resposta
- Modelos imutáveis com factory methods (`Movie.create`, `Studio.create`, `Producer.create`)
- Parsing do CSV em `Studio.fromCsvCell` e `Producer.fromCsvCell`

## Pré-requisitos

- JDK 25+
- Nenhuma instalação externa de banco de dados é necessária

## Como executar

```bash
./gradlew bootRun
```

No Windows:

```bash
gradlew.bat bootRun
```

A aplicação sobe em `http://localhost:8080/api`.

Na inicialização, `src/main/resources/static/Movielist.csv` é lido e os dados são persistidos no H2 em memória.

### Docker

```bash
docker build -t challenge-api .
docker run -p 8080:8080 challenge-api
```

## Documentação interativa (Swagger UI)

Disponível após iniciar a aplicação:

| Recurso | URL |
|---------|-----|
| Swagger UI | `http://localhost:8080/api/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/api/v3/api-docs` |

## Endpoints

### Intervalo entre prêmios consecutivos

```
GET /api/v1/producers/interval
```

Retorna os produtores com **menor** e **maior** intervalo entre duas vitórias consecutivas (apenas producers com 2+ anos distintos de vitória entram no cálculo).

**Exemplo de resposta (dados da proposta):**

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

## Testes

```bash
./gradlew test
```

No Windows:

```bash
gradlew.bat test
```

Apenas **testes de integração**, garantindo conformidade com os dados da proposta:

| Teste | O que valida |
|-------|--------------|
| `MovieCsvImportIntegrationTest` | 206 filmes importados, 42 vencedores, parsing correto de múltiplos produtores |
| `ProducerIntervalIntegrationTest` | Endpoint `/api/v1/producers/interval` com Joel Silver (intervalo 1) e Matthew Vaughn (intervalo 13) |
| `ProducerIntervalControlledDataIntegrationTest` | Empate em min e max, produtor com única vitória excluído, filme não-vencedor ignorado |
