/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public void startHamsterCrawler(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        Element filesListContainer = doc.getElementById("FilesListContainer");
        Elements links = filesListContainer.getElementsByClass("downloadContext");
        List fileIdList = new ArrayList();

        // lista id plików
        for (Element link : links) {
            fileIdList.add(StringUtils.substringBetween(link.attr("href").toString(), ",", "."));

        }
    }
}
