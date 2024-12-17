package com.example.medcare.service;


import com.example.medcare.dto.MailBody;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Patient;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReminderService {


    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;


    public void sendReminder() {
        // Define formatter for parsing date strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Fetch all patients

        List<Patient> patients = userRepository.findAllPatients();
        if(patients == null){
            return;
        }
        for (Patient patient : patients) {
            List<Appointment> appointments = appointmentRepository.findAllByPatient(patient);

            for (Appointment appointment : appointments) {
                try {
                    // Parse appointmentTime into LocalDateTime
                    String normalizedAppointmentTime = appointment.getAppointmentTime();

                    LocalDateTime appointmentDateTime = LocalDateTime.parse(normalizedAppointmentTime, formatter);
                    // Add if it is confirmed first
                    if (!appointment.isReminded() &&LocalDateTime.now().plusDays(1).toLocalDate().isEqual(appointmentDateTime.toLocalDate())) {
                        appointment.setReminded(true);
                        MailBody mailBody = MailBody.builder()
                                .to(patient.getEmail())
                                .subject("Reminder: Appointment Tomorrow")
                                .body(
                                        "<html>" +
                                                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333333;'>" +
                                                "<p>Dear " + patient.getFirstName() +" "+patient.getLastName() + ",</p>" +
                                                "<p>This is a friendly reminder that you have an upcoming appointment scheduled for <strong>tomorrow</strong> at: </p>" +
                                                "<p style='font-size: 18px; font-weight: bold; color: #2b8cbe;'>" +
                                                appointmentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")) +
                                                "</p>" +
                                                "<p>Please make sure to arrive on time or notify us in case of any changes.</p>" +
                                                "<p>If you have any questions or need further assistance, feel free to contact our team.</p>" +
                                                "<p>Best regards,<br><strong>The MedCare Team</strong></p>" +
                                                "</body>" +
                                                "</html>"
                                )
                                .build();
                        emailService.sendHtmlMessage(mailBody);
                        appointmentRepository.save(appointment);
                    }

                } catch (Exception e) {
                    // Handle invalid date format or null values
                    System.err.println("Invalid date format for appointment: " + appointment.getAppointmentTime());
                }
            }
        }
    }



}
