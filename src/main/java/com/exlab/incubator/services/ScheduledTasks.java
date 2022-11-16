package com.exlab.incubator.services;

import com.exlab.incubator.entities.User;
import com.exlab.incubator.repositories.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {

    private UserRepository userRepository;

    @Autowired
    public ScheduledTasks(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedDelay = 30000)
    private void checkingUsersForTheEndOfTheVerificationTime(){
        long currentTime = new Date().getTime();
        List<User> users = userRepository.findAll().stream().filter((user) -> user.getIsConfirmed() == false)
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            if ((currentTime  - user.getCreatedAt().getTime()) >= (3600000 * 24)) {
                userRepository.delete(user);
            }
        });
    }

    @Scheduled(fixedDelay = 30000)
    private void checkingForLinkObsolescence(){
        long currentTime = new Date().getTime();
        List<User> users = userRepository.findAll().stream().filter((user) -> user.getIsConfirmed() == false)
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            if ((currentTime  - user.getTimeOfSendingTheConfirmationLink().getTime()) >= 300000) {
                user.setActivationCode("outdated");
                userRepository.save(user);
            }
        });
    }

}
