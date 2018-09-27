/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pilat
 */
@Service
public class JSoupCrawler {

    public Document getDocumentFromURL(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        System.out.println("Data from URL: " + doc.toString());

        return doc;
    }

    public int getNumberOfFilePages(Document jsoupDocument) {

        // pozyskuje liczbe stron z plikami
        Element paginator = jsoupDocument.getElementsByClass("paginator").first();
        System.out.println("2. Paginator: " + paginator.toString());

        String paginatorRel = paginator.getElementsByClass("current").text();

        String paginatorRel2 = paginator.getElementsByTag("a").text();

        String liczbyStron = paginatorRel + StringUtils.substring(paginatorRel2, 17);

        System.out.println("Strony " + liczbyStron);

        // dziele string,
        String[] tablicaStringowNumeryStron = liczbyStron.split(" ");

        // parsuje numery
        int[] tablicaIntNumeryStron = new int[tablicaStringowNumeryStron.length];
        for (int n = 0; n < tablicaStringowNumeryStron.length; n++) {
            tablicaIntNumeryStron[n] = Integer.parseInt(tablicaStringowNumeryStron[n]);
            System.out.println("Numer: " + tablicaIntNumeryStron[n]);
        }

        // wyciagam najwiekszy numer strony (generalnie działa)
        IntSummaryStatistics stat = Arrays.stream(tablicaIntNumeryStron).summaryStatistics();

        return stat.getMax();

    }

    public List getIdListToDownload(Document jsoupDocument) throws IOException {

        System.out.println("Data from jsoupDocument: " + jsoupDocument.toString());

        // pozyskuje id plikow
        Element filesListContainer = jsoupDocument.getElementById("FilesListContainer");
        Elements links = filesListContainer.getElementsByClass("downloadContext");
        List fileIdList = new ArrayList();

        // lista id plików
        for (Element link : links) {
            fileIdList.add(StringUtils.substringBetween(link.attr("href").toString(), ",", "."));

        }
        System.out.println("Link: " + fileIdList.toString());

        return fileIdList;
    }
}
