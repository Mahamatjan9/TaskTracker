package com.example.tasktrackerb7.dto.converter;

import com.example.tasktrackerb7.db.entities.*;
import com.example.tasktrackerb7.db.repository.*;
import com.example.tasktrackerb7.db.service.serviceimpl.ChecklistServiceImpl;
import com.example.tasktrackerb7.dto.response.*;
import com.example.tasktrackerb7.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardConverter {

    private final CardRepository cardRepository;

    private final EstimationRepository estimationRepository;

    private final LabelRepository labelRepository;

    private final UserRepository userRepository;

    private final ChecklistRepository checklistRepository;

    private final ChecklistServiceImpl checklistService;


    private User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepository.findByEmail(login).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public CardInnerPageResponse convertToCardInnerPageResponse(Card card) {
        CardInnerPageResponse response = new CardInnerPageResponse();
        response.setId(card.getId());
        response.setName(card.getTitle());
        response.setDescription(card.getDescription());
        response.setIsArchive(card.getArchive());
        List<LabelResponse> list = new ArrayList<>();
        if (card.getLabels() != null) {
            for (LabelResponse l: labelRepository.getAllLabelResponse()) {
                LabelResponse labelResponse = new LabelResponse();
                labelResponse.setId(l.getId());
                labelResponse.setId(l.getId());
                labelResponse.setDescription(l.getDescription());
                labelResponse.setColor(l.getColor());
                list.add(labelResponse);
            }
            response.setLabelResponses(list);
        }

        if (card.getEstimation() != null) {
            response.setEstimationResponse(getEstimationByCardId(card.getId()));
        }

        if (card.getUsers() != null) {
            response.setMemberResponses(getAllCardMembers(card.getUsers()));
        }

        response.setChecklistResponses(getChecklistResponses(checklistRepository.getAllChecklists(card.getId())));
        if (card.getComments() != null) {
            response.setCommentResponses(getCommentResponse(card.getComments()));
        }
        return response;
    }

    private List<CommentResponse> getCommentResponse(List<Comment> comments) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                commentResponses.add(convertCommentToResponse(comment));
            }
        }
        return commentResponses;
    }

    private CommentResponse convertCommentToResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getText(), comment.getLocalDateTime(), true, new UserResponse(getAuthenticateUser().getId(), getAuthenticateUser().getName() + " " + getAuthenticateUser().getSurname(), getAuthenticateUser().getPhotoLink()), comment.getUser());
    }

    private List<ChecklistResponse> getChecklistResponses(List<Checklist> checklists) {
        List<ChecklistResponse> checklistResponses = new ArrayList<>();
        if (checklists == null) {
            return checklistResponses;
        } else {
            for (Checklist checklist : checklists) {
                checklistResponses.add(checklistService.convertToResponse(checklist));
            }
        }
        return checklistResponses;
    }

    private EstimationResponse getEstimationByCardId(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NotFoundException("Card with id: " + " not found"));
        Estimation estimation = estimationRepository.findById(card.getEstimation().getId()).orElseThrow(() -> new NotFoundException("estimation with id: " + card.getEstimation().getId() + " not found"));
        return new EstimationResponse(estimation.getId(), estimation.getDateOfStart(), estimation.getDateOfFinish(), estimation.getReminder());
    }

    public List<CardResponse> convertToResponseForGetAll(List<Card> cards) {
        List<CardResponse> cardResponses = new ArrayList<>();
        if (cards == null) {
            return cardResponses;
        } else {
            for (Card card : cards) {
                CardResponse cardResponse = new CardResponse();
                List<LabelResponse> list = new ArrayList<>();

                cardResponse.setId(card.getId());
                cardResponse.setName(card.getTitle());

                for (Label l : card.getLabels()) {
                    LabelResponse labelResponse = new LabelResponse();
                    labelResponse.setId(l.getId());
                    labelResponse.setId(l.getId());
                    labelResponse.setDescription(l.getDescription());
                    labelResponse.setColor(l.getColor());
                    list.add(labelResponse);
                }
                cardResponse.setLabelResponses(list);

                if (card.getEstimation() != null) {
                    int between = Period.between(LocalDate.from(card.getEstimation().getDateOfStart()), LocalDate.from(card.getEstimation().getDateOfFinish())).getDays();
                    cardResponse.setDuration(" " + between + " days");
                }

                cardResponse.setNumOfMembers(card.getUsers().size());
                int item = 0;
                for (Checklist checklist : checklistRepository.getAllChecklists(card.getId())) {
                    for (int i = 0; i < checklist.getItems().size(); i++) {
                        item++;
                    }
                }

                cardResponse.setNumOfItems(item);
                int completedItems = 0;
                for (Checklist c : card.getChecklists()) {
                    for (Item item1 : c.getItems()) {
                        if (item1.getIsDone()) {
                            completedItems++;
                        }
                    }
                }
                if (card.getComments() != null) {
                    cardResponse.setCommentResponses(getCommentResponse(card.getComments()));
                }
                cardResponse.setNumOfCompletedItems(completedItems);
                cardResponses.add(cardResponse);
            }
        }
        return cardResponses;
    }

    public CardResponse convertToResponseForGetAllArchived(Card card) {
        CardResponse cardResponse = new CardResponse();
        cardResponse.setId(card.getId());
        cardResponse.setName(card.getTitle());
        List<LabelResponse> list = new ArrayList<>();
        for (Label l: card.getLabels()) {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setId(l.getId());
            labelResponse.setId(l.getId());
            labelResponse.setDescription(l.getDescription());
            labelResponse.setColor(l.getColor());
            list.add(labelResponse);
        }
        cardResponse.setLabelResponses(list);
        if (card.getEstimation() != null) {
            int between = Period.between(card.getEstimation().getDateOfStart().toLocalDate(), card.getEstimation().getDateOfFinish().toLocalDate()).getDays();
            cardResponse.setDuration(" " + between + " days");
        }

        cardResponse.setNumOfMembers(card.getUsers().size());
        int item = 0;
        for (Checklist checklist : checklistRepository.getAllChecklists(card.getId())) {
            for (int i = 0; i < checklist.getItems().size(); i++) {
                item++;
            }
        }

        cardResponse.setNumOfItems(item);
        int completedItems = 0;
        for (Checklist c : card.getChecklists()) {
            for (Item item1 : c.getItems()) {
                if (item1.getIsDone()) {
                    completedItems++;
                }
            }
        }

        cardResponse.setNumOfCompletedItems(completedItems);
        return cardResponse;
    }

    private List<MemberResponse> getAllCardMembers(List<User> users) {
        List<MemberResponse> memberResponses = new ArrayList<>();
        for (User user : users) {
            memberResponses.add(convertToMemberResponse(user));
        }
        return memberResponses;
    }

    private MemberResponse convertToMemberResponse(User user) {
        return new MemberResponse(user.getId(), user.getUsername(), user.getSurname(), user.getEmail(), user.getPhotoLink());
    }

}
