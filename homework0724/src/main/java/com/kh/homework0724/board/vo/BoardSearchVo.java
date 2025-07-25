package com.kh.homework0724.board.vo;

import lombok.Data;

@Data
public class BoardSearchVo {
    private String columnName;
    private String searchInput;
    private String sortBy;
}
