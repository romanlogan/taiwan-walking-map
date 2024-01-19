package com.dbproject.web.memo;

import com.dbproject.api.memo.MemoService;
import com.dbproject.api.memo.UpdateMemoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    @PutMapping("/updateMemo")
    public ResponseEntity updateMemo(@Valid @RequestBody UpdateMemoRequest updateMemoRequest,
                                     Principal principal) {

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }



        Long id = memoService.updateMemo(updateMemoRequest);

        return new ResponseEntity(id, HttpStatus.OK);
    }
}
