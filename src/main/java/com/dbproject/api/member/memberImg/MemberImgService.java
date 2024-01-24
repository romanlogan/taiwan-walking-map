package com.dbproject.api.member.memberImg;

import com.dbproject.api.File.FileService;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;

    private final FileService fileService;

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;


    public void updateMemberImg(MultipartFile memberImgFile, String email) throws Exception {

        Optional<MemberImg> memberImg = memberImgRepository.findByMemberEmail(email);

        if(memberImg.isPresent()){
            // update

            System.out.println("------------------update---------------------");
            MemberImg savedMemberImg = memberImg.get();
            if (!StringUtils.isEmpty(savedMemberImg.getImgName())) {
                fileService.deleteFile(memberImgLocation + "/" + savedMemberImg.getImgName());
            }

            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
            String imgUrl = "/images/member" + imgName;
            savedMemberImg.updateMemberImg(oriImgName, imgName, imgUrl);

        }else{
            //save
            System.out.println("------------------save---------------------");
            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";

            //파일 업로드
            if (!StringUtils.isEmpty(oriImgName)) {
                imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
                imgUrl = "/images/member/"+imgName;
            }

            Member member = memberRepository.findByEmail(email);
            MemberImg newMemberImg = new MemberImg(imgName, oriImgName, imgUrl, member);
            memberImgRepository.save(newMemberImg);

        }

    }
}
