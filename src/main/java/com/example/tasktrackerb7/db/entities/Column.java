package com.example.tasktrackerb7.db.entities;

import com.example.tasktrackerb7.dto.request.ColumnRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "columns")
@Getter
@Setter
@NoArgsConstructor
public class Column {

    @Id
    @SequenceGenerator(name = "column_gen", sequenceName = "column_seq", allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "column_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH})
    private Board board;

    @OneToMany(cascade = {DETACH, MERGE, REFRESH, REMOVE}, mappedBy = "column")
    private List<Card> cards;

    public Column(ColumnRequest columnRequest) {
        this.name = columnRequest.getName();
    }

    public void addCard(Card card) {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }
}
