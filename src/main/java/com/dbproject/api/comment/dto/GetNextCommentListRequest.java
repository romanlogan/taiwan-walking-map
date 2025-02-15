package com.dbproject.api.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNextCommentListRequest {

    private String locationId;
    private Integer page;

    public GetNextCommentListRequest() {
    }

    public GetNextCommentListRequest(String locationId, Integer page) {
        this.locationId = locationId;
        this.page = page;
    }
}
