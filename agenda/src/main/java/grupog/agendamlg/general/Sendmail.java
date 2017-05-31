package grupog.agendamlg.general;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * SendMail.java
 *
 * May 3, 2017
 *
 * @author Jean Paul Beaudry
 */
public class Sendmail {

    /**
     * Default email to send notifications via emal
     */
    //public static String username = "agendamlgsii@outlook.com";
    //public final static String username = "agendamlgsii@gmail.com";
    public final static String username = "agendamlgsii@zoho.eu";
    private static final String password = "Q_Z!v#u@SrlO";

    public static void mail(String reciever, String subject, String msg) throws AddressException {

        Properties props = new Properties();
        // GMAIL
        /*
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        */
        
        //ZOHO
        
        props.put("mail.smtp.host", "smtp.zoho.eu");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        //OUTLOOK
        /*
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "outlook.office365.com");
        */

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));
            message.setSubject(subject);
            
            message.setContent(msg, "text/html");
            message.setHeader("Content-Type", "text/html; charset=charset=iso-8859-1");
            //message.setText(msg);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void mailThread(final String reciever, final String subject, final String message){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Sendmail.mail(reciever, subject, message);
                } catch (AddressException ex) {
                    Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
