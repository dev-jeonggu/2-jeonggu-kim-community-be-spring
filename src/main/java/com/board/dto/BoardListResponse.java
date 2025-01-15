package com.board.dto;

import java.util.List;

import java.util.Map;

public class BoardListResponse {
    private List<Map<String, Object>> data;
    private boolean hasMore;

    public BoardListResponse() {}

    public BoardListResponse(List<Map<String, Object>> boards, boolean hasMore) {
        this.data = boards;
        this.hasMore = hasMore;
    }

    // NOTE : Getter와 Setter
    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setBoards(List<Map<String, Object>> boards) {
        this.data = boards;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}