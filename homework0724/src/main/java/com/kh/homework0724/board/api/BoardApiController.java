package com.kh.homework0724.board.api;

import com.kh.homework0724.board.service.BoardService;
import com.kh.homework0724.board.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@CrossOrigin(value = "http://192.168.20.208:5500")
public class BoardApiController {

    private final BoardService service;

    @PostMapping("/insert")
    public ResponseEntity<Integer> insertBoard(@RequestBody BoardVo vo){
        int result = service.insertBoard(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryVo>> getCategoryList(){
        List<CategoryVo> voList = service.getCategoryList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voList);
    }

    @PostMapping("/{currentPage}")
    public ResponseEntity<Map<String, Object>> selectBoardList(
            @PathVariable int currentPage,
            @RequestBody BoardSearchVo srchVo
    ){
        int listCount = service.getBoardCount(srchVo);
        int pageLimit = 5;
        int boardLimit = 5;
        PageVo pvo = new PageVo(listCount, pageLimit, boardLimit, currentPage);
        List<BoardVo> voList = service.selectBoardList(pvo, srchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("voList", voList);
        map.put("pvo", pvo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @PostMapping("/selectOne")
    public ResponseEntity<BoardVo> selectOneByNo(@RequestBody BoardVo vo) {
        BoardVo selectedBoardVo = service.selectOneByNo(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(selectedBoardVo);
    }

    @PutMapping
    public ResponseEntity<Integer> updateBoard(@RequestBody BoardVo vo) {
        int result = service.updateBoard(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteBoard(@RequestBody BoardVo vo) {
        int result = service.deleteBoard(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/comment/{no}")
    public ResponseEntity<List<CommentVo>> getCommentList(@PathVariable String no){
        List<CommentVo> voList = service.getCommentList(no);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voList);
    }

    @PostMapping("/comment/insert")
    public ResponseEntity<Integer> insertBoardComment(@RequestBody CommentVo vo){
        int result = service.insertBoardComment(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<Integer> deleteBoardComment(@RequestBody CommentVo vo){
        int result = service.deleteBoardComment(vo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

}
