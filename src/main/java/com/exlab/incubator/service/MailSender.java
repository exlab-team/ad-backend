package com.exlab.incubator.service;

public interface MailSender {

    void send(String emailTo, String subject, String message);

}
