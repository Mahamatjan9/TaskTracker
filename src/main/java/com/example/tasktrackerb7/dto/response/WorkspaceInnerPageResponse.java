package com.example.tasktrackerb7.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceInnerPageResponse {

    private String workspaceName;

    private long favoritesCount;

    private int cardsCount;

    private long participantsCount;

    private boolean isAdmin = false;

}
