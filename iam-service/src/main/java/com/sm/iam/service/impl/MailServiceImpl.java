package com.sm.iam.service.impl;

import com.sm.iam.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendSimpleMessage() {
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("<MAIL_ID>");
        message.setTo("<MAIL_ID>"); 
        message.setSubject("testing java mail service"); 
        message.setText("This is mail body <b>Yooooo!!!</b>");
        javaMailSender.send(message);
	}

	@Override
	public void sendOtpEmail(String otp, String toEmail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("<MAIL_ID>", "SM Ecommerce Support");
		helper.setTo(toEmail);
		String subject = "";
		String content = "<div style=\"display: flex; flex-direction: column;width: max-content;\">"
				+ "        <div style=\"display: flex; flex-direction: row; width: 100%; justify-content: space-between;\">"
				+ "            <h2>SM</h2>"
				+ "            <h2>Password Assistance</h2>"
				+ "        </div>"
				+ "        <div>"
				+ "            <p>To authenticate, please use the following One Time Password (OTP):</p>"
				+ "            <h3>"+ otp +"</h3>"
				+ "            <p>We hope to see you again soon.</p>"
				+ "        </div>"
				+ "    </div>";
		helper.setSubject(subject);
		helper.setText(content, true);
		javaMailSender.send(message);
	}

}
