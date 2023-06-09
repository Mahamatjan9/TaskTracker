package com.example.tasktrackerb7.api;

import com.example.tasktrackerb7.db.service.ColumnService;
import com.example.tasktrackerb7.dto.request.ColumnRequest;
import com.example.tasktrackerb7.dto.response.ColumnResponse;
import com.example.tasktrackerb7.dto.response.SimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/columns")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Column Api", description = "Column Api")
public class ColumnApi {

    private final ColumnService columnService;


    @Operation(summary = "Create column", description = "Create new column by boardId")
    @PostMapping("/{boardId}")
    @PreAuthorize("isAuthenticated()")
    ColumnResponse create(@PathVariable Long boardId, @RequestBody @Valid ColumnRequest columnRequest) {
        return columnService.create(boardId, columnRequest);
    }

    @Operation(summary = "Update column", description = "Update column name by id")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    ColumnResponse update(@PathVariable Long id, @RequestBody @Valid ColumnRequest columnRequest) {
        return columnService.update(id, columnRequest);
    }

    @Operation(summary = "Delete column", description = "Delete column by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    SimpleResponse delete(@PathVariable Long id) {
        return columnService.delete(id);
    }
}
