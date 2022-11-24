package com.exlab.incubator.services;

public interface MailSender {

    void send(String emailTo, String subject, String message);

}
