package com.example.tasktrackerb7.db.entities;

import com.example.tasktrackerb7.dto.request.EstimationRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "estimations")
@Getter
@Setter
@NoArgsConstructor
public class Estimation {

    @Id
    @SequenceGenerator(name = "estimation_gen", sequenceName = "estimation_seq", allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "estimation_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String reminder;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateOfStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateOfFinish;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH}, fetch = FetchType.LAZY)
    private User creator;

    @OneToOne(cascade = {DETACH, MERGE, REFRESH}, fetch = FetchType.LAZY)
    private Card card;

    public Estimation(EstimationRequest estimationRequest) {
        this.dateOfStart = estimationRequest.getDateOfStart();
        this.dateOfFinish = estimationRequest.getDateOfFinish();
        this.reminder = estimationRequest.getReminderRequest();
    }

}
