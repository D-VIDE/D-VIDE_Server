package com.divide.follow;

import com.divide.follow.dto.request.DeleteFollowRequest;
import com.divide.follow.dto.request.PostFollowRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
public class FollowControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void beforeEach(
            RestDocumentationContextProvider restDocumentation
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    @WithUserDetails("email@gmail.com")
    public void getFollow() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/follow")
                .param("first", "0")
                .param("relation", "FOLLOWER")
        );

        // then
        resultActions
                .andDo(document("get-follow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("relation").description("팔로잉을 가져올 지 팔로우를 가져올 지 결정하는 조건문.")
                                        .attributes(key("example").value("FOLLOWING, FOLLOWER")),
                                parameterWithName("first").description("pagination을 위한 skip value").optional()
                                        .attributes(key("example").value("5"))
                        ),
                        responseFields(
                                fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("사용자 id").attributes(key("example").value(5)),
                                fieldWithPath("data.[].profileImgUrl").type(JsonFieldType.STRING).description("프로필 이미지를 가르키는 url주소").attributes(key("example").value("https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/~~~.jpg")),
                                fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임").attributes(key("example").value("닉네임ABC12")),
                                fieldWithPath("data.[].isFFF").type(JsonFieldType.BOOLEAN).description("맞팔로우 여부(relation이 FOLLOWING일 경우에만 반환 됨)").attributes(key("example").value(true))
                        )
                    ));
    }

    @Test
    @WithUserDetails("email@gmail.com")
    public void postFollow() throws Exception {
        // given
        PostFollowRequest postFollowRequest = new PostFollowRequest(5L);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/follow")
                .content(objectMapper.writeValueAsString(postFollowRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(document("post-follow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("팔로우할 user의 id")
                                        .attributes(key("example").value(5))
                        ),
                        responseFields(
                                fieldWithPath("followId").type(JsonFieldType.NUMBER).description("팔로우 id")
                                        .attributes(key("example").value(2))
                        )
                ));
    }

    @Test
    @WithUserDetails("email@gmail.com")
    public void deleteFollow() throws Exception {
        // given
        DeleteFollowRequest postFollowRequest = new DeleteFollowRequest(5L);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/follow")
                .content(objectMapper.writeValueAsString(postFollowRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(document("delete-follow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("언팔로우할 user의 id")
                                        .attributes(key("example").value(5))
                        ),
                        responseFields(
                                fieldWithPath("followId").type(JsonFieldType.NUMBER).description("팔로우 id")
                                        .attributes(key("example").value(2))
                        )
                ));
    }
}
