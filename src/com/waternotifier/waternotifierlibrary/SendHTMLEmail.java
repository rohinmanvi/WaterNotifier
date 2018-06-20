package com.waternotifier.waternotifierlibrary;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendHTMLEmail {

    public static void main(String[] args) {

        final String username = "indiawaternotifier@gmail.com";
        final String password = "Dont4get";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("indiawaternotifier@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("rimgdg@gmail.com"));
            message.setSubject("RENEW SIM Cards - Notice 1 - within one week");
            message.setText("Dear India WN Administrator,"
                    + "\n\n Please renew SIM Cards, by making payments online/nearest dealer within a week !");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
