package com.example.tasktrackerb7.db.service;

import com.example.tasktrackerb7.dto.request.LabelRequest;
import com.example.tasktrackerb7.dto.response.LabelResponse;
import com.example.tasktrackerb7.dto.response.SimpleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LabelService {

    LabelResponse createLabel(LabelRequest labelRequest);

    List<LabelResponse> getAllLabelsByCardId(Long cardId);

    LabelResponse updateLabel(LabelRequest labelRequest, Long labelId);

    SimpleResponse deleteLabelById(Long id);

    SimpleResponse deleteLabelByIdFromCard(Long cardId, Long labelId);

    void assignLabelToCard(Long labelId, Long cardId);


}
