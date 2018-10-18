/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Controller;

import java.io.IOException;
import org.pilat.HamsterCrawler.Crawler.JSoupCrawler;
import org.pilat.HamsterCrawler.Model.HamsterClientModel;
import org.pilat.HamsterCrawler.Utils.EmailUtil;
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
    @Autowired
    EmailUtil emailUtil;
    
    @GetMapping("/start")
    public String getStartPage() throws IOException {

        return "start";
    }
    
    @PostMapping("/start")
    public String postStartPage(
            @RequestParam("url")String url,
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            @RequestParam("fileName") String fileName,
            @RequestParam("email") String email)
            throws IOException, InterruptedException{
        
        HamsterClientModel model;
        
        // dane z logowania
        model = jSoupCrawler.chomikSendAuth(login, password);
        
        // podstawowy link z chomika
        model.setNormalizeHamsterUrl(jSoupCrawler.normalizeHamsterUrl(url));
        
        // liczba stron z plikami do pobrania
        model.setNumberOfFilePages(jSoupCrawler.getNumberOfFilePages(jSoupCrawler.getDocumentFromURL(url)));
        
        // lista id plików do pobrania
        model.setIdListToDownload(jSoupCrawler.getIdListToDownload(model.getNormalizeHamsterUrl(), model.getNumberOfFilePages()));
        
        // lista linków do konkretnych plików
        model.setUrlListToDownload(jSoupCrawler.getUrlsToDownload(model.getIdListToDownload(), model));

        System.out.println("MODEL: " + model.toString());
        
        //pobieram plik
        //jSoupCrawler.fileDownload(model.getUrlListToDownload(), "Janko");
        
        // pakuje pliki zip
        jSoupCrawler.zipFiles(jSoupCrawler.fileDownload(model.getUrlListToDownload(), fileName), fileName);
        
        // wysyłam plik na serwer
         jSoupCrawler.singleFileUpload(fileName);

         
         //wysyłam mail z linkiem
         //String downloadURL = "https://panel-s26.zenbox.pl/CMD_FILE_MANAGER/domains/maciekpilat.pl/public_html/hamstercrawler/" + fileName + ".zip";
         String downloadURL = "http://maciekpilat.pl/hamstercrawler/" + fileName + ".zip";
         
         
         model.setUrlToDownloadZipFile(downloadURL);
         
         emailUtil.sendMail(email, downloadURL);
         

        
        return "redirect:start";
    }
}
