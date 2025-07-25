package com.kh.homework0724.board.vo;

import lombok.Data;

@Data
public class BoardVo {

    private String no;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private String hit;
    private String delYn;
    private String categoryNo;

    private String categoryName;

}
