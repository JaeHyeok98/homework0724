package com.kh.homework0724.board.mapper;

import com.kh.homework0724.board.builder.BoardSqlBuilder;
import com.kh.homework0724.board.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO BOARD
            (
                NO
                , TITLE
                , CONTENT
                , CATEGORY_NO
            )
            VALUES
            (
                SEQ_BOARD_NO.NEXTVAL
                , #{title}
                , #{content}
                , #{categoryNo}
            )
            """)
    int insertBoard(BoardVo vo);

    @Select("""
            SELECT
                CATEGORY_NO
                , CATEGORY_NAME
            FROM CATEGORY
            """)
    List<CategoryVo> getCategoryList();

    @SelectProvider(type = BoardSqlBuilder.class, method = "buildSelectBoardList")
    List<BoardVo> selectBoardList(@Param("pvo") PageVo pvo, @Param("srchVo") BoardSearchVo srchVo);

    @SelectProvider(type = BoardSqlBuilder.class, method = "buildGetBoardCount")
    int getBoardCount(@Param("srchVo") BoardSearchVo srchVo);

    @Select("""
            SELECT
                B.NO
                , B.TITLE
                , B.CONTENT
                , B.HIT
                , B.CREATED_AT
                , B.MODIFIED_AT
                , B.DEL_YN
                , C.CATEGORY_NO
                , C.CATEGORY_NAME
            FROM BOARD B
            JOIN CATEGORY C ON B.CATEGORY_NO = C.CATEGORY_NO
            WHERE B.NO = #{no}
            AND B.DEL_YN = 'N'
            """)
    BoardVo selectOneByNo(BoardVo vo);

    @Update("""
            UPDATE BOARD
                SET
                    HIT = HIT+1
            WHERE NO = #{no}
            AND DEL_YN = 'N'
            """)
    int updateHit(BoardVo vo);

    @Update("""
            UPDATE BOARD
                SET
                    TITLE = #{title}
                    , CONTENT = #{content}
                    , MODIFIED_AT = SYSDATE
                    , CATEGORY_NO = #{categoryNo}
            WHERE NO = #{no}
            AND DEL_YN = 'N'
            """)
    int updateBoard(BoardVo vo);

    @Update("""
            UPDATE BOARD
                SET
                    DEL_YN = 'Y'
            WHERE NO = #{no}
            """)
    int deleteBoard(BoardVo vo);

    @Select("""
            SELECT
                BOARD_COMMENT_NO
                , BOARD_NO
                , CONTENT
                , CREATED_AT
            FROM BOARD_COMMENT
            WHERE DEL_YN = 'N'
            AND BOARD_NO = #{no}
            ORDER BY BOARD_COMMENT_NO DESC
            """)
    List<CommentVo> getCommentList(String no);

    @Insert("""
            INSERT INTO BOARD_COMMENT
            (
                BOARD_COMMENT_NO
                , BOARD_NO
                , CONTENT
            )
            VALUES
            (
                SEQ_BOARD_COMMENT_NO.NEXTVAL
                , #{boardNo}
                , #{content}
            )
            """)
    int insertBoardComment(CommentVo vo);

    @Update("""
            UPDATE BOARD_COMMENT
                SET
                    DEL_YN = 'Y'
            WHERE BOARD_NO = #{boardNo}
            AND BOARD_COMMENT_NO = #{boardCommentNo}
            """)
    int deleteBoardComment(CommentVo vo);

}
