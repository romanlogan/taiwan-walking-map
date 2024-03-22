package com.dbproject.web.invitePlan;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.service.InvitePlanService;
import com.dbproject.binding.CheckBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class InvitePlanController {

    private final InvitePlanService invitePlanService;

    @GetMapping("/")
    public String getPlanForm(){

        return "plan/planForm";
    }

    @PostMapping("/invite")
    public ResponseEntity invite(@Valid @RequestBody InvitePlanRequest request,
                                 BindingResult bindingResult) {


        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        Long invitePlanId = invitePlanService.invitePlan(request);



        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(invitePlanId),
                null
        ), HttpStatus.OK);
    }
}
