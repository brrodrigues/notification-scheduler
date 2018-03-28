package br.com.lasa.notificacao.service;

public interface Mailer {

    public void sendEmail(String to, String subject, String message);
}
