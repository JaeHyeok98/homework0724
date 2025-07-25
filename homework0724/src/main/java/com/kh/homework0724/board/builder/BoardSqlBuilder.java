package com.kh.homework0724.board.builder;

import com.kh.homework0724.board.vo.BoardSearchVo;
import com.kh.homework0724.board.vo.PageVo;
import org.apache.ibatis.annotations.Param;

public class BoardSqlBuilder {
    public static String buildSelectBoardList(@Param("pvo") PageVo pvo, @Param("srchVo") BoardSearchVo srchVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT
                B.NO
              , B.TITLE
              , B.CONTENT
              , C.CATEGORY_NAME
            FROM BOARD B
            JOIN CATEGORY C ON B.CATEGORY_NO = C.CATEGORY_NO
            WHERE B.DEL_YN = 'N'
        """);
        if (srchVo.getSearchInput() != null && !srchVo.getSearchInput().isBlank()) {
            String columnName = srchVo.getColumnName();
            String result = setColumnNameAndValue(columnName);
            sql.append(result);
        }
        if (srchVo.getSortBy() != null && !srchVo.getSortBy().isBlank()) {
            String sort = srchVo.getSortBy();
            if (sort.equals("DESC")) {
                sql.append(" ORDER BY B.NO DESC");
            } else {
                sql.append(" ORDER BY B.NO ASC");
            }
        }
        sql.append(" OFFSET #{pvo.offset} ROWS FETCH NEXT #{pvo.boardLimit} ROWS ONLY ");
        return sql.toString();
    }

    public static String buildGetBoardCount(@Param("srchVo") BoardSearchVo srchVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT COUNT(B.NO)
            FROM BOARD B
            JOIN CATEGORY C ON B.CATEGORY_NO = C.CATEGORY_NO
            WHERE B.DEL_YN = 'N'
        """);
        if (srchVo.getSearchInput() != null && !srchVo.getSearchInput().isBlank()) {
            String columnName = srchVo.getColumnName();
            String result = setColumnNameAndValue(columnName);
            sql.append(result);
        }
        return sql.toString();
    }

    public static String setColumnNameAndValue(String columnName) {
        StringBuilder sqlRow = new StringBuilder();
        switch (columnName) {
            case "NO":
                sqlRow.append("AND B.NO = #{srchVo.searchInput}");
                break;
            case "TITLE":
                sqlRow.append("AND B.TITLE LIKE '%' || #{srchVo.searchInput} || '%' ");
                break;
            case "CATEGORY_NAME":
                sqlRow.append("AND C.CATEGORY_NAME = #{srchVo.searchInput}");
                break;
            default:
                break;
        }
        return sqlRow.toString();
    }
}
