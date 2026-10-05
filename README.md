# 📬 Newsletter RESTful API

Uma API backend desenvolvida em **Java 21** e **Spring Boot 4**, estruturada sob princípios de arquitetura limpa, tratamento defensivo de dados e conteinerização de serviços. O sistema tem como objetivo gerenciar assinaturas de usuários e automatizar a busca, cacheamento e distribuição periódica de notícias a partir de APIs externas.

---

## 🏛️ Destaques de Arquitetura e Decisões Técnicas

* **Consumo de API Externa com `RestClient` Nativo:** Utilização da interface fluente do `RestClient` (introduzida no Spring Framework 6 / Spring Boot 3) gerenciada via classe de configuração dedicada (`RestClientConfig`), substituindo clientes HTTP legados ou dependências externas como OpenFeign.
* **Imutabilidade e Tipagem Segura com Java Records:** Todos os contratos de transporte (DTOs de entrada, saída e de integração com a NewsAPI) são construídos exclusivamente como Records (`SubscriberRequestDTO`, `SubscriberResponseDTO`, `MessageResponseDTO`, `StandardErrorDTO`), garantindo imutabilidade e ausência de código boilerplate.
* **Segurança de Borda & Prevenção de Enumeração de Usuários (*Anti-User Enumeration*):** Validação estrita de contratos com Bean Validation (`@Valid`, `@NotBlank`, `@Email`) na camada de controle. No fluxo de cadastro, a aplicação responde uniformemente com status `HTTP 202 Accepted`, notificando o assinante por e-mail e evitando expor a clientes anônimos se um e-mail já existe na base de dados.
* **Tratamento Centralizado de Exceções:** Implementação de um interceptador global com `@RestControllerAdvice` (`GlobalExceptionHandler`) que captura erros de validação e regras de negócio, padronizando os payloads de resposta sob o contrato estruturado `StandardErrorDTO`.
* **Construção Otimizada de Contêineres (Multi-Stage Build):** O `Dockerfile` é dividido em estágio de compilação com Maven e JDK 21 Alpine (`build`) e estágio de execução isolado com JRE 21 Alpine. A ordem de cópia do `pom.xml` antes do código-fonte preserva o cache de dependências, e a aplicação é executada sob o usuário não-root `spring:spring` para atender a boas práticas de segurança em produção.
* **Orquestração de Infraestrutura Declarativa:** Definição dos serviços da aplicação, banco de dados em memória Redis e mensageria RabbitMQ no `docker-compose.yaml`, com resolução interna de nomes via rede privada e persistência de dados em disco através do volume nomeado `redis_data`.

---

## 🛠️ Stack Tecnológica

| Camada / Componente | Tecnologia              | Finalidade |
| :--- |:------------------------| :--- |
| **Linguagem** | Java 21 (LTS)           | Uso de Records, Pattern Matching e tipagem estrita |
| **Framework Base** | Spring Boot 4.x         | Ecossistema da aplicação (Web MVC, Data JPA, Bean Validation) |
| **Banco Relacional** | PostgreSQL (Neon.tech)  | Persistência transacional dos dados de assinantes |
| **Cliente HTTP** | Spring RestClient       | Consumo síncrono e integrado dos endpoints da NewsAPI |
| **Cache Distribuído** | Redis 7.x               | Armazenamento temporário em memória para controle de chamadas à NewsAPI |
| **Mensageria Assíncrona** | RabbitMQ 3.x            | Processamento desacoplado de filas de eventos para envio de e-mails |
| **Conteinerização** | Docker & Docker Compose | Padronização e isolamento do ecossistema de desenvolvimento |

---

## 📂 Estrutura de Pacotes

A aplicação está organizada no pacote base `com.marcio.newsletter_api` seguindo uma arquitetura em camadas desacopladas:

```text
com.marcio.newsletter_api
├── config/              # Configurações de Beans e clientes (@Configuration, RestClientConfig)
├── controllers/         # Endpoints REST (SubscriberController)
│   └── exceptions/      # Handlers globais (@RestControllerAdvice, GlobalExceptionHandler)
├── domain/              # Entidades JPA de banco de dados (Subscriber)
├── dtos/                # Records de entrada/saída (Requests, Responses, StandardErrorDTO)
│   └── newsapi/         # Mapeamento dos payloads retornados pela NewsAPI
├── integrations/        # Serviços de integração externa (NewsApiClient)
├── repositories/        # Interfaces Spring Data JPA (SubscriberRepository)
└── services/            # Camada de regras de negócio (SubscriberService)
```

---

## ⚙️ Estado de Implementação

### Concluído
- [x] Configuração base do ecossistema com Java 21 e Spring Boot 4.
- [x] Mapeamento relacional da entidade `Subscriber` com Spring Data JPA.
- [x] Implementação de contratos de entrada e saída com Java Records (`dtos/`).
- [x] Validação declarativa na borda via Bean Validation (`@Valid`, `@NotBlank`, `@Email`).
- [x] Fluxo de cadastro seguro com resposta uniforme `HTTP 202 Accepted` (*Anti-User Enumeration*).
- [x] Interceptador global de exceções via `@RestControllerAdvice` (`GlobalExceptionHandler`).
- [x] Contrato padronizado para tratamento de erros com `StandardErrorDTO`.
- [x] Configuração e consumo da NewsAPI utilizando Spring `RestClient` nativo (`RestClientConfig`, `NewsApiClient`).
- [x] Elaboração de `Dockerfile` com Multi-Stage Build, otimização de camadas e execução sob usuário sem privilégios de root (`spring:spring`).
- [x] Estruturação do `docker-compose.yaml` com serviços para API, Redis e RabbitMQ com mapeamento de portas e volumes.

### Em Desenvolvimento / Próximos Passos
- [ ] Configuração de infraestrutura AMQP no Spring Boot (definição de *Queues*, *Exchanges* e *Routing Keys* para o RabbitMQ).
- [ ] Implementação de serviço de envio de e-mails transacionais via SMTP (`JavaMailSender`).
- [ ] Criação de *Producer* e *Consumer* assíncronos para processamento de e-mails de confirmação e alerta de recadastro.
- [ ] Implementação de estratégia de cache distribuído (*Cache-Aside*) com Redis e expiração controlada (TTL) para os artigos da NewsAPI.
- [ ] Rotina agendada diária em lote via `@Scheduled` (expressão CRON) para coleta de notícias e distribuição da newsletter para inscritos ativos.
- [ ] Camada de segurança e autenticação com Spring Security e JWT para proteção de rotas administrativas.

---

Desenvolvido por **Márcio Vinícius Souza Veloso** como projeto prático de engenharia de software e arquitetura backend.