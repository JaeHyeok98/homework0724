package com.kh.homework0724.board.service;

import com.kh.homework0724.board.mapper.BoardMapper;
import com.kh.homework0724.board.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;

    public int insertBoard(BoardVo vo) {
        int result = mapper.insertBoard(vo);
        if(result != 1){
            throw new IllegalStateException();
        }
        return result;
    }

    public List<CategoryVo> getCategoryList() {
        List<CategoryVo> voList = mapper.getCategoryList();
        return voList;
    }

    public List<BoardVo> selectBoardList(PageVo pvo, BoardSearchVo srchVo) {
        return mapper.selectBoardList(pvo, srchVo);
    }

    public int getBoardCount(BoardSearchVo srchVo) {
        return mapper.getBoardCount(srchVo);
    }

    public BoardVo selectOneByNo(BoardVo vo) {
        int result = mapper.updateHit(vo);
        if(result!=1){
            throw new IllegalStateException();
        }
        return mapper.selectOneByNo(vo);
    }

    public int updateBoard(BoardVo vo) {
        int result = mapper.updateBoard(vo);
        if(result != 1){
            throw new IllegalStateException();
        }
        return result;
    }

    public int deleteBoard(BoardVo vo) {
        int result = mapper.deleteBoard(vo);
        if(result != 1){
            throw new IllegalStateException();
        }
        return result;
    }

    public List<CommentVo> getCommentList(String no) {
        return mapper.getCommentList(no);
    }

    public int insertBoardComment(CommentVo vo) {
        int result = mapper.insertBoardComment(vo);
        if(result != 1){
            throw new IllegalStateException();
        }
        return result;
    }

    public int deleteBoardComment(CommentVo vo) {
        int result = mapper.deleteBoardComment(vo);
        if(result != 1){
            throw new IllegalStateException();
        }
        return result;
    }

}
