package org.pilat.HamsterCrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pilat.HamsterCrawler.Model.HamsterClientModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;

@SpringBootApplication
public class HamsterCrawlerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HamsterCrawlerApplication.class, args);

        HamsterClientModel model = new HamsterClientModel();
        OkHttpClient client = new OkHttpClient();

// loguje sie do chomika
        Connection.Response responseJ = Jsoup.connect("https://chomikuj.pl/action/Login/TopBarLogin")
                .data("Login", "jaud")
                .data("Password", "aktsok")
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

// drugie zapytanie
        RequestBody formBody = new FormBody.Builder()
                .add("fileId", "4242588214")
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
                .add("fileId", "4242588214")
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

// wyciągam wartość
model.setUrlToDownload(jo.get("redirectUrl").toString());

        System.out.println("REDIRECT: " + model.getUrlToDownload());



//        
//        Gson gson = new Gson();
//        String jsonRepresentation = gson.toJson(jsonResponseString);
//        //JsonObject object = gson.fromJson(jsonRepresentation, JsonObject.class);        
//JsonElement object = gson.fromJson(jsonRepresentation, JsonElement.class);


//-------------------------------------------------------------------------------------------------------------        
// JSOUP 
//Connection.Response response3 = Jsoup.connect("https://chomikuj.pl/action/License/DownloadWarningAccept")
//         .cookie("__RequestVerificationToken_Lw__", model.getRequestVerificationTokenLw())
//         .cookie("ChomikSession", model.getChomikSession())
//         .cookie("RememberMe", model.getRememberMe())
//         .cookie("rcid", model.getRcid())
//         .cookie("guid", model.getGuid())
//         .cookie("__cfduid", model.getCfduid())
//         .data("fileId", "4242588214")
//         .data("__RequestVerificationToken", model.getRequestVerificationToken())
//         .method(Connection.Method.POST)
//         .execute();
//        System.out.println("WYNIK 3: " + response3.toString());   
//-----------------------------------------------------------------------------------------------------------------
//JSOUP   
//byte[] bytes = Jsoup.connect("https://chomikuj.pl/action/License/DownloadWarningAccept")
//   .cookie("__RequestVerificationToken_Lw__", model.getRequestVerificationTokenLw())
//   .cookie("ChomikSession", model.getChomikSession())
//   .cookie("RememberMe", model.getRememberMe())
//   .cookie("rcid", model.getRcid())
//   .cookie("guid", model.getGuid())
//   .cookie("__cfduid", model.getCfduid())
//   .data("fileId", "4242588214")
//   .data("__RequestVerificationToken", model.getRequestVerificationToken())
//   .header("Accept-Encoding", "gzip, deflate")
//   .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
//   .referrer("https://chomikuj.pl/action/License/DownloadWarningAccept")
//   .ignoreContentType(true)
//   .maxBodySize(0)
//   .timeout(600000)
//   .execute()
//   .bodyAsBytes();
//
//System.out.println("BITY: " + bytes.length);
//------------------------------------------------------------------------------------------------------------------------------         
// OKHttp        
//        
//        
//       //OkHttpClient client = new OkHttpClient();
//
//// pierwsze zapytanie
//RequestBody formBodyId = new FormBody.Builder()
//      .add("Login", "jaud")
//      .add("Password", "aktsok")
//      .build();
//
//Request request1 = new Request.Builder()
//      .url("https://chomikuj.pl/action/License/DownloadContext")
//      .post(formBodyId)
//      .build();
//
//Call call1 = client.newCall(request1);
//Response response1 = call1.execute();
//
//// pozyskuje TOKEN LW , ZAPISUJE W MODEL i WYSWIETLAM
//List<String> headersList;
//headersList = response1.headers("Set-Cookie");
//model.setRequestVerificationTokenLw(headersList.get(5).substring(32, 168));
//
//
//
//System.out.println("BODY: " + response1.body().string());
//System.out.println("HEADERS PRIMITIVE: " + response1.headers());
//System.out.println("HEADERS COOKIE LIST 0: " + headersList.get(0));
//System.out.println("HEADERS COOKIE LIST 1: " + headersList.get(1));
//System.out.println("HEADERS COOKIE LIST 2: " + headersList.get(2));
//System.out.println("HEADERS COOKIE LIST 3: " + headersList.get(3));
//System.out.println("HEADERS COOKIE LIST 4: " + headersList.get(4));
//System.out.println("HEADERS COOKIE LIST 5: " + headersList.get(5));
//System.out.println("HEADERS COOKIE LIST ALL: " + headersList.toString());
//
////System.out.println("TOKEN LW: " + model.getRequestVerificationTokenLw());
//
//model.setCfduid(StringUtils.substringsBetween(headersList.get(0), "__cfduid=", ";").toString());
//model.setGuid(StringUtils.substringsBetween(headersList.get(0), "guid=", ";").toString());
//model.setRcid(StringUtils.substringsBetween(headersList.get(0), "rcid=", ";").toString());
//model.setRcid(StringUtils.substringsBetween(headersList.get(0), "", ";").toString());
//model.setRcid(StringUtils.substringsBetween(headersList.get(0), "", ";").toString());
//model.setRcid(StringUtils.substringsBetween(headersList.get(0), "", ";").toString());
// pozyskuje token z body i przekazuje do modelu
//Document documentId = Jsoup.parse(response1.body().string());
//Elements element = documentId.getElementById("content").getElementsByAttribute("name");
//model.setRequestVerificationToken(element.get(0).val());
//System.out.println("TOKEN: " + model.getRequestVerificationToken());
//Thread.sleep(2000);
//-----------------------------------------------------------------------------------------------------------------
// drugie zapytanie
//      RequestBody formBody = new FormBody.Builder()
//      .add("fileId", "4242588214")
//      .add("__RequestVerificationToken", model.getRequestVerificationToken())
//      .build();
//
//      String cookieString = "__RequestVerificationToken_Lw__=" + model.getRequestVerificationTokenLw();
//       
//       Request request2 = new Request.Builder()
//      .url("https://chomikuj.pl/action/License/DownloadContext")
//      .addHeader("Cookie", cookieString)
//      .post(formBody)
//      .build();
//
//        Call call2 = client.newCall(request2);
//        Response response2 = call2.execute();
//        System.out.println("WYNIK 2: " + response2.body().string()); 
//        
//Thread.sleep(3000);        
//-----------------------------------------------------------------------------------------------------------------
// trzecie zapytanie
//      RequestBody formBody3 = new FormBody.Builder()
//      .add("fileId", "4242588214")
//      .add("__RequestVerificationToken", model.getRequestVerificationToken())
//      .build();
//
//      String cookieString3 = "__RequestVerificationToken_Lw__=" + model.getRequestVerificationTokenLw();
//       
//       Request request3 = new Request.Builder()
//      .url("https://chomikuj.pl/action/License/DownloadWarningAccept")
//      .addHeader("Cookie", cookieString)
//      .post(formBody)
//      .build();
//
//        Call call3 = client.newCall(request3);
//        Response response3 = call3.execute();
//        System.out.println("WYNIK 3: " + response3.body().string()); 
// uzyskuje SERIALIZEDUSERSELECTION i przekazuje do modelu
//        String SerializedUserSelection = "AAEAAAD" + StringUtils.substringBetween(response.body().string(), "AAEAAAD", "==") + "==";
//        model.setSerializedUserSelection(SerializedUserSelection);
//------------------------------------------------------------------------------------------------------------------------------        
//String url = "https://chomikuj.pl/action/Login/TopBarLogin";
//
//	HttpClient client = HttpClientBuilder.create().build();
//	HttpPost post = new HttpPost(url);
//
//	// add header
//	post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
//
//	List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
//	urlParameters.add(new BasicNameValuePair("Login", "jaud"));
//	urlParameters.add(new BasicNameValuePair("Password", "aktsok"));
//
//	post.setEntity(new UrlEncodedFormEntity(urlParameters));
//       
//        HttpGet httpGet = new HttpGet("https://chomikuj.pl/action/Login/TopBarLogin");
//        
//        HttpClientContext context = HttpClientContext.create();
//
//	HttpResponse response = client.execute(post, context);
//        
//        HttpHost target = context.getTargetHost();
//            List<URI> redirectLocations = context.getRedirectLocations();
//            URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
//            System.out.println("Final HTTP location: " + location.toASCIIString());
//
//
//
//        HttpEntity entity = response.getEntity();
//        
//            String content = EntityUtils.toString(entity);
//            System.out.println(content);
//
//        
//----------------------------------------------------------------------------------------------------------------------------------  
// test URL
//    GET / HTTP/1.1
//    Host: chomikuj.pl/action/Login/TopBarLogin
//    Connection: keep-alive
//    Cache-Control: max-age=0
//    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//    Upgrade-Insecure-Requests: 1
//    User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36
//    Accept-Encoding: gzip, deflate, sdch
//    Accept-Language: en-US,en;q=0.8,ru;q=0.6
//    Login=jaud
//    Password=aktsok
//    "
//)
//                .method(Connection.Method.POST)
//                .execute();
//
//
//    :authority: chomikuj.pl
//:method: POST
//:path: /action/License/SearchDownload
//:scheme: https
//accept: */*
//accept-encoding: gzip, deflate, br
//accept-language: pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7
//content-length: 193
//content-type: application/x-www-form-urlencoded
//cookie: __iwa_testCookie_Enabled=enabled; __cfduid=daa3c4284ffa88045dc3d1ea5ee2e971b1535913356; guid=19225611-8a37-4805-84b7-3d1046b0ca15; rcid=9; cookiesAccepted=1; __utmz=215632453.1535913359.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __gfp_64b=xM9wAinDX6Hm4rfMGz50zZRA_2iIuJ1osTndP0comPD.97; chTA=1; chAPC=1; __iwa_vid=d6d93766-b0b2-4383-c45d-9108e4aba804; _ga=GA1.2.900444184.1535913359; __utmv=215632453.|1=User_ID=5799188=1; spt=0; __gchext_c=1; __RequestVerificationToken_Lw__=Iq+Z4rjNOXMjrL7e/a8hn0kMTuV3nCBiYlhYvz0TEc+62yNAz+Z7GTOFfsGpCXsCrteVc/FosD8f81ZwgpW+uelWYd9OLM1fF/QlPRHlJ7l2+K7X0EABcnM+4QZntQTjiKL6iA==; ChomikSession=ac1e806e-2cf2-4532-9d25-cecadcc6c4b4; __utmc=215632453; _gid=GA1.2.1477341406.1537162157; __utma=215632453.900444184.1535913359.1536912657.1537162157.30; __utmt_ch=1; RememberMe=5799188=0076364f4d58d8602b9f229f863ab199; _gat_service=1; __utmb=215632453.5.10.1537162157
//origin: https://chomikuj.pl
//referer: https://chomikuj.pl/action/SearchFiles
//user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36
//x-requested-with: XMLHttpRequest
//          
// koniec test   
////----------------------------------------------------------------------------------------------------------------------------------  
//// Logowanie
//        Connection.Response response1 = Jsoup.connect("https://chomikuj.pl/action/Login/TopBarLogin")
//                .data("Login", "jaud")
//                .data("Password", "aktsok")
//                .method(Connection.Method.POST)
//                .execute();
//
//// zapisuje dane z cookie do modelu
//        model.setCookies(response1.cookies());
////        model.setRequestVerificationTokenLw(response1.cookie("__RequestVerificationToken_Lw__"));
////        model.setChomikSession(response1.cookie("ChomikSession"));
////        model.setRememberMe(response1.cookie("RememberMe"));
////        model.setRcid(response1.cookie("rcid"));
////        model.setGuid(response1.cookie("guid"));
////        model.setCfduid(response1.cookie("__cfduid"));
//          System.out.println("---------------------------------------------------------------------------------------------------");        
//          System.out.println("COOKIES: " + model.getCookies());
//          System.out.println("---------------------------------------------------------------------------------------------------");
//        
//// zapisuje headers
//        model.setHeaders(response1.headers());
//        System.out.println("HEADERS: " + model.getHeaders());
//        System.out.println("---------------------------------------------------------------------------------------------------");
//        
//// zapisuje user agent
//
//// parsuje response do document i wyciągam drugi token        
//        Document document = Jsoup.parse(response1.body());
//        Elements element = document.getElementById("content").getElementsByAttribute("name");
//        model.setRequestVerificationToken(element.get(0).val());
//        System.out.println("TOKEN: " + model.getRequestVerificationToken());
//        System.out.println("---------------------------------------------------------------------------------------------------");
//
//        
//// wyswietlam dane        
// //       System.out.println("!!!!!!!!!!!!!! Dane z MODEL: " + model.toString());
//----------------------------------------------------------------------------------------------------------------------------------        
// wywołujemy stronkę z pożądanym plikeim
//String cookietest = model.getRequestVerificationTokenLw() + "; path=/; domain=.chomikuj.pl; HttpOnly; Expires=Tue, 19 Jan 2038 03:14:07 GMT;";
        //      Connection.Response response2 = Jsoup.connect("https://chomikuj.pl/action/License/DownloadContext")                
