package com.tap.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

/**
 * Sends OTP emails via Gmail SMTP using Jakarta Mail API + Angus Mail
 * (angus-mail-2.0.3.jar + jakarta.mail-api-2.1.3.jar must be in WEB-INF/lib).
 *
 * IMPORTANT: Gmail requires an "App Password" (not your normal Gmail password)
 * when 2-Step Verification is enabled. Generate one at:
 * https://myaccount.google.com/apppasswords
 *
 * NOTE ON SPAM FOLDER DELIVERY: sending a multipart (text + HTML) message
 * instead of HTML-only, as done below, meaningfully reduces spam-filter
 * scoring versus HTML-only mail. That said, personal Gmail SMTP relay has
 * inherently limited sender reputation for automated/OTP-style mail sent to
 * addresses it has no history with — the single most effective fix is
 * manually marking the first message from this sender as "Not Spam" in the
 * recipient's Gmail, after which future messages to that same address land
 * in the inbox. For a real production deployment, a dedicated transactional
 * email provider (Amazon SES, SendGrid, Mailgun, etc.) with a verified
 * sending domain would replace this Gmail-relay approach entirely.
 */
public class MailUtil {

    // ---- Update these with your own Gmail sender credentials ----
    private static final String SENDER_EMAIL = "gakhilasajjan123456@gmail.com";
    private static final String SENDER_APP_PASSWORD = "hldksvmvecnuoyig";
    // ---------------------------------------------------------------

    private MailUtil() {}

    private static Session buildSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        return Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD);
            }
        });
    }

    /**
     * Sends the OTP email. Returns true on success, false on failure
     * (failure is logged to stderr but never thrown to the caller so that
     * a broken mail configuration during local dev does not crash the app).
     */
    public static boolean sendOTPEmail(String toEmail, String otpCode, String purpose) {
        try {
            Session session = buildSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "TummyFull"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("TummyFull - Your OTP Code");

            String plainText =
                    "TummyFull\n\n"
                  + "Your One-Time Password for " + purpose + " is: " + otpCode + "\n\n"
                  + "This OTP will expire in " + OTPUtil.OTP_VALIDITY_MINUTES + " minutes.\n\n"
                  + "If you did not request this, please ignore this email.\n";

            String html =
                    "<div style='font-family:Arial,sans-serif;max-width:480px;margin:auto;border:1px solid #eee;border-radius:12px;overflow:hidden;'>"
                  + "<div style='background:#1b3a2b;padding:20px;text-align:center;'>"
                  + "<h2 style='color:#f7c948;margin:0;'>TummyFull</h2></div>"
                  + "<div style='padding:24px;'>"
                  + "<p>Hello,</p>"
                  + "<p>Your One-Time Password for <b>" + escape(purpose) + "</b> is:</p>"
                  + "<h1 style='letter-spacing:6px;color:#1b3a2b;'>" + escape(otpCode) + "</h1>"
                  + "<p>This OTP will expire in " + OTPUtil.OTP_VALIDITY_MINUTES + " minutes.</p>"
                  + "<p style='color:#888;font-size:12px;'>If you did not request this, please ignore this email.</p>"
                  + "</div></div>";

            // multipart/alternative: mail clients pick html if they render html,
            // and spam filters see a legitimate plain-text alternative present -
            // this alone typically improves inbox placement over HTML-only mail.
            MimeMultipart alternative = new MimeMultipart("alternative");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(plainText, "utf-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=utf-8");

            alternative.addBodyPart(textPart);
            alternative.addBodyPart(htmlPart);

            message.setContent(alternative);
            Transport.send(message);
            System.out.println("[TummyFull] OTP email sent successfully to " + toEmail + " (purpose=" + purpose + ")");
            return true;
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            System.err.println("MailUtil: failed to send OTP email to " + toEmail + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("<", "&lt;").replace(">", "&gt;");
    }
}

