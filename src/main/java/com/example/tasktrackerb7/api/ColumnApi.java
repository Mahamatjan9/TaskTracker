package com.example.tasktrackerb7.api;


import com.example.tasktrackerb7.db.service.serviceimpl.ColumnServiceImpl;
import com.example.tasktrackerb7.dto.request.ColumnRequest;
import com.example.tasktrackerb7.dto.response.ColumnResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/column")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Column Api", description = "Column Api")
public class ColumnApi {

    private final ColumnServiceImpl columnService;


    @Operation(summary = "create", description = "create column by boardId")
    @PostMapping("/{boardId}")
    ColumnResponse create(@PathVariable Long boardId, @RequestBody @Valid ColumnRequest columnRequest) {
        return columnService.create(boardId, columnRequest);
    }

    @Operation(summary = "update", description = "update column by id")
    @PutMapping("/{id}")
    ColumnResponse update(@PathVariable Long id, @RequestBody @Valid ColumnRequest columnRequest) {
        return columnService.update(id, columnRequest);
    }

    @Operation(summary = "delete", description = "delete column by id")
    @DeleteMapping("/{id}")
    ColumnResponse delete(@PathVariable Long id) {
        return columnService.delete(id);
    }

    @Operation(summary = "get all", description = "get all column by boardId")
    @GetMapping("/{boardId}")
    List<ColumnResponse> getAll(@PathVariable Long boardId) {
        return columnService.getAll(boardId);
    }

}
