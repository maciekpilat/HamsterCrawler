/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Controller;

import java.io.IOException;
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
        System.out.println("Oto url: " + url);
        jSoupCrawler.startHamsterCrawler(url);
        //phantomSeleniumConfig.initPhantomJS(url);       
        return "redirect:start";
    }
}
