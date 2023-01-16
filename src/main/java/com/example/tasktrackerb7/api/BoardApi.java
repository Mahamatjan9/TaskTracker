package com.example.tasktrackerb7.api;

import com.example.tasktrackerb7.db.service.BoardService;
import com.example.tasktrackerb7.dto.request.BoardRequest;
import com.example.tasktrackerb7.dto.response.BoardResponse;
import com.example.tasktrackerb7.dto.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/board")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardApi {

    private final BoardService boardService;

    @PostMapping
    public BoardResponse create(@RequestBody BoardRequest boardRequest) {
        return boardService.create(boardRequest);
    }

    @PutMapping("/name/{id}")
    public BoardResponse updateName(@PathVariable Long id, @RequestBody @Valid BoardRequest boardRequest) {
        return boardService.updateName(id, boardRequest);
    }

    @PutMapping("/{id}")
    public BoardResponse updateBackground(@PathVariable Long id, @RequestBody @Valid BoardRequest boardRequest) {
        return boardService.updateBackground(id, boardRequest);
    }

    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id) {
        return boardService.delete(id);
    }

    @GetMapping("/workspace/{id}")
    public List<BoardResponse> getAll(@PathVariable Long id) {
        return boardService.getAllByWorkspaceId(id);
    }

    @GetMapping("/{id}")
    public BoardResponse getById(@PathVariable Long id) {
        return boardService.getById(id);
    }

}
