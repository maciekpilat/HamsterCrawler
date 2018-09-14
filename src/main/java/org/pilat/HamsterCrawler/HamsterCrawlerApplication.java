package org.pilat.HamsterCrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pilat.HamsterCrawler.Model.HamsterClientModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HamsterCrawlerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HamsterCrawlerApplication.class, args);

        HamsterClientModel model = new HamsterClientModel();

//----------------------------------------------------------------------------------------------------------------------------------        
        Connection.Response response1 = Jsoup.connect("https://chomikuj.pl/action/Login/TopBarLogin")
                .data("Login", "jaud")
                .data("Password", "aktsok")
                .method(Connection.Method.POST)
                .execute();
        
// zapisuje dane z cookie do modelu
        model.setRequestVerificationTokenLw(response1.cookie("__RequestVerificationToken_Lw__"));
        model.setChomikSession(response1.cookie("ChomikSession"));
        model.setRememberMe(response1.cookie("RememberMe"));
        model.setRcid(response1.cookie("rcid"));
        model.setGuid(response1.cookie("guid"));
        model.setCfduid(response1.cookie("__cfduid"));
        System.out.println("!!!!!!!!!!!!!! Dane z MODEL: " + model.toString());

// parsuje response do document i wyciągam drugi token        
        Document document = Jsoup.parse(response1.body());
        Elements element = document.getElementById("content").getElementsByAttribute("name");
        model.setRequestVerificationToken(element.get(0).val());
        System.out.println("TOKEN: " + model.getRequestVerificationToken().toString());

        
//----------------------------------------------------------------------------------------------------------------------------------        
// wywołujemy stronkę z pożądanym plikeim
        Document document2 = Jsoup
                .connect("https://chomikuj.pl/action/License/DownloadContext")
                .data("__RequestVerificationToken", model.getRequestVerificationToken().toString())
                .data("fileId", "2383146630")
                .cookie("__RequestVerificationToken_Lw__", model.getRequestVerificationTokenLw().toString())
                .cookie("ChomikSession", model.getChomikSession().toString())
                .cookie("RememberMe", model.getRememberMe().toString())                
                .cookie("rcid", model.getRcid().toString())
                .cookie("guid", model.getGuid().toString())                
                .cookie("__cfduid", model.getCfduid().toString())
                .post();
                
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Drugie zapytanie: " + document2.body());
        
        
    }
}
