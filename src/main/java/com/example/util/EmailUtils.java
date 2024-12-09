package com.example.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    private static final String SMTP_SERVER = "smtp.163.com"; // 网易163邮箱的 SMTP 服务器地址
    private static final String EMAIL = "13360804995@163.com"; // 发件人邮箱
    private static final String AUTH_CODE = "GSuZjzAUKb3fDUuY"; // 授权码

    /**
     * 发送邮件
     *
     * @param recipient 收件人邮箱
     * @param subject   邮件主题
     * @param content   邮件正文
     */
    public static void sendEmail(String recipient, String subject, String content) {
        try {
            // 创建邮件配置
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", SMTP_SERVER); // SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
            props.setProperty("mail.transport.protocol", "smtp"); // 使用 SMTP 协议

            // 创建会话对象
            Session session = Session.getInstance(props);
            session.setDebug(true); // 打开 debug 模式以查看日志

            // 创建邮件
            MimeMessage message = createEmail(session, EMAIL, recipient, subject, content);
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件
            Transport transport = session.getTransport();
            transport.connect(EMAIL, AUTH_CODE); // 连接邮件服务器
            transport.sendMessage(message, message.getAllRecipients()); // 发送邮件
            transport.close(); // 关闭连接

            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            throw new RuntimeException("发送邮件时发生错误", e);
        }
    }

    /**
     * 创建邮件
     *
     * @param session    邮件会话
     * @param sender     发件人邮箱
     * @param recipient  收件人邮箱
     * @param subject    邮件主题
     * @param content    邮件正文
     * @return 创建好的邮件对象
     * @throws Exception 如果创建失败
     */
    private static MimeMessage createEmail(Session session, String sender, String recipient, String subject, String content) throws Exception {
        // 创建邮件
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender, "发件人名称", "UTF-8")); // 发件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient, "收件人名称", "UTF-8")); // 收件人
        message.setSubject(subject, "UTF-8"); // 邮件主题
        message.setContent(content, "text/html;charset=UTF-8"); // 邮件正文
        message.setSentDate(new Date()); // 设置发送时间
        message.saveChanges(); // 保存设置
        return message;
    }
}

