package com.dbproject.web.invitePlan;

import com.dbproject.api.friend.service.FriendService;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRouteRequest;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.service.InvitePlanService;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.constant.PlanPeriod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InvitePlanController.class)
class InvitePlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private InvitePlanService invitePlanService;

    @MockBean
    private FriendService friendService;

    public void setInvitePlanMemberRequestList(InvitePlanRequest request){
        List<InvitePlanMemberRequest> invitePlanMemberRequestList = new ArrayList<>();
        InvitePlanMemberRequest invitePlanMemberRequest1 = new InvitePlanMemberRequest("zxcv@zxcv.com","computer");
        InvitePlanMemberRequest invitePlanMemberRequest2 = new InvitePlanMemberRequest("yunni@yunni.com","ipad");
        invitePlanMemberRequestList.add(invitePlanMemberRequest1);
        invitePlanMemberRequestList.add(invitePlanMemberRequest2);
        request.setMemberList(invitePlanMemberRequestList);
    }

    public void setInvitePlanRouteRequestList(InvitePlanRequest request) {
        List<InvitePlanRouteRequest> invitePlanRouteRequestList = new ArrayList<>();

        InvitePlanRouteRequest invitePlanRouteRequest = new InvitePlanRouteRequest(1);
        invitePlanRouteRequest.setLocationRequestList(getInvitePlanLocationRequestList());

        invitePlanRouteRequestList.add(invitePlanRouteRequest);
        request.setRouteList(invitePlanRouteRequestList);
    }

    private static List<InvitePlanLocationRequest> getInvitePlanLocationRequestList() {
        List<InvitePlanLocationRequest> invitePlanLocationRequestList = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {

            InvitePlanLocationRequest invitePlanLocationRequest = new InvitePlanLocationRequest(i);
            invitePlanLocationRequestList.add(invitePlanLocationRequest);
        }

        return invitePlanLocationRequestList;
    }

    //    @BeforeEach
