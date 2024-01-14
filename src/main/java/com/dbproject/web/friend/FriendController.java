package com.dbproject.web.friend;

import com.dbproject.api.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/addFriend")
    public ResponseEntity addFriend(@Valid @RequestBody AddFriendRequest addFriendRequest,
                                    Principal principal) {
        //validate

        Long requesterId = friendService.request(addFriendRequest, principal.getName());

        return new ResponseEntity(requesterId, HttpStatus.OK);
    }

    @GetMapping(value = {"/requestFriendList", "/requestFriendList/{page}"})
    public String getRequestFriendList(@PathVariable("page") Optional<Integer> page,
                                       Principal principal,
                                       Model model) {


        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<RequestFriendListDto> requestFriendListDtos = friendService.getRequestFriendList(pageable, principal.getName());

        model.addAttribute("requestFriendList", requestFriendListDtos);
        model.addAttribute("maxPage", 5);

        return "/member/requestFriendList";
    }

    @PostMapping("/acceptAddFriend")
    public ResponseEntity addFriend(@Valid @RequestBody AcceptAddFriendRequest acceptAddFriendRequest) {

        Long id = friendService.acceptAddFriend(acceptAddFriendRequest);

        return new ResponseEntity(id, HttpStatus.OK);
    }

    @PutMapping("/rejectFriendRequest")
    public ResponseEntity rejectFriendRequest(@Valid @RequestBody RejectFriendRequest deleteFriendRequest) {

//        Long id = friendService.acceptAddFriend(deleteFriendRequest);
        friendService.rejectFriendRequest(deleteFriendRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}
