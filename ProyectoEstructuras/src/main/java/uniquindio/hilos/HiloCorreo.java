package uniquindio.hilos;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class HiloCorreo extends Thread{
    String correoDestinatario;
    String contendido;
    String contraseniaTemp;
    public void run(){
        mandarCorreo();
    }

    public HiloCorreo(String correoDestinatario) {
        this.correoDestinatario = correoDestinatario;
    }

    public HiloCorreo(String correoDestinatario, String contendido, String contraseniaTemp) {
        this.correoDestinatario = correoDestinatario;
        this.contendido = contendido;
        this.contraseniaTemp = contraseniaTemp;
    }

    public HiloCorreo(String correoDestinatario, String contenido) {
        this.correoDestinatario = correoDestinatario;
        this.contendido = contenido;
        this.contraseniaTemp = "";
    }

    private void mandarCorreo(){
        final String username = "erikpablot28@gmail.com"; // Tu dirección de correo
        final String password = "mqbk kflx ucen jeqv"; // Tu contraseña

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Cambia esto si usas otro servidor SMTP
        props.put("mail.smtp.port", "587"); // Puerto SMTP de Gmail

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correoDestinatario)); // Destinatario
            message.setSubject("Correo notificacion pasteleria");
//            if(!contraseniaTemp.equals("")){
//
//                message.setContent(contendido, "text/html");
//                //message.setText(contendido+" "+contraseniaTemp);
//            }else {
                message.setContent(contendido, "text/html");
            //}



            Transport.send(message);

            System.out.println("Correo enviado exitosamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }

    }
}
