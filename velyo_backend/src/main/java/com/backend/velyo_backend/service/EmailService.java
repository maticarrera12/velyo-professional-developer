package com.backend.velyo_backend.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String firstName, String lastName, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("¡Bienvenido a Velyo!");
        message.setText("Hola " + firstName + " " + lastName + ",\n\n" +
                "¡Gracias por registrarte en Velyo! Ya podés acceder a la plataforma y comenzar a explorar nuestras opciones de alojamiento.\n\n" +
                "Ingresá desde el siguiente enlace:\n" +
                "http://localhost:5173/iniciar-sesion\n\n" +
                "Si tenés alguna duda o consulta, no dudes en escribirnos.\n\n" +
                "¡Te damos la bienvenida a la comunidad Velyo!\n\n" +
                "Saludos,\n" +
                "El equipo de Velyo");
        mailSender.send(message);
    }

        public void sendConfirmReservation(String firstName, String lastName, String email) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("¡Tu reserva en Velyo fue confirmada!");
            message.setText("Hola " + firstName + " " + lastName + ",\n\n" +
                    "¡Tu reserva ha sido confirmada exitosamente! Gracias por elegir Velyo para tu próxima estadía.\n\n" +
                    "Podés consultar los detalles de tu reserva iniciando sesión en la plataforma:\n" +
                    "http://localhost:5173/iniciar-sesion\n\n" +
                    "Ante cualquier duda, estamos para ayudarte.\n\n" +
                    "¡Te esperamos!\n" +
                    "El equipo de Velyo");
            mailSender.send(message);
    }
}
