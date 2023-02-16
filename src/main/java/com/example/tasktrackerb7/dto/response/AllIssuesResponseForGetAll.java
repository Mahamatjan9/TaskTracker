package com.example.tasktrackerb7.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllIssuesResponseForGetAll {

    private Boolean isAdmin;

    private List<AllIssuesResponse> allIssuesResponses;
}
