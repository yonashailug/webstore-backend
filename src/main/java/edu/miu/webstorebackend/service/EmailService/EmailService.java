package edu.miu.webstorebackend.service.EmailService;

import javax.mail.MessagingException;

public interface EmailService {
    public void sendEMail(String destinationEmail, String Subject, String mailContent ) throws MessagingException ;
}
