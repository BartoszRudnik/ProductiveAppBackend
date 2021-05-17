package Xeva.productiveApp.email;

public interface EmailSender {

    void send(String to, String email, String subject);
    String buildEmailRegistration(String name, String link);
    String buildEmailPasswordReset(String name, String token);
    String buildEmailAccountDelete(String name, String token);

}
