package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.MyBuyFundingListDTO;
import com.app.ggumteo.domain.funding.MyFundingBuyerListDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;
    private final PostFileService postFileService;

    @GetMapping("/member/video/my-page")
    public void read(Long id, Model model){
        MemberVO memberDTO = myPageService.getMember(id).orElseThrow();
        model.addAttribute("member", memberDTO);
    }
//    http://localhost:10000/member/video/my-page?id=15

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/work/{page}")
    public MyWorkListDTO getMyVideoWorkList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        return myPageService.getMyVideoWorkList(page, workAndFundingPagination, memberId, postType);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/funding/{page}")
    public MyFundingListDTO getMyVideoFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        log.info("memberId={}", memberId);
        return myPageService.getMyVideoFundingList(page, workAndFundingPagination, memberId, postType);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/funding/{fundingPostId}/buyers/{page}")
    public MyFundingBuyerListDTO getFundingBuyerList(@PathVariable("fundingPostId") Long fundingPostId
            , @PathVariable("page") int page, SettingTablePagination settingTablePagination) {

        log.info("fundingPostId={}", fundingPostId);
        return myPageService.getMyFundingBuyerList(page, settingTablePagination, fundingPostId);
    }

    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/funding/buyers/sendStatus/update")
    public void updateFundingSendStatus(@RequestBody BuyFundingProductDTO buyFundingProductDTO) {
        myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/buy/funding/{page}")
    public MyBuyFundingListDTO getMyBuyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        return myPageService.getMyBuyFundingList(page, workAndFundingPagination, memberId, postType);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/inquiry/{page}")
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination) {

        return myPageService.getMyInquiryHistoryList(page, workAndFundingPagination, memberId);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/inquiry/{inquiryId}/admin-answer")
    public Optional<AdminAnswerDTO> getAdminAnswerByInquiryId(@PathVariable("inquiryId") Long inquiryId) {
        return myPageService.getAdminAnswerByInquiryId(inquiryId);
    }

    //    업로드
    @PostMapping("/member/video/file/upload")
    @ResponseBody
    public List<PostFileDTO> upload(@RequestParam("file") List<MultipartFile> files) {
        try {
            return postFileService.uploadFile(files);  // 서비스의 uploadFile 메서드 호출
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return Collections.emptyList();  // 오류 발생 시 빈 리스트 반환
        }
    }

    @GetMapping("/member/video/file/write")
    public void goToWriteForm() {;}

    //    가져오기
    @GetMapping("/member/video/file/display")
    @ResponseBody
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        File file = new File("C:/upload", fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        return FileCopyUtils.copyToByteArray(file);
    }
}
