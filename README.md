# 📰 Newsletter API

Uma API RESTful desenvolvida em Java e Spring Boot para automação de envios de newsletters. O sistema permite que usuários se cadastrem, escolham temas de interesse (como Tecnologia e Jogos) e recebam e-mails diários com as principais notícias do dia, consumindo dados de APIs externas.

## 🛠️ Tecnologias Utilizadas

Neste projeto, as seguintes tecnologias estão sendo aplicadas:

*   **Java 21** - Linguagem principal
*   **Spring Boot 3** - Framework para construção da API
*   **PostgreSQL** - Banco de dados relacional em nuvem
*   **Spring Data JPA** - Mapeamento objeto-relacional (ORM)
*   **Spring Security & JWT** - Autenticação e proteção de rotas
*   **Spring Cloud OpenFeign** - Consumo de APIs externas (NewsAPI)
*   **JUnit 5 & Mockito** - Testes unitários

## ⚙️ Funcionalidades (MVP)

*   [x] Estruturação inicial do repositório
*   [ ] Cadastro e autenticação de usuários
*   [ ] Configuração de preferências (ex: Tech, Games)
*   [ ] Rotina agendada (CRON) para busca de notícias
*   [ ] Disparo automatizado de e-mails
*   [ ] Endpoint de disparo manual (com controle de *Rate Limiting*)

## 🚀 Como executar o projeto

*(Esta seção será atualizada conforme avançarmos na construção do ambiente de desenvolvimento e deploy).*