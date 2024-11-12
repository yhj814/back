package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.funding.FundingVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.buy.BuyFundingProductDAO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Primary
@Slf4j
public class FundingServiceImpl implements FundingService{


    private final FundingDAO fundingDAO;
    private final PostDAO postDAO;
    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;
    private final PostFileService postFileService;
    private final BuyFundingProductDAO buyFundingProductDAO;
    private final FundingVO fundingVO;

    @Override
    public void write(FundingDTO fundingDTO) {
        // 게시글 저장
        PostVO postVO = new PostVO();
        postVO.setPostTitle(fundingDTO.getPostTitle());
        postVO.setPostContent(fundingDTO.getPostContent());
        postVO.setPostType(fundingDTO.getPostType());
        postVO.setMemberProfileId(fundingDTO.getMemberProfileId());

        postDAO.save(postVO);
        Long postId = postVO.getId();
        if (postId == null) {
            throw new RuntimeException("게시글 ID 생성 실패");
        }

        // 펀딩 ID 설정
        fundingDTO.setId(postId);

        // 썸네일 파일 처리
        String thumbnailFileName = fundingDTO.getThumbnailFileName();
        if (thumbnailFileName != null) {
            FileVO thumbnailFileVO = new FileVO();
            thumbnailFileVO.setFileName(thumbnailFileName);
            thumbnailFileVO.setFilePath(getPath());
            File file = new File("C:/upload/" + getPath() + "/" + thumbnailFileName);
            thumbnailFileVO.setFileSize(String.valueOf(file.length()));

            // 파일 정보 데이터베이스에 저장
            fileDAO.save(thumbnailFileVO);

            // 썸네일 파일 ID 설정
            fundingDTO.setThumbnailFileId(thumbnailFileVO.getId());
        } else {
            fundingDTO.setThumbnailFileId(null);
        }

        // 펀딩 엔티티 저장
        fundingDAO.save(fundingDTO); // FundingDTO를 전달

        // 펀딩 상품 삽입 처리
        List<FundingProductVO> fundingProducts = fundingDTO.getFundingProducts();
        if (fundingProducts != null && !fundingProducts.isEmpty()) {
            for (FundingProductVO product : fundingProducts) {
                product.setFundingId(postId); // 펀딩 ID 설정
                fundingDAO.saveFundingProduct(product);
                log.info("펀딩 상품 저장 완료: {}", product);
            }
        } else {
            log.error("펀딩 상품이 없습니다.");
        }

        // 업로드된 파일 처리
        List<String> fileNames = fundingDTO.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            for (String fileName : fileNames) {
                if (fileName != null && !fileName.isEmpty()) {
                    FileVO fileVO = new FileVO();
                    fileVO.setFileName(fileName);
                    fileVO.setFilePath(getPath());
                    File file = new File("C:/upload/" + getPath() + "/" + fileName);
                    fileVO.setFileSize(String.valueOf(file.length()));

                    // 파일 정보 데이터베이스에 저장
                    fileDAO.save(fileVO);

                    // 파일과 게시글의 연관 관계 설정
                    PostFileVO postFileVO = new PostFileVO(postId, fileVO.getId());
                    postFileDAO.insertPostFile(postFileVO);
                }
            }
        }
    }
    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
    @Override
    public List<FundingDTO> findFundingList(Search search, Pagination pagination) {
        pagination.progress2();
        List<FundingDTO> fundings = fundingDAO.findAllFundingList(search, pagination);
        log.info("Retrieved FundingDTO list: {}", fundings);

        for (FundingDTO funding : fundings) {
            if (funding.getThumbnailFileName() != null) {
                String thumbnailFileName = "t_" + funding.getThumbnailFileName();
                String filePath = funding.getThumbnailFilePath(); // 파일 경로
                String fullPath = filePath + "/" + thumbnailFileName;
                funding.setThumbnailFilePath(fullPath);
                log.info("Set thumbnailFilePath for Funding ID {}: {}", funding.getId(), fullPath);
            } else {
                log.warn("Funding ID {} has no thumbnailFileName", funding.getId());
                funding.setThumbnailFilePath(null); // 명시적으로 null 설정
            }
        }

        return fundings;
    }

    @Override
    public FundingDTO findFundingById(Long id) {
        FundingDTO funding = fundingDAO.findFundingId(id);
        if (funding == null) {
            throw new RuntimeException("펀딩을 찾을 수 없습니다: ID " + id);
        }
        return funding;
    }


    @Override
    public FundingDTO findFundingId(Long id) {
        return fundingDAO.findFundingId(id);
    }




    @Override
    public void updateFunding(FundingDTO fundingDTO, List<Long> deletedFileIds) {
        log.info("업데이트 요청 - 펀딩 ID: {}", fundingDTO.getId());
        log.info("삭제할 파일 IDs: {}", deletedFileIds);
        log.info("삭제할 펀딩 상품 IDs: {}", fundingDTO.getFundingProductIds());
        log.info("새로운 썸네일 파일명: {}", fundingDTO.getThumbnailFileName());

        // 기존 데이터를 다시 조회하여 최신 정보를 가져옴
        FundingDTO currentFunding = fundingDAO.findFundingId(fundingDTO.getId());
        if (currentFunding == null) {
            throw new RuntimeException("펀딩을 찾을 수 없습니다: ID " + fundingDTO.getId());
        }
        Long currentThumbnailFileId = currentFunding.getThumbnailFileId();

        // 썸네일 파일 교체 처리
        if (fundingDTO.getThumbnailFileName() != null && !fundingDTO.getThumbnailFileName().isEmpty()) {
            // 기존 썸네일 파일의 외래 키 참조 해제 및 삭제
            if (currentThumbnailFileId != null) {
                fundingDAO.updateThumbnailFileId(fundingDTO.getId(), null);  // 썸네일 파일 ID를 먼저 null로 설정
                fileDAO.deleteFile(currentThumbnailFileId);
                log.info("기존 썸네일 파일 삭제 완료: 썸네일 파일 ID: {}", currentThumbnailFileId);
            }

            // 새로운 썸네일 파일 정보 생성
            FileVO thumbnailFileVO = new FileVO();
            thumbnailFileVO.setFileName(fundingDTO.getThumbnailFileName());
            thumbnailFileVO.setFilePath(getPath());
            File file = new File("C:/upload/" + getPath() + "/" + fundingDTO.getThumbnailFileName());
            if (file.exists()) {
                thumbnailFileVO.setFileSize(String.valueOf(file.length()));
            } else {
                throw new RuntimeException("새로운 썸네일 파일이 존재하지 않습니다: " + fundingDTO.getThumbnailFileName());
            }

            try {
                thumbnailFileVO.setFileType(Files.probeContentType(file.toPath()));
            } catch (IOException e) {
                log.error("썸네일 파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                thumbnailFileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
            }

            // 파일 정보 데이터베이스에 저장
            fileDAO.save(thumbnailFileVO);

            // 저장된 파일의 ID를 설정
            fundingDTO.setThumbnailFileId(thumbnailFileVO.getId());
            log.info("새로운 썸네일 파일 저장 완료: 썸네일 파일 ID: {}", thumbnailFileVO.getId());
        } else {
            // 새로운 썸네일 파일이 없을 경우, 기존 썸네일 파일 ID 유지
            fundingDTO.setThumbnailFileId(currentThumbnailFileId);
            log.info("새로운 썸네일 파일이 없습니다. 기존 썸네일 유지.");
        }

        // 펀딩 상품 삭제 처리
        List<Long> fundingProductIdsToDelete = fundingDTO.getFundingProductIds();
        if (fundingProductIdsToDelete != null && !fundingProductIdsToDelete.isEmpty()) {
            for (Long productId : fundingProductIdsToDelete) {
                fundingDAO.deleteFundingProductById(productId);
                log.info("펀딩 상품 삭제 완료: 펀딩 상품 ID {}", productId);
            }
        }

        // 펀딩 상품 업데이트 및 삽입 처리
        List<FundingProductVO> updatedProducts = fundingDTO.getFundingProducts();
        if (updatedProducts != null) {
            for (FundingProductVO product : updatedProducts) {
                if (product.getId() != null) {
                    // 기존 펀딩 상품 업데이트
                    fundingDAO.updateFundingProduct(product);
                    log.info("펀딩 상품 업데이트 완료: {}", product);
                } else {
                    // 새로운 펀딩 상품 삽입
                    product.setFundingId(fundingDTO.getId());
                    fundingDAO.saveFundingProduct(product);
                    log.info("새로운 펀딩 상품 삽입 완료: {}", product);
                }
            }
        }

        // 기존 파일 삭제
        if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
            postFileService.deleteFilesByIds(deletedFileIds);
            log.info("삭제된 파일 IDs: {}", deletedFileIds);
        }

        // 새 파일 추가 (썸네일 파일이 아닌 일반 파일들)
        if (fundingDTO.getFileNames() != null && !fundingDTO.getFileNames().isEmpty()) {
            for (String fileName : fundingDTO.getFileNames()) {
                if (fileName != null && !fileName.isEmpty()) {
                    FileVO newFileVO = new FileVO();
                    newFileVO.setFileName(fileName);
                    newFileVO.setFilePath(getPath());
                    File file = new File("C:/upload/" + getPath() + "/" + fileName);
                    if (file.exists()) {
                        newFileVO.setFileSize(String.valueOf(file.length()));
                    } else {
                        throw new RuntimeException("새로운 파일이 존재하지 않습니다: " + fileName);
                    }

                    try {
                        newFileVO.setFileType(Files.probeContentType(file.toPath()));
                    } catch (IOException e) {
                        log.error("파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                        newFileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
                    }

                    // 파일 정보 데이터베이스에 저장
                    fileDAO.save(newFileVO);

                    // 파일과 게시물의 연관 관계 설정
                    PostFileVO postFileVO = new PostFileVO(fundingDTO.getId(), newFileVO.getId());
                    postFileDAO.insertPostFile(postFileVO);
                    log.info("새 파일 추가 완료: 파일 ID: {}", newFileVO.getId());
                }
            }
        }

        // 펀딩 정보 업데이트
        fundingDAO.updateFunding(fundingDTO);
        if (fundingDTO.getPostTitle() != null && fundingDTO.getPostContent() != null) {
            fundingDAO.updatePost(fundingDTO);
        }
        log.info("펀딩 정보 업데이트 완료: 펀딩 ID {}", fundingDTO.getId());
    }


    @Override
    public int findTotalWithSearchAndType(Search search) {
        return fundingDAO.findTotalWithSearchAndType(search);
    }


    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return fundingDAO.findFilesByPostId(postId);
    }

    @Override
    public List<FundingProductVO> findFundingProductsByFundingId(Long fundingId) {
        List<FundingProductVO> fundingProducts = fundingDAO.findFundingProductsByFundingId(fundingId);
        if (fundingProducts != null && !fundingProducts.isEmpty()) {
            log.info("Funding products found for Funding ID {}: {}", fundingId, fundingProducts);
        } else {
            log.warn("No funding products found for Funding ID {}", fundingId);
        }
        return fundingProducts;
    }

    @Override
    public void updateFundingStatusToEnded() {
        fundingDAO.updateFundingStatusToEnded();
    }
    @Override
    public List<FundingDTO> findRelatedFundingByGenre(String genreType, Long fundingId) {
        return fundingDAO.findRelatedFundingByGenre(genreType, fundingId);
    }
    @Override
    public void buyFundingProduct(Long memberId, Long fundingId, Long fundingProductId, int productPrice) {
        log.info("Updating convergePrice for fundingId: {}, fundingProductId: {}, with productPrice: {}", fundingId, fundingProductId, productPrice);

        // 상품 수량 감소 (fundingProductId 사용)
        buyFundingProductDAO.decrementProductAmount(fundingProductId);

        // 펀딩 금액 증가 (fundingId 사용)
        buyFundingProductDAO.updateConvergePrice(fundingId, productPrice);

        // 구매 내역 삽입 (memberId와 fundingProductId 사용)
        buyFundingProductDAO.insertBuyFundingProduct(memberId, fundingProductId);
    }



    @Override
    public void updateFundingProduct(FundingProductVO fundingProductVO) {
        fundingDAO.updateFundingProduct(fundingProductVO);
    }


}
