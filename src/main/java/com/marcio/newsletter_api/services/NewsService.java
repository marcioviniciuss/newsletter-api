package com.marcio.newsletter_api.services;

import com.marcio.newsletter_api.dtos.news.ArticleResponseDto;
import com.marcio.newsletter_api.dtos.news.NewsResponseDto;
import com.marcio.newsletter_api.dtos.news.newsApi.ArticleDto;
import com.marcio.newsletter_api.dtos.news.newsApi.NewsDto;
import com.marcio.newsletter_api.integrations.NewsApiClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class NewsService {

    private final NewsApiClient newsApiClient;

    public NewsService(NewsApiClient newsApiClient) {
        this.newsApiClient = newsApiClient;
    }

    // Método que o Controller do front-end chamará
    public NewsResponseDto getNewsForPortal(String category, String language, String timeframe, int page, int pageSize) {
        String query = resolveQuery(category);
        String fromDate = calculateFromDate(timeframe);
        String toDate = LocalDate.now().toString();

        NewsDto rawNews = newsApiClient.fetchNews(query, language, fromDate, toDate, page, pageSize);

        return processNewsResponse(rawNews, category, timeframe, page, pageSize);
    }

    // Método que a rotina diária matinal (@Scheduled / RabbitMQ) chamará
    public List<ArticleResponseDto> getNewsForDailyDigest(String category, String language) {
        String query = resolveQuery(category);
        LocalDate today = LocalDate.now();
        String fromDate = today.minusDays(1).toString(); // Ontem
        String toDate = today.toString();                // Hoje cedo

        // Pega as 10 principais notícias mais populares para o disparo por e-mail
        NewsDto rawNews = newsApiClient.fetchNews(query, language, fromDate, toDate, 1, 10);

        if (rawNews == null || rawNews.articles() == null) {
            return Collections.emptyList();
        }

        return rawNews.articles().stream()
                .map(article -> mapToArticleResponse(article, category))
                .toList();
    }

    // Converte e empacota para a estrutura de paginação do front-end
    private NewsResponseDto processNewsResponse(NewsDto rawNews, String category, String timeframe, int page, int pageSize) {
        if (rawNews == null || rawNews.articles() == null) {
            return new NewsResponseDto(page, pageSize, false, timeframe, Collections.emptyList());
        }

        List<ArticleResponseDto> articles = rawNews.articles().stream()
                .map(article -> mapToArticleResponse(article, category))
                .toList();

        // Limite da conta gratuita da NewsAPI é de 100 resultados no total
        int totalResults = rawNews.totalResults() != null ? rawNews.totalResults() : 0;
        boolean hasMore = (page * pageSize) < Math.min(totalResults, 100);

        return new NewsResponseDto(page, pageSize, hasMore, timeframe, articles);
    }

    // Converte cada ArticleDto externo no DTO limpo da sua aplicação com tratamento de nulos
    private ArticleResponseDto mapToArticleResponse(ArticleDto article, String category) {
        return new ArticleResponseDto(
                article.title() != null ? article.title() : "Sem título",
                article.description() != null ? article.description() : "Sem descrição disponível.",
                article.url(),
                article.urlToImage() != null ? article.urlToImage() : "https://placehold.co/600x400/18181b/a1a1aa?text=News",
                article.publishedAt(),
                article.source() != null && article.source().name() != null ? article.source().name() : "Fonte desconhecida",
                category
        );
    }

    // Tradução de categorias para o parâmetro textual 'q' da NewsAPI
    private String resolveQuery(String category) {
        if (category == null) return "tecnologia OR games OR cibersegurança";

        return switch (category.toLowerCase()) {
            case "games" -> "games OR jogos OR videogame";
            case "technology" -> "tecnologia OR programação OR software";
            case "cybersecurity" -> "cybersecurity OR \"segurança da informação\" OR hacker";
            case "politics" -> "política OR governo OR economia";
            default -> "tecnologia OR games OR cibersegurança";
        };
    }

    // Cálculo dinâmico da data limite para o portal
    private String calculateFromDate(String timeframe) {
        LocalDate today = LocalDate.now();
        if ("month".equalsIgnoreCase(timeframe)) {
            return today.minusDays(29).toString(); // Máximo suportado pelo plano gratuito
        }
        return today.minusDays(7).toString();      // Padrão: 7 dias
    }
}