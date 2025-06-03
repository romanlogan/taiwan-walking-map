package com.dbproject.api.member.memberImg;

import org.springframework.web.multipart.MultipartFile;

public interface MemberImgService {

    void updateMemberImg(MultipartFile memberImgFile, String email) throws Exception;
}
