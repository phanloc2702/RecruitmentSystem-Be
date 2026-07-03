package org.example.recruitmentsystem.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendPasswordResetEmail(
            String to,
            String resetLink
    ) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Đặt lại mật khẩu JobViet");

            helper.setText("""
                    <h2>Xin chào %s,</h2>

                    <p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản JobViet.</p>

                    <p>
                        <a href="%s">
                            Nhấn vào đây để đặt lại mật khẩu
                        </a>
                    </p>

                    <p>Liên kết chỉ có hiệu lực trong <b>15 phút</b>.</p>

                    <p>Nếu không phải bạn thực hiện, hãy bỏ qua email này.</p>

                    <br/>

                    <p>JobViet Team</p>
                    """.formatted("", resetLink), true);

            mailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException("Không thể gửi email");
        }

    }
}