package com.biblioteca.api.service;

import com.biblioteca.api.dto.response.DadosLivroDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LivroScrapingService {

    public DadosLivroDTO extrairDadosLivro(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                    .timeout(8000)
                    .get();

            String html = doc.html().toLowerCase();
            if (html.contains("captcha") || html.contains("robot check") || html.contains("validateCaptcha")) {
                throw new RuntimeException("Página protegida por captcha da Amazon.");
            }

            Element tituloEl = doc.selectFirst("#productTitle");
            System.out.println("Elemento título encontrado: " + tituloEl);

            String titulo = tituloEl != null ? tituloEl.text().trim() : null;

            String precoInteiro = obterTexto(doc, ".a-price-whole");
            String precoFracao = obterTexto(doc, ".a-price-fraction");

            BigDecimal preco;
            if (precoInteiro != null && precoFracao != null) {
                preco = new BigDecimal(precoInteiro.replaceAll("[^\\d]", "") + "." + precoFracao.replaceAll("[^\\d]", ""));
                System.out.println("Preço formatado: " + preco);
            } else {
                throw new RuntimeException("Preço não encontrado na página.");
            }

            Integer ano = extrairAno(doc);
            String isbn = extrairIsbn(doc);

            if (titulo == null) throw new RuntimeException("Título não encontrado");
            if (ano == null) throw new RuntimeException("Ano de publicação não encontrado");
            if (isbn == null || isbn.equals("0000000000")) throw new RuntimeException("ISBN inválido ou não encontrado");

            return new DadosLivroDTO(titulo, isbn, ano, preco);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao acessar a URL: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair dados do livro: " + e.getMessage());
        }
    }

    private Integer extrairAno(Document doc) {
        Pattern anoPattern = Pattern.compile("\\b(19|20)\\d{2}\\b");
        Matcher matcher = anoPattern.matcher(doc.text());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 2000;
    }

    private String obterTexto(Document doc, String seletorCss) {
        Element el = doc.selectFirst(seletorCss);
        if (el != null) {
            System.out.println("Texto encontrado para seletor [" + seletorCss + "]: " + el.text());
            return el.text().trim();
        } else {
            System.out.println("Nenhum elemento encontrado para seletor: " + seletorCss);
            return null;
        }
    }

    private String extrairIsbn(Document doc) {
        String textoCompleto = doc.text();

        Pattern padraoIsbn = Pattern.compile(
                "ISBN(?:-13)?:?\\s?(97[89][-\\s]?[\\d\\-\\s]{10,17})|ISBN(?:-10)?:?\\s?([\\dX-]{10,13})",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = padraoIsbn.matcher(textoCompleto);

        if (matcher.find()) {
            String isbn = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            return isbn.replaceAll("[^\\dX]", "");
        }

        return "0000000000";
    }
}
