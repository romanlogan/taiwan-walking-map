package com.dbproject.web.friend;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.friend.dto.AcceptAddFriendRequest;
import com.dbproject.api.friend.dto.AddFriendRequest;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.service.FriendService;
import com.dbproject.api.friend.service.FriendServiceImpl;
import com.dbproject.api.friend.friendRequest.dto.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.exception.MemberNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class FriendController {

    private final FriendService friendService;

    private final MemberRepository memberRepository;

    @PostMapping("/addFriendRequest")
    public ResponseEntity saveFriendRequest(@Valid @RequestBody AddFriendRequest addFriendRequest,
                                            BindingResult bindingResult,
                                            Principal principal) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        Long requesterId = null;

        try {
            requesterId = friendService.saveFriendRequest(addFriendRequest, principal.getName());
        } catch (MemberNotExistException e) {


        } catch (DuplicateFriendRequestException e) {

        }

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(requesterId),
                null
        ),  HttpStatus.OK);
    }

    @GetMapping(value = {"/requestFriendList", "/requestFriendList/{page}"})
    public String getRequestFriendList(@PathVariable("page") Optional<Integer> page,
                                       Principal principal,
                                       Model model) {


        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<RequestFriendListDto> requestFriendListDtos = friendService.getRequestFriendList(pageable, principal.getName());

        model.addAttribute("requestFriendList", requestFriendListDtos);
        model.addAttribute("maxPage", 5);

        return "myPage/requestFriendList";
    }


    @PostMapping("/acceptAddFriend")
    public ResponseEntity acceptAddFriendRequest(@Valid @RequestBody AcceptAddFriendRequest acceptAddFriendRequest,
                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        Long id = friendService.acceptAddFriend(acceptAddFriendRequest);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ),  HttpStatus.OK);
    }


    @PutMapping("/rejectFriendRequest")
    public ResponseEntity rejectFriendRequest(@Valid @RequestBody RejectFriendRequest deleteFriendRequest,
                                              Principal principal) {

        friendService.rejectFriendRequest(deleteFriendRequest);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(1),
                null
        ),  HttpStatus.OK);
    }

    @GetMapping("/friendList")
    public String getFriendList(Principal principal,
                                Model model) {

//      need try-catch member not exist exception
        FriendListResponse response = friendService.getFriendList(principal.getName());


        model.addAttribute("friendListResponse", response);

        return "myPage/friendList";
    }
}
