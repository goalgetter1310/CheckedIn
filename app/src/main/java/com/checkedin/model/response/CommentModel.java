package com.checkedin.model.response;

import com.checkedin.model.Comment;

import java.util.List;

public class CommentModel extends BaseModel {
    private List<Comment> data;
    private int total;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
