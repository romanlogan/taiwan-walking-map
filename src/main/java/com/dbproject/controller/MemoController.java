package com.dbproject.controller;

import com.dbproject.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PutMapping("updateMemo")
    public ResponseEntity updateMemo(@RequestBody UpdateMemoRequest updateMemoRequest) {

        Long id = memoService.updateMemo(updateMemoRequest);

        return new ResponseEntity(id, HttpStatus.OK);
    }
}
