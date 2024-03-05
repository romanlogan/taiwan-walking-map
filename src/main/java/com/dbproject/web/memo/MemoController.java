package com.dbproject.web.memo;

import com.dbproject.api.memo.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

//    @PutMapping("/updateMemo")
//    public ResponseEntity updateMemo(@Valid @RequestBody UpdateMemoRequest updateMemoRequest,
//                                     Principal principal) {
//
////        if (principal == null) {
////            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
////        }
//
//
//
//        Long id = memoService.updateMemo(updateMemoRequest);
//
//        return new ResponseEntity(id, HttpStatus.OK);
//    }
}
