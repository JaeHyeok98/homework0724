package com.kh.homework0724.board.vo;

import lombok.Data;

@Data
public class CommentVo {

    private String boardCommentNo;
    private String boardNo;
    private String content;
    private String createdAt;
    private String delYn;

}
