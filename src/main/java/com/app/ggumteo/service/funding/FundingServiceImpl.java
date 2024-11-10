package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.funding.FundingVO;
import com.app.ggumteo.domain.post.PostVO;
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
        return fundingDAO.findAllFundingList(search, pagination);
    }

    @Override
    public int findTotalWithSearchAndType(Search search) {
        return fundingDAO.findTotalWithSearchAndType(search);
    }


@Override
public List<FundingDTO> findFundingById(Long id) {
    return fundingDAO.findByFundingId(id);
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

}
