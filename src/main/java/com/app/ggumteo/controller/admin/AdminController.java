package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.admin.AnnouncementVO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.report.AuditionReportDTO;
import com.app.ggumteo.domain.report.ReplyReportDTO;
import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.service.admin.AdminService;
import com.app.ggumteo.service.admin.AnnouncementService;
import com.app.ggumteo.service.inquiry.InquiryService;
import com.app.ggumteo.service.report.AuditionReportService;
import com.app.ggumteo.service.report.ReplyReportService;
import com.app.ggumteo.service.report.WorkReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final InquiryService inquiryService;
    private final AnnouncementService announcementService;
    private final WorkReportService workReportService;
    private final ReplyReportService replyReportService;
    private final AuditionReportService auditionReportService;

    // 인증번호 입력 페이지
    @GetMapping("/verify")
    public String showVerifyPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        return "admin/verify-code";
    }

    // 인증 완료 시 관리자 페이지로 이동
    @PostMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyCode(@ModelAttribute AdminDTO adminDTO) {
        Map<String, Object> response = new HashMap<>();
        boolean isVerified = adminService.verifyAdminCode(adminDTO.getAdminVerifyCode());

        if (isVerified) {
            response.put("success", true);
            log.info("인증번호 {}로 관리자 인증 성공!", adminDTO.getAdminVerifyCode());
            response.put("redirect", "/admin/admin");
        } else {
            log.warn("인증 실패 - 입력된 인증번호: {}", adminDTO.getAdminVerifyCode());
            response.put("success", false);
        }
        return response;
    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin/admin";
    }

    // 공지사항 작성
    @PostMapping("/announcement")
    public ResponseEntity<String> writeAnnouncement(@RequestBody AnnouncementVO announcementVO) {
        announcementService.write(announcementVO);
        log.info("공지사항이 등록되었습니다.");
        return ResponseEntity.ok("공지사항이 성공적으로 등록되었습니다.");
    }

    // 공지사항 조회 (검색 및 정렬 기능 포함)
    @GetMapping("/announcements")
    @ResponseBody
    public Map<String, Object> listAnnouncements(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "order", defaultValue = "createdDate") String order,
            @RequestParam(value = "search", required = false) String search) {

        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);
        pagination.setTotal(announcementService.getTotalAnnouncements(search));
        pagination.progress();

        List<AnnouncementVO> announcements = announcementService.getAllAnnouncements(pagination, order, search);
        log.info("공지사항 조회 - 페이지: {}, 검색어: {}, 정렬 기준: {}", page, search, order);

        Map<String, Object> response = new HashMap<>();
        response.put("announcements", announcements);
        response.put("pagination", pagination);

        return response;
    }

    // 공지사항 수정
    @PostMapping("/announcement/update")
    @ResponseBody
    public ResponseEntity<String> updateAnnouncement(@RequestBody AnnouncementVO announcementVO) {
        announcementService.updateAnnouncement(announcementVO);
        log.info("공지사항이 수정되었습니다. ID: {}", announcementVO.getId());
        return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다.");
    }

    // 공지사항 삭제
    @PostMapping("/announcement/delete")
    @ResponseBody
    public ResponseEntity<String> deleteAnnouncements(@RequestBody List<Integer> ids) {
        announcementService.deleteAnnouncements(ids);
        log.info("선택한 공지사항이 삭제되었습니다. 삭제된 ID 목록: {}", ids);
        return ResponseEntity.ok("선택한 공지사항이 성공적으로 삭제되었습니다.");
    }

    // 정렬 및 검색어가 적용된 문의사항 목록 조회 (페이징 처리 포함)
    @GetMapping("/inquiries")
    @ResponseBody
    public Map<String, Object> listInquiries(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "order", required = false, defaultValue = "inquiry-created-date") String order,
            @RequestParam(value = "search", required = false) String search) {

        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 정렬 및 검색을 적용하여 총 문의사항 개수를 가져옴
        int totalInquiries = inquiryService.getTotalInquiries(order, search);
        pagination.setTotal(totalInquiries);
        pagination.progress();

        // 정렬과 검색어가 적용된 문의사항 목록을 가져옴
        List<InquiryDTO> inquiries = inquiryService.getInquiries(pagination, order, search);
        log.info("문의사항 조회 - 페이지: {}, 정렬 기준: {}, 검색어: {}, 문의사항 개수: {}", page, order, search, inquiries.size());

        Map<String, Object> response = new HashMap<>();
        response.put("inquiries", inquiries);
        response.put("pagination", pagination);

        return response;
    }

    // 문의사항 답변 등록
    @PostMapping("/inquiries/{inquiryId}/answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerAnswer(
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> requestData) {

        String answerContent = requestData.get("answerContent");
        Map<String, Object> answerInfo = inquiryService.registerAnswer(inquiryId, answerContent);

        log.info("문의 ID {} 답변 등록 완료. 답변 내용: {}", inquiryId, answerContent);
        return ResponseEntity.ok(answerInfo);
    }

    // 문의사항 삭제
    @PostMapping("/inquiry/delete")
    @ResponseBody
    public ResponseEntity<String> deleteInquiries(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 게시물을 선택하세요.");
        }

        log.info("삭제 요청 ID: {}", ids);  // 요청받은 ID

        try {
            inquiryService.deleteSelectedInquiries(ids);
            log.info("선택된 문의사항이 삭제되었습니다.");
            return ResponseEntity.ok("선택된 문의사항이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("문의사항 삭제 중 오류 발생: ", e);
            //  서버 쪽에 문제 발생을 알림
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문의사항 삭제 중 오류가 발생했습니다.");
        }
    }

    // 회원 정보 조회
    @GetMapping("/members")
    @ResponseBody
    public Map<String, Object> getMembers(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order", required = false) String order) {

        // 페이지네이션 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 전체 회원 수 조회
        int totalMemberCount = adminService.getTotalMemberCount(search, order);
        pagination.setTotal(totalMemberCount); // 총 데이터 개수 설정
        pagination.progress(); // 페이지네이션 계산 수행

        // 회원 정보 조회 (검색, 정렬, 페이지네이션 포함)
        List<MemberProfileDTO> members = adminService.getMembers(search, order, pagination);

        // 로그 정보 출력
        log.info("회원관리 검색어: {}", search);
        log.info("회원관리 정렬 기준: {}", order);
        log.info("페이지네이션 정보: {}", pagination);
        log.info("회원 정보 조회 완료: {}명", members.size());

        // 결과를 반환할 맵 생성
        Map<String, Object> result = new HashMap<>();
        result.put("members", members);
        result.put("pagination", pagination);

        return result;
    }

    // 회원 상태 변경
    @PostMapping("/members/status")
    @ResponseBody
    public ResponseEntity<String> updateMemberStatus(@RequestBody Map<String, Object> requestData) {
        Long memberId = Long.valueOf(requestData.get("memberId").toString());
        String status = requestData.get("status").toString();

        int updateCount = adminService.updateMemberStatus(memberId, status);

        if (updateCount > 0) {
            log.info("회원 ID {}의 상태가 {}로 변경되었습니다.", memberId, status);
            return ResponseEntity.ok("회원 상태가 성공적으로 변경되었습니다.");
        } else {
            log.warn("회원 상태 변경 실패 - 회원 ID: {}", memberId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 상태 변경에 실패했습니다.");
        }
    }

    // 회원 삭제
    @PostMapping("/members/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMembers(@RequestBody List<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 회원을 선택하세요.");
        }

        try {
            adminService.deleteMembersByIds(memberIds); // 회원 삭제
            log.info("총 {}명의 회원이 삭제되었습니다. 삭제된 회원 IDs: {}", memberIds.size(), memberIds);
            return ResponseEntity.ok("회원 삭제가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("회원 삭제 중 오류 발생 - 삭제 요청 IDs: {}", memberIds, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 삭제에 실패했습니다.");
        }
    }

    // 영상 신고 목록
    @GetMapping("/videoReports")
    @ResponseBody
    public Map<String, Object> getVideoReports(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        // 페이징 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 총 데이터 개수를 가져와서 페이징 진행
        pagination.setTotal(workReportService.getVideoReportsCount(search,order));
        pagination.progress();

        // 데이터 조회
        List<WorkReportDTO> reports = workReportService.getVideoReports(search, order, pagination);

        log.info("영상 신고 검색어: {}", search);
        log.info("영상 신고 기준: {}", order);
        log.info("페이지: {}", pagination);
        log.info("{}",reports.size());

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("reports", reports);
        response.put("pagination", pagination);

        return response;
    }

    // 영상 신고 상태 업데이트
    @PostMapping("/videoReports/status")
    @ResponseBody
    public ResponseEntity<String> updateVideoReportStatus(@RequestBody Map<String, Object> requestData) {
        Long workId = Long.valueOf(requestData.get("workId").toString());
        String status = requestData.get("reportStatus").toString();

        try {
            workReportService.updateReportStatus(workId, status);
            log.info("영상 신고 ID {}의 상태가 {}로 변경되었습니다.", workId, status);
            return ResponseEntity.ok("영상 신고 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("영상 신고 상태 변경 중 오류 발생 - 신고 ID: {}", workId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("영상 신고 상태 변경에 실패했습니다.");
        }
    }

    // 글 신고 목록
    @GetMapping("/textReports")
    @ResponseBody
    public Map<String, Object> getTextReports(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        // 페이징 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 총 데이터 개수를 가져와서 페이징 진행
        pagination.setTotal(workReportService.getTextReportsCount(search,order));
        pagination.progress();

        // 데이터 조회
        List<WorkReportDTO> reports = workReportService.getTextReports(search, order, pagination);

        log.info("글 신고 검색어: {}", search);
        log.info("글 신고 정렬 기준: {}", order);
        log.info("{}",reports.size());

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("reports", reports);
        response.put("pagination", pagination);

        return response;
    }

    // 글 신고 상태 업데이트
    @PostMapping("/textReports/status")
    @ResponseBody
    public ResponseEntity<String> updateTextReportStatus(@RequestBody Map<String, Object> requestData) {
        Long workId = Long.valueOf(requestData.get("workId").toString());
        String status = requestData.get("reportStatus").toString();

        try {
            workReportService.updateTextReportStatus(workId, status);
            log.info("글 신고 ID {}의 상태가 {}로 변경되었습니다.", workId, status);
            return ResponseEntity.ok("글 신고 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("글 신고 상태 변경 중 오류 발생 - 신고 ID: {}", workId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 신고 상태 변경에 실패했습니다.");
        }
    }

    // 영상 댓글 신고 목록
    @GetMapping("/videoReplyReports")
    @ResponseBody
    public Map<String, Object> getVideoReplyReports(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "order", required = false, defaultValue = "replyCreatedDate") String order,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        // 페이징 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 총 데이터 개수를 가져와서 페이징 진행
        pagination.setTotal(replyReportService.getVideoReplyReportsCount(search, order));
        pagination.progress();

        // 데이터 조회
        List<ReplyReportDTO> replyReports = replyReportService.getVideoReplyReports(search, order, pagination);

        log.info("영상 댓글 신고 검색어: {}", search);
        log.info("영상 댓글 신고 정렬 기준: {}", order);
        log.info("결과 개수: {}", replyReports.size());

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("reports", replyReports);
        response.put("pagination", pagination);

        return response;
    }


    // 영상 댓글 신고 상태 업데이트
    @PostMapping("/videoReplyReports/status")
    @ResponseBody
    public ResponseEntity<String> updateVideoReplyReportStatus(@RequestBody Map<String, Object> requestData) {
        Long replyId = Long.valueOf(requestData.get("replyId").toString());
        String status = requestData.get("reportStatus").toString();

        try {
            replyReportService.updateReplyReportStatus(replyId, status);
            log.info("영상 댓글 신고 ID {}의 상태가 {}로 변경되었습니다.", replyId, status);
            return ResponseEntity.ok("영상 댓글 신고 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("영상 댓글 신고 상태 변경 중 오류 발생 - 신고 ID: {}", replyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("영상 댓글 신고 상태 변경에 실패했습니다.");
        }
    }

    // 글 댓글 신고 목록
    @GetMapping("/textReplyReports")
    @ResponseBody
    public Map<String, Object> getTextReplyReports(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "order", required = false, defaultValue = "replyCreatedDate") String order,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        // 페이징 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 총 데이터 개수를 가져와서 페이징 진행
        pagination.setTotal(replyReportService.getTextReplyReportsCount(search, order));
        pagination.progress();

        // 데이터 조회
        List<ReplyReportDTO> replyReports = replyReportService.getTextReplyReports(search, order, pagination);

        log.info("글 댓글 신고 검색어: {}", search);
        log.info("글 댓글 신고 정렬 기준: {}", order);
        log.info("결과 개수: {}", replyReports.size());

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("reports", replyReports);
        response.put("pagination", pagination);

        return response;
    }


    // 글 댓글 신고 상태 업데이트
    @PostMapping("/textReplyReports/status")
    @ResponseBody
    public ResponseEntity<String> updateTextReplyReportStatus(@RequestBody Map<String, Object> requestData) {
        Long replyId = Long.valueOf(requestData.get("replyId").toString());
        String status = requestData.get("reportStatus").toString();

        try {
            replyReportService.updateTextReplyReportStatus(replyId, status);
            log.info("글 댓글 신고 ID {}의 상태가 {}로 변경되었습니다.", replyId, status);
            return ResponseEntity.ok("글 댓글 신고 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("글 댓글 신고 상태 변경 중 오류 발생 - 신고 ID: {}", replyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 댓글 신고 상태 변경에 실패했습니다.");
        }
    }

    // 영상 모집 신고 목록
    @GetMapping("/videoAuditionReports")
    @ResponseBody
    public Map<String, Object> getVideoAuditionReports(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "order", required = false, defaultValue = "createdDate") String order,
            @RequestParam(value = "page", defaultValue = "2") Integer page
    ) {
        // 페이징 설정
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 총 데이터 개수를 가져와서 페이징 진행
        pagination.setTotal(auditionReportService.getVideoAuditionReportCount(search, order));
        pagination.progress();

        // 데이터 조회
        List<AuditionReportDTO> reports = auditionReportService.getVideoAuditionReports(search, order, pagination);

        log.info("영상 모집 신고 검색어: {}", search);
        log.info("영상 모집 신고 정렬 기준: {}", order);
        log.info("결과 개수: {}", reports.size());

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("reports", reports);
        response.put("pagination", pagination);

        return response;
    }


    // 영상 모집 신고 상태 업데이트
    @PostMapping("/videoAuditionReports/status")
    @ResponseBody
    public ResponseEntity<String> updateVideoAuditionReportStatus(@RequestBody Map<String, Object> requestData) {
        Long auditionId = Long.valueOf(requestData.get("auditionId").toString());
        String status = requestData.get("reportStatus").toString();

        try {
            auditionReportService.updateVideoAuditionReportStatus(auditionId, status);
            log.info("영상 모집 신고 ID {}의 상태가 {}로 변경되었습니다.", auditionId, status);
            return ResponseEntity.ok("글 댓글 신고 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("영상 모집 신고 상태 변경 중 오류 발생 - 신고 ID: {}", auditionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 댓글 신고 상태 변경에 실패했습니다.");
        }
    }
}


