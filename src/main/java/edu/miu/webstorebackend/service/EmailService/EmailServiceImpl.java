package edu.miu.webstorebackend.service.EmailService;

import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.model.VerificationToken;
import edu.miu.webstorebackend.service.VerificationTokenService.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

@Service
public class EmailServiceImpl implements EmailService{
    private JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEMail(String destinationEmail, String Subject, String mailContent ) throws MessagingException {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "Utf-8");
            messageHelper.setTo(destinationEmail);
            messageHelper.setSubject("Web Store project");
            messageHelper.setText(mailContent, true);
            mailSender.send(message);

    }
}
