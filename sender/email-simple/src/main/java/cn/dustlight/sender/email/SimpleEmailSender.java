package cn.dustlight.sender.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
public class SimpleEmailSender extends EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    protected void doSend(String subject, String content, String from, String... receivers) throws IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(message);
        try {
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
