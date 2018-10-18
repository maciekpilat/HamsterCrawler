package org.pilat.HamsterCrawler;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HamsterCrawlerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HamsterCrawlerApplication.class, args);


    }
}
