package com.example.tasktrackerb7.db.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "workspaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @SequenceGenerator(name = "workspace_gen", sequenceName = "workspace_seq",allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "workspace_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(cascade = {DETACH, MERGE, REFRESH, REMOVE}, mappedBy = "workspace")
    private List<UserWorkspaceRole> members;

    @OneToMany(cascade = {DETACH, MERGE, REFRESH, REMOVE}, mappedBy = "workspace")
    private List<Board> boards;

    private boolean archive;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH})
    private User creator;

    @OneToMany(cascade = {DETACH, MERGE, REFRESH, REMOVE}, mappedBy = "workspace")
    private List<Favourite> favourites;

    @OneToMany(cascade = {ALL}, mappedBy = "workspace")
    private List<Card> allIssues;

    public void addBoard(Board board) {
        if(board == null) {
            boards = new ArrayList<>();
        }
        boards.add(board);
    }

    public void addUserWorkspaceRole(UserWorkspaceRole userWorkspaceRole) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(userWorkspaceRole);
    }
}