//                .cookie("__RequestVerificationToken_Lw__",cookietest)
//                .cookie("ChomikSession", model.getChomikSession().toString())
//                .cookie("RememberMe", model.getRememberMe().toString())                
//                .cookie("rcid", model.getRcid().toString())
//                .cookie("guid", model.getGuid().toString())                
//                .cookie("__cfduid", model.getCfduid().toString())
//                .cookies(cookies)
//                  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
//                  .header("Content-Type", "application/x-www-form-urlencoded")
//                  .header("Cache-Control", "no-cache")
//                .data("__RequestVerificationToken", model.getRequestVerificationToken())
//                .data("fileId", "2383146630")
//                .method(Connection.Method.GET)
//                .execute();
//        System.out.println("DRUGIE ZAPYTANIE: " + response2.body());
//------------------------------------------------------------------------------------------------------------------------------------------------
// OKHTTP
//       OkHttpClient client = new OkHttpClient();
//
//       RequestBody formBody = new FormBody.Builder()
//      .add("fileId", "2383146630")
//      .add("__RequestVerificationToken", "udKRLUJ6ayJYy86Gspqh/W10tv3UG+kTTcQVvyK4I8A/e1Kl7e2moSE9JIMmJoQJs6KBz+qlQHUdM+RsXQJf0uwoHmQ2NNVwJuAaseSSOnOIIbqU96Xo6PMVnA0RXup2idxpYw==")
//      .build();
//
//      String cookieString = "__RequestVerificationToken_Lw__=" + "eROivh9JAFkMiapJcceK0I5wbJbui6aReMlCdgoNyWkY5BAP5FU5LLEtymp2Ii4V9M40WUclnA2WMpvAVYxahl9ueJfqJa5xNDSZFFQ3NscYHiQk2aIxSit7LCtMB0MTHY4hnQ==";
//       
//       Request request = new Request.Builder()
//      .url("https://chomikuj.pl/action/License/DownloadContext")
//      .addHeader("Cookie", cookieString)
//      .post(formBody)
//      .build();
//
//        Call call = client.newCall(request);
//        Response response = call.execute();
//        System.out.println("OK HTTP: " + response.body().string());
//------------------------------------------------------------------------------------------------------------------------------------------------
// najnowsze i tak nie hula
//        System.out.println("KOD ZAPYTANIA: " + Jsoup.connect("https://chomikuj.pl/action/License/DownloadContext"));
//        System.out.println("KOD ZAPYTANIA POST: " + Jsoup.connect("https://chomikuj.pl/action/License/DownloadContext").method(Connection.Method.POST));
//Connection.Response document2 = Jsoup
//                .connect("https://chomikuj.pl/action/License/DownloadContext")
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36")
//                .cookies(model.getCookies())
//        .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
//        .header("Content-Type", "application/x-www-form-urlencoded")
//        .header("Cache-Control", "no-cache")
//        .data("__RequestVerificationToken, +4dPDXQLdobRUAxkkWUH7pDj2h4zMSrnTG5ueDQQdvHzryUUl3tK86/QTe/boTeOHrBf3wpiYidICYNm7QMpB/PkdgdGCTiulEPboX3Fy3+y2AwpQQ3vYQmn8fhNYEZhH2XJMg==,fileId, 2383146630")
//        .data("__RequestVerificationToken","+4dPDXQLdobRUAxkkWUH7pDj2h4zMSrnTG5ueDQQdvHzryUUl3tK86/QTe/boTeOHrBf3wpiYidICYNm7QMpB/PkdgdGCTiulEPboX3Fy3+y2AwpQQ3vYQmn8fhNYEZhH2XJMg==")
//        .data("fileId","2383146630")
//                .header("CF-RAY", "45c1c93afe526afb-WAW")
//                .header("Server", "cloudflare")
//                .header("Connection", "keep-alive")
//                .header("Date", "Tue, 18 Sep 2018 06:34:19 GMT")
//                .header("Cache-Control", "private")
//                .header("Content-Encoding", "gzip")
//                .header("Vary", "Accept-Encoding")
//                .header("Content-Length", "11582")
//                .header("X-Server", "m43")
//                .header("Expect-CT", "max-age=604800")
//                .header("report-uri", "https://report-uri.cloudflare.com/cdn-cgi/beacon/expect-ct")
//                .header("Content-Type", "text/html")
//                .header("charset", "utf-8")
//                .data("__RequestVerificationToken", model.getRequestVerificationToken())
//                .data("fileId", "2383146630")
//                .cookie("ChomikSession", model.getChomikSession().toString())
//                .cookie("RememberMe", model.getRememberMe().toString())                
//                .cookie("rcid", model.getRcid().toString())
//                .cookie("guid", model.getGuid().toString())                
//                .cookie("__cfduid", model.getCfduid().toString())
//                .method(Connection.Method.POST).execute();
//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Drugie zapytanie: " + document2.body());     
//        =, =, =, =, =, =, =, =, =, =, ="", =; =}
// serwer trawi ale nie zwraca dobrego wyniku jakby nie dostał tokena
//HttpResponse<String> response = Unirest.post("https://chomikuj.pl/action/License/DownloadContext")
//  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
//  .header("Content-Type", "application/x-www-form-urlencoded")
//  .header("Cache-Control", "no-cache")
////  .header("Postman-Token", "c8814451-6ef4-4dff-a6f3-5b78913caa9f")
//        .header("Cookie","__RequestVerificationToken_Lw__=JMQ13oyj8W5ga/4VRCUx/xbQqrwJATEo7wYYI40naaJtp1RHC5xr8hRZK6RZY62x9ITlU02vrv79bCuBrxc7mJ7eveHp5jmpR1hRl0waRpmAbliSd5pMAtuhSjIihInxmRCMsA==")
//  .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__RequestVerificationToken\"\r\n\r\n0RG2Hm5cJ9zo4fQRfB0NCFrsgsz9krpmWI4X9klIeDCn9/NGfWJvRREY2O0OHfeGH4tzFVxRqr/pXGQMxMzclquPNr4Re37X9aHYTuxyisJIFtpzBNX+gQntWEgo7rtIbhsynQ==\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"fileId\"\r\n\r\n2383146630\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
//  .asString();
//
//        HttpRequest req = Unirest.post("amy.xy");
//req.header("Cookie", "__RequestVerificationToken_Lw__=JMQ13oyj8W5ga/4VRCUx/xbQqrwJATEo7wYYI40naaJtp1RHC5xr8hRZK6RZY62x9ITlU02vrv79bCuBrxc7mJ7eveHp5jmpR1hRl0waRpmAbliSd5pMAtuhSjIihInxmRCMsA==");
//
//        BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
//
//        System.out.println("ODP Z UNIREST: " + response.getBody());
//---------------------------------------------------------------------------------------------------------------------------------- 
    }
}

//String Login = "jaud";
//String Password = "aktsok";
//String login = Login + ":" + Password;
//String base64login = new String(Base64.encodeBase64(login.getBytes()));
//
//        Connection.Response response1 = Jsoup.connect("https://chomikuj.pl/action/Login/TopBarLogin")                
//                .header("Authorization", "Basic " + base64login)
//                .method(Connection.Method.GET)
//                .execute();
