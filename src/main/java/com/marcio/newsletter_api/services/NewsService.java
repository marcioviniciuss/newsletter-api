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
        String query = resolveQuery(category, language);
        String fromDate = calculateFromDate(timeframe);
        String toDate = LocalDate.now().toString();

        NewsDto rawNews = newsApiClient.fetchNews(query, language, fromDate, toDate, page, pageSize);

        return processNewsResponse(rawNews, category, timeframe, page, pageSize);
    }

    // Método que a rotina diária matinal (@Scheduled / RabbitMQ) chamará
    public List<ArticleResponseDto> getNewsForDailyDigest(String category, String language) {
        String query = resolveQuery(category, language);
        LocalDate today = LocalDate.now();
        String fromDate = today.minusDays(1).toString();
        String toDate = today.toString();

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

    private String resolveQuery(String category, String language) {
        boolean isEnglish = "en".equalsIgnoreCase(language);
        String cat = category != null ? category.toLowerCase() : "all";

        if (isEnglish) {
            return switch (cat) {
                case "games" -> "gaming";
                case "technology" -> "technology";
                case "cybersecurity" -> "cybersecurity";
                case "politics" -> "politics";
                default -> "technology OR gaming OR politics OR sports OR cybersecurity";
            };
        }

        return switch (cat) {
            case "games" -> "jogos";
            case "technology" -> "tecnologia";
            case "cybersecurity" -> "ciberseguranca OR cyber-segurança OR hacking OR hack";
            case "politics" -> "politica";
            default -> "tecnologia OR games OR jogos OR politica OR esportes OR ciberseguranca OR cyber-segurança OR hacking OR hack";
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