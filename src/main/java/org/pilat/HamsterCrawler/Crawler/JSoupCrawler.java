/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pilat.HamsterCrawler.Model.HamsterClientModel;
import org.riversun.okhttp3.OkHttp3CookieHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pilat
 */
@Service
public class JSoupCrawler {

    public Document getDocumentFromURL(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();

        return doc;
    }

    public int getNumberOfFilePages(Document jsoupDocument) {

        // pozyskuje liczbe stron z plikami
        System.out.println("Zaczynam badać liczbę stron z plikami.");
        try {

            Element paginator = jsoupDocument.getElementsByClass("paginator").first();
            String paginatorRel = paginator.getElementsByClass("current").text();
            String paginatorRel2 = paginator.getElementsByTag("a").text();
            // tworze jeden string
            String liczbyStron1 = paginatorRel + paginatorRel2;
            // wyrzucam wszystko poza liczbami odzielam kazda liczbe spacja
            String liczbyStron2 = liczbyStron1.replaceAll("\\D+", " ");
            //System.out.println("Strony " + liczbyStron2);

            // dziele string na poszczgolne liczby,
            String[] tablicaStringowNumeryStron = liczbyStron2.split(" ");

            // parsuje numery
            int[] tablicaIntNumeryStron = new int[tablicaStringowNumeryStron.length];
            for (int n = 0; n < tablicaStringowNumeryStron.length; n++) {
                tablicaIntNumeryStron[n] = Integer.parseInt(tablicaStringowNumeryStron[n]);
                //System.out.println("Numer: " + tablicaIntNumeryStron[n]);
            }

            // wyciagam najwiekszy numer strony (generalnie działa)
            IntSummaryStatistics stat = Arrays.stream(tablicaIntNumeryStron).summaryStatistics();

            System.out.println("Liczba stron na których znajdują się pliki do pobrania: " + stat.getMax());
            return stat.getMax() - 1;
        } catch (Exception e) {
            System.out.println("Liczba stron na których znajdują się pliki do pobrania: " + '0');
            return 0;
        }
    }
// podstawowy url do stron z plikami do pobrania

    public String normalizeHamsterUrl(String url) {

        return StringUtils.substringBefore(url, ",");

    }

    public List getIdListToDownload(String normalizeHamsterUrl, int numberOfPagesWithFiles) throws IOException {

        System.out.println("URL ktory trafia do getIdListToDownload: " + normalizeHamsterUrl + " i numer stron: " + numberOfPagesWithFiles + " co oznacza 1 w JAVA");

        List fileIdList = new ArrayList();

        for (int i = 0; i <= numberOfPagesWithFiles; i++) {

            Document jsoupDocument = Jsoup.connect(normalizeHamsterUrl + "," + i).get();

            // pozyskuje id plikow
            Element filesListContainer = jsoupDocument.getElementById("FilesListContainer");
            Elements links = filesListContainer.getElementsByClass("downloadContext");
            System.out.println("links: " + links.toString());

            // lista id plików
            for (Element link : links) {
                fileIdList.add(StringUtils.substringBetween(link.attr("href").toString(), ",", "."));
            }
        }

        // usuwam duplikaty
        List<Integer> listWithoutDuplicates = (List<Integer>) fileIdList.stream().distinct().collect(Collectors.toList());

        System.out.println("Id plików do pobrania: " + listWithoutDuplicates.toString());
        return listWithoutDuplicates;
    }

    public HamsterClientModel chomikSendAuth(String login, String password) throws IOException {

        HamsterClientModel model = new HamsterClientModel();

        // loguje sie do chomika
        Connection.Response responseJ = Jsoup.connect("https://chomikuj.pl/action/Login/TopBarLogin")
                .data("Login", login)
                .data("Password", password)
                .method(Connection.Method.POST)
                .execute();

        // wyciagam dane i przekazuje do modelu        
        model.setRequestVerificationTokenLw(responseJ.cookie("__RequestVerificationToken_Lw__"));
        model.setChomikSession(responseJ.cookie("ChomikSession"));
        model.setRememberMe(responseJ.cookie("RememberMe"));
        model.setRcid(responseJ.cookie("rcid"));
        model.setGuid(responseJ.cookie("guid"));
        model.setCfduid(responseJ.cookie("__cfduid"));

        // parsuje response do document i wyciągam drugi token        
        Document document = Jsoup.parse(responseJ.body());
        Elements element = document.getElementById("content").getElementsByAttribute("name");
        model.setRequestVerificationToken(element.get(0).val());

        // wyswietlam dane zgromadzone w modelu
        System.out.println("MODEL to STRING: " + model.toString());

        return model;
    }

    public List getUrlsToDownload(List getIdListToDownload, HamsterClientModel model) throws IOException {

        // list linkow do sciagania
        List urlListToDownload = new ArrayList();

        for (int i = 0; i <= getIdListToDownload.size() - 1; i++) {
            OkHttpClient client = new OkHttpClient();
            System.out.println("Wartość i w pętli: " + i);
            // drugie zapytanie
//------------------------------------------------------------------------------------------------------------
            RequestBody formBody = new FormBody.Builder()
                    .add("fileId", getIdListToDownload.get(i).toString())
                    .add("__RequestVerificationToken", model.getRequestVerificationToken())
                    .build();

            String cookieString = "__RequestVerificationToken_Lw__=" + model.getRequestVerificationTokenLw() + "; ChomikSession=" + model.getChomikSession() + "; RememberMe=" + model.getRememberMe() + "; rcid=" + model.getRcid() + "; guid=" + model.getGuid() + "; __cfduid=" + model.getCfduid();

            Request request2 = new Request.Builder()
                    .url("https://chomikuj.pl/action/License/DownloadContext")
                    .addHeader("Cookie", cookieString)
                    .post(formBody)
                    .build();

            Call call2 = client.newCall(request2);
            Response response2 = call2.execute();
            System.out.println("WYNIK 2: " + response2.body().string());

// trzecie zapytanie
//------------------------------------------------------------------------------------------------------------
            RequestBody formBody3 = new FormBody.Builder()
                    .add("fileId", getIdListToDownload.get(i).toString())
                    .add("__RequestVerificationToken", model.getRequestVerificationToken())
                    .build();

            String cookieStringTokenLw = "__RequestVerificationToken_Lw__=" + model.getRequestVerificationTokenLw();
            String cookieStringChomikSession = "ChomikSession=" + model.getChomikSession();
            String cookieStringRememberMe = "RememberMe=" + model.getRememberMe();
            String cookieStringRcid = "rcid=" + model.getRcid();
            String cookieStringGuid = "guid=" + model.getGuid();
            String cookieStringCfduid = "__cfduid=" + model.getCfduid();

            String url = "https://chomikuj.pl/action/License/DownloadWarningAccept";

            OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
            cookieHelper.setCookie(url, "__RequestVerificationToken_Lw__", model.getRequestVerificationTokenLw());
            cookieHelper.setCookie(url, "ChomikSession", model.getChomikSession());
            cookieHelper.setCookie(url, "RememberMe", model.getRememberMe());
            cookieHelper.setCookie(url, "rcid", model.getRcid());
            cookieHelper.setCookie(url, "guid", model.getGuid());
            cookieHelper.setCookie(url, "__cfduid", model.getCfduid());

            OkHttpClient client3 = new OkHttpClient.Builder()
                    .cookieJar(cookieHelper.cookieJar())
                    .build();

            Request request3 = new Request.Builder()
                    .url(url)
                    .post(formBody3)
                    .build();

            Call call3 = client3.newCall(request3);
            Response response3 = call3.execute();

// utrwalam wynik zapytania do string bo po jednorazowym wykorzystaniu response3 się zeruje        
            String jsonResponseString = response3.body().string();

// zamieniam na obiekt json
            JSONObject jo = new JSONObject(jsonResponseString);

// wyciągam wartość i zapisuje na liscie
            urlListToDownload.add(jo.get("redirectUrl").toString());

            //System.out.println("REDIRECT: " + model.getUrlToDownload());
        }

        return urlListToDownload;
    }

    public List fileDownload(List urlListToDownload, String generalFileName) throws MalformedURLException {

        List<String> filesNamesList = new ArrayList();

        for (int i = 0; i <= urlListToDownload.size() - 1; i++) {

            String fileName = generalFileName + "_" + i;
            File myFile = new File("src/main/resources/" + fileName + ".mp3");
            filesNamesList.add(fileName + ".mp3");

            try {
                URL url = new URL(urlListToDownload.get(i).toString());
                File destination = new File(myFile.toString());

                // Copy bytes from the URL to the destination file.
                FileUtils.copyURLToFile(url, destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filesNamesList;
    }

    public void zipFiles(List<String> srcFiles, String generalFileName) throws FileNotFoundException, IOException {

        String fileName = "C:\\Users\\Pilat\\Documents\\NetBeansProjects\\HamsterCrawler2\\" + generalFileName + ".zip";
        FileOutputStream fos = new FileOutputStream(fileName); // lokalizacja spakowanego pliku
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream("src/main/resources/" + fileToZip); // lokalizacja plikow wsadowych
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            fis.close();
        }
        zipOut.close();
        fos.close();

    }

    public void singleFileUpload(String fileName) throws IOException, InterruptedException {

 String name = fileName + ".zip";
 
        String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String host = "s26.zenbox.pl";
        String user = "hamstercrawler%40maciekpilat.pl";
        String pass = "aktsok";
        String filePath = "C:\\Users\\Pilat\\Documents\\NetBeansProjects\\HamsterCrawler2\\" + name;
        //String uploadPath = "/MyProjects/archive/" + name;
        //String uploadPath = "/hamstercrawler/" + name;
        String uploadPath = name;
 
        ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
        System.out.println("Upload URL: " + ftpUrl);
 
        try {
            URL url = new URL(ftpUrl); 
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(filePath);
 
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            inputStream.close();
            outputStream.close();
 
            System.out.println("File uploaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    public void sendLinkToEmail(String fileName) {

        String fileURL = "https://panel-s26.zenbox.pl/CMD_FILE_MANAGER/domains/maciekpilat.pl/public_html//hamstercrawler//" + fileName;

    }

}
