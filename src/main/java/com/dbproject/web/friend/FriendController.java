package com.dbproject.web.friend;

import com.dbproject.api.friend.AcceptAddFriendRequest;
import com.dbproject.api.friend.AddFriendRequest;
import com.dbproject.api.friend.FriendListResponse;
import com.dbproject.api.friend.FriendService;
import com.dbproject.api.friend.friendRequest.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.RequestFriendListDto;
import com.dbproject.api.member.MemberRepository;
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
@RequestMapping("/myPage")
public class FriendController {

    private final FriendService friendService;

    private final MemberRepository memberRepository;

    @PostMapping("/addFriendRequest")
    public ResponseEntity saveFriendRequest(@Valid @RequestBody AddFriendRequest addFriendRequest,
                                            Principal principal) {
        //validate
//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

        Long requesterId = friendService.saveFriendRequest(addFriendRequest, principal.getName());

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

        return "/myPage/requestFriendList";
    }


    @PostMapping("/acceptAddFriend")
    public ResponseEntity acceptAddFriendRequest(@Valid @RequestBody AcceptAddFriendRequest acceptAddFriendRequest) {

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

        Long id = friendService.acceptAddFriend(acceptAddFriendRequest);

        return new ResponseEntity(id, HttpStatus.OK);
    }


    @PutMapping("/rejectFriendRequest")
    public ResponseEntity rejectFriendRequest(@Valid @RequestBody RejectFriendRequest deleteFriendRequest,
                                              Principal principal) {

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

//        Long id = friendService.acceptAddFriend(deleteFriendRequest);
        friendService.rejectFriendRequest(deleteFriendRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    @GetMapping(value = {"/friendList","/friendList/{page}"})
    public String getFriendList(@PathVariable("page") Optional<Integer> page,
                                Principal principal,
                                Model model) {


        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        String email = principal.getName();
        FriendListResponse friendListResponse = friendService.getFriendList(pageable, email);


//        model.addAttribute("favoriteList", friendPage);
//        model.addAttribute("friendList", friendPage);
        model.addAttribute("friendListResponse", friendListResponse);
        model.addAttribute("maxPage", 5);


        return "/myPage/friendList";
    }
}
