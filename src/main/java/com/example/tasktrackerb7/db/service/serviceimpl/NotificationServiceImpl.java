package com.example.tasktrackerb7.db.service.serviceimpl;

import com.example.tasktrackerb7.db.entities.*;
import com.example.tasktrackerb7.db.repository.*;
import com.example.tasktrackerb7.db.service.NotificationService;
import com.example.tasktrackerb7.dto.response.NotificationResponse;
import com.example.tasktrackerb7.dto.response.SimpleResponse;
import com.example.tasktrackerb7.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;





    private User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepository.findByEmail(login).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    public List<NotificationResponse> getAllNotification() {
        User user = getAuthenticateUser();
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification : user.getNotifications()){
            notificationResponses.add(new NotificationResponse(notification));
        }
        return notificationResponses;
    }

    @Override
    public NotificationResponse getById(Long id) {
       Notification notification = notificationRepository.findById(id).orElseThrow(()-> new NotFoundException("Notification not found!"));
       notification.setStatus(true);
       notificationRepository.save(notification);
        return new NotificationResponse(
                notification.getId(),
                notification.getFromUser().getId(),
                notification.getUser().getId(),
                notification.getBoard().getId(),
                notification.getCard().getId(),
                notification.getBoard().getName(),
                notification.getColumn().getName(),
                notification.getFromUser().getName() + " " + notification.getFromUser().getSurname(),
                notification.getFromUser().getPhotoLink(),
                notification.getDateOfWrite(),
                notification.isStatus(),
                notification.getNotificationType(),
                notification.getText()
        );
    }

    @Override
    public SimpleResponse markAsRead() {
        User user = getAuthenticateUser();
        List<Notification> notifications = notificationRepository.getAllNotification(user.getId());
        for (Notification notification : notifications){
                notification.setStatus(true);
            notificationRepository.save(notification);
        }
        return new SimpleResponse(" Mark as read successfully!");
    }
}
