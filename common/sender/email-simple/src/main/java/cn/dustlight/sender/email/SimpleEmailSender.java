package cn.dustlight.sender.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

public class SimpleEmailSender extends EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    protected void doSend(String subject, String content, String from, String... receivers) throws IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(message);
        try {
            if (from == null)
                from = mailProperties.getUsername();
            h.setFrom(from);
            h.setTo(receivers);
            h.setText(content, true);
            h.setSubject(subject);
        } catch (MessagingException e) {
            throw new IOException("Send email error", e);
        }
        javaMailSender.send(message);
    }
}
