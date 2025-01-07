package com.board.dto;

import java.util.List;

import java.util.Map;

public class BoardListResponse {
    private List<Map<String, Object>> boards;
    private boolean hasNext;

    public BoardListResponse() {}

    public BoardListResponse(List<Map<String, Object>> boards, boolean hasNext) {
        this.boards = boards;
        this.hasNext = hasNext;
    }

    // NOTE : Getter와 Setter
    public List<Map<String, Object>> getBoards() {
        return boards;
    }

    public void setBoards(List<Map<String, Object>> boards) {
        this.boards = boards;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}