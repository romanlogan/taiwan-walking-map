package com.dbproject.api.member.memberImg;

import com.dbproject.api.File.FileService;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
public class MemberImgServiceImpl implements MemberImgService{

    @Value("${memberImgLocation}")
    private String memberImgLocation;

    private final FileService fileService;

    private final MemberRepository memberRepository;

    private final MemberImgRepository memberImgRepository;

    @Autowired
    public MemberImgServiceImpl(FileService fileService, MemberRepository memberRepository, MemberImgRepository memberImgRepository) {
        this.fileService = fileService;
        this.memberRepository = memberRepository;
        this.memberImgRepository = memberImgRepository;
    }

    @Transactional
    public void updateMemberImg(MultipartFile memberImgFile, String email) throws Exception {

        Optional<MemberImg> memberImg = memberImgRepository.findByMemberEmail(email);

        if(memberImg.isPresent()){
            // if already uploaded member img, then update that
            MemberImg savedMemberImg = memberImg.get();

            if (!StringUtils.isEmpty(savedMemberImg.getImgName())) {
//                delete img file that uploaded in the past
                fileService.deleteFile(memberImgLocation + "/" + savedMemberImg.getImgName());
            }

            String oriImgName = memberImgFile.getOriginalFilename();
//          file upload
            String imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
            String imgUrl = "/images/member/" + imgName;
            savedMemberImg.updateMemberImg(imgName, oriImgName, imgUrl);

        }else{
            // save member Img
            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";

            // file upload
            if (!StringUtils.isEmpty(oriImgName)) {
                imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
                imgUrl = "/images/member/" + imgName;
            }

            Member member = memberRepository.findByEmail(email);
            memberImgRepository.save(new MemberImg(imgName, oriImgName, imgUrl, member));
        }
    }
}
