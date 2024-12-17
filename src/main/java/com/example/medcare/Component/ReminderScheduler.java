package com.example.medcare.Component;


import com.example.medcare.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderService reminderService;
    @Scheduled(fixedRate = 20000) // 30 seconds
    public void sendReminder() {
        System.out.println("Sending reminder");
        reminderService.sendReminder();
    }

}