//    LocalDate.of() 가 스태틱이여서 ? 3개 로 돌렸더니 3월60 일이 계속 나온다 ?
    public InvitePlanRequest createRequest(String name, PlanPeriod planPeriod, String supply,int year, int month, int day) {

        LocalDate arriveDate = LocalDate.of(year, month, day);
        LocalDate departDate = LocalDate.of(year, month, day+2);
        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);

        return request;
    }


    @DisplayName("invite 朋友參加 Plan")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void invite() throws Exception {
        //given
//        String name = "lee's 3 days tainan trip";
//        PlanPeriod planPeriod = PlanPeriod.LONGTRIP;
//        String supply = "hair dryer, slipper, brush";
//        LocalDate departDate2 = LocalDate.of(2024, 3, 1);
//        LocalDate arriveDate2 = LocalDate.of(2024, 3, 3);
//        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate2, arriveDate2);

        InvitePlanRequest request = createRequest("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
     }

    @DisplayName("未登入的使用者invite參加Plan時，return 401 UnAuthorization Error")
    @Test
//    @WithMockUser(username = "user", roles = "USER")
    void inviteWithoutLogin() throws Exception {
        //given
        InvitePlanRequest request = createRequest("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("invite朋友參加Plan時name值不能null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithNullName() throws Exception {
        //given
        InvitePlanRequest request = createRequest(null, PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.name.message").value("name值是必要"))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue", nullValue()));
    }


    @DisplayName("invite朋友參加Plan時name值不能Blank")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithBlankName() throws Exception {
        //given
        InvitePlanRequest request = createRequest("    ", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.name.message").value("name值是必要"))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value("    "));
    }

    @DisplayName("invite朋友參加Plan時name值不能超過50個字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithOver50CharacterName() throws Exception {
        //given
        InvitePlanRequest request = createRequest("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.name.message").value("name值是必要1到50個字"))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde"));
    }

    @DisplayName("invite朋友參加Plan時planPeriod值不能null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithNullPlanPeriod() throws Exception {
        //given
        InvitePlanRequest request = createRequest("lee's 3 days tainan trip", null, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.planPeriod.message").value("planPeriod值是必要"))
                .andExpect(jsonPath("$.errorMap.planPeriod.rejectedValue").value(nullValue()));

    }

    @DisplayName("invite朋友參加Plan時supply不能超過1000個字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithOver1000CharSupply() throws Exception {
        //given
        InvitePlanRequest request = createRequest("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "The standard Lorem Ipsum passage, used since the 1500s\n" +
                "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"\n" +
                "\n" +
                "Section 1.10.32 of \"de Finibus Bonorum et Malorum\", written by Cicero in 45 BC\n" +
                "\"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?\"\n" +
                "\n" +
                "1914 translation by H. Rackham\n" +
                "\"But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?\"\n" +
                "\n" +
                "Section 1.10.33 of \"de Finibus Bonorum et Malorum\", written by Cicero in 45 BC\n" +
                "\"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.\"\n" +
                "\n" +
                "1914 translation by H. Rackham\n" +
                "\"On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.\"",
                2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.supply.message").value("supply值只能最多1000個字"))
                .andExpect(jsonPath("$.errorMap.supply.rejectedValue").value("The standard Lorem Ipsum passage, used since the 1500s\n" +
                        "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"\n" +
                        "\n" +
                        "Section 1.10.32 of \"de Finibus Bonorum et Malorum\", written by Cicero in 45 BC\n" +
                        "\"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?\"\n" +
                        "\n" +
                        "1914 translation by H. Rackham\n" +
                        "\"But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?\"\n" +
                        "\n" +
                        "Section 1.10.33 of \"de Finibus Bonorum et Malorum\", written by Cicero in 45 BC\n" +
                        "\"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.\"\n" +
                        "\n" +
                        "1914 translation by H. Rackham\n" +
                        "\"On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.\""));
    }

    @DisplayName("invite朋友參加Plan時departDate值不能null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithNullDepartDate() throws Exception {
        //given
        InvitePlanRequest request = createRequestWithNullDepartDate("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.departDate.message").value("departDate值是必要"))
                .andExpect(jsonPath("$.errorMap.departDate.rejectedValue").value(nullValue()));

    }

    private InvitePlanRequest createRequestWithNullDepartDate(String name, PlanPeriod planPeriod, String supply, int year, int month, int day) {

        LocalDate arriveDate = LocalDate.of(year, month, day);
        LocalDate departDate = null;
        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);

        return request;
    }


    @DisplayName("invite朋友參加Plan時如果DepartDate是Null和Name是Blank的話，會return 400 Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithNullDepartDateAndNullName() throws Exception {
        //given
        InvitePlanRequest request = createRequestWithNullDepartDate("   ", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.departDate.message").value("departDate值是必要"))
                .andExpect(jsonPath("$.errorMap.name.message").value("name值是必要"))

                .andExpect(jsonPath("$.errorMap.departDate.rejectedValue").value(nullValue()))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value("   "));

    }

    @DisplayName("invite朋友參加Plan時如果DepartDate是Null和Name是Blank和NullPlanPeriod的話，會return 400 Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteWithNullDepartDateAndNullNameAndNullPlanPeriod() throws Exception {
        //given
        InvitePlanRequest request = createRequestWithNullDepartDate("   ", null, "hair dryer, slipper, brush", 2024, 3, 20);
        setInvitePlanMemberRequestList(request);
        setInvitePlanRouteRequestList(request);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/plan/invite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.departDate.message").value("departDate值是必要"))
                .andExpect(jsonPath("$.errorMap.name.message").value("name值是必要"))
                .andExpect(jsonPath("$.errorMap.planPeriod.message").value("planPeriod值是必要"))
                .andExpect(jsonPath("$.errorMap.departDate.rejectedValue").value(nullValue()))
                .andExpect(jsonPath("$.errorMap.planPeriod.rejectedValue").value(nullValue()))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value("   "));
    }

}