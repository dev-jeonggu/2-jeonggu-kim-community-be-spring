package com.board.dto;

import java.util.List;

import java.util.Map;

public class BoardListResponse {
    private List<Map<String, Object>> data;
    private boolean hasNext;

    public BoardListResponse() {}

    public BoardListResponse(List<Map<String, Object>> boards, boolean hasNext) {
        this.data = boards;
        this.hasNext = hasNext;
    }

    // NOTE : Getter와 Setter
    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setBoards(List<Map<String, Object>> boards) {
        this.data = boards;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}