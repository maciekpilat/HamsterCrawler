/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Controller;

import java.io.IOException;
import java.util.List;
import org.jsoup.nodes.Document;
import org.pilat.HamsterCrawler.Crawler.JSoupCrawler;
import org.pilat.HamsterCrawler.Utils.PhantomSeleniumConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Pilat
 */
@Controller
public class StartPageController {

    @Autowired
    JSoupCrawler jSoupCrawler;
    @Autowired
    PhantomSeleniumConfig phantomSeleniumConfig;
    
    @GetMapping("/start")
    public String getStartPage() {

        return "start";
    }
    
    @PostMapping("/start")
    public String postStartPage(@RequestParam("url")String url) throws IOException{
                
        Document jsoupDocument = jSoupCrawler.getDocumentFromURL(url);
        
        int pagesNumber = jSoupCrawler.getNumberOfFilePages(jsoupDocument);
        
        List idList = jSoupCrawler.getIdListToDownload(jsoupDocument);

        System.out.println("Given URL: " + url + " and NUMBER OF PAGES: " + pagesNumber + " ID: " + idList.toString());
        
        return "redirect:start";
    }
}
