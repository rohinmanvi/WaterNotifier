package com.waternotifier.waternotifierlibrary;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendHTMLEmail {

    public static void main(String[] args) {

        final String username = "indiawaternotifier@gmail.com";
        final String password = "Dont4get";

        String mailBodyMessage = "";


        // Recipient's email ID needs to be mentioned.
//        String to = "rohin.manvi@gmail.com";
        String toEmailList = "Rajesh Manvi <rimgdg@gmail.com>, Water Notifier - INDIA <indiawaternotifier@gmail.com>";
        String ccEmailList = "Rohin Manvi <rohin.manvi@gmail.com>, Rajesh Manvi <rimgdg@gmail.com>, Rajesh Manvi <manvir@hotmail.com>";

        // Sender's email ID needs to be mentioned
        String from = "Water Notifier - INDIA <indiawaternotifier@gmail.com>";

        // Assuming you are sending email from localhost or smtp.gmail.com
        String host = "smtp.gmail.com";

        // Assuming you are sending email from smtp.gmail.com and its port is 587
//        String port = "587";
        // For SSL
        String port = "465";



        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
//            message.setFrom(new InternetAddress("indiawaternotifier@gmail.com"));
            message.setFrom(new InternetAddress(from));


            // Set To: header field of the header.
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse("rimgdg@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmailList));

            //            message.addRecipient(Message.RecipientType.CC, "rohin.manvi@gmail.com");
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmailList));

            // Set Subject: header field
            message.setSubject("RENEW SIM Cards - Notice 1 - within one week - SSL Based Message");

            // Send the actual HTML message, as big as you like
//            message.setContent("<h1>This is actual message</h1>"
//                    + "<p>Dear India WN Administrator,</p>"
//                    + "<p> Please renew SIM Cards, by making payment online/with nearest dealer only a week left!</p>", "text/html");
            message.setContent(getMessageBody(), "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMessageBody() {
        String message = "";

        ArrayList<SIMCard> listOfSIMCards = new ArrayList<SIMCard>();

        listOfSIMCards = SIMCard.getAllActive();

        message = "<h1>All active SIM Cards</h1>";

        for (int i = 0; i < listOfSIMCards.size(); i++) {

            message =  message + "<li>" + listOfSIMCards.get(i).getPhone() + " valid for " + listOfSIMCards.get(i).getValidityDays() + " "
                    + listOfSIMCards.get(i).getRegisteredDate() + " "
                    + "</li>";
        }

        return message;
    }
}
