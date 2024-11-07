package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.pagination.Pagination;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Primary
@Slf4j
public class FundingServiceImpl implements FundingService{


    private final FundingDAO fundingDAO;
    private final PostDAO postDAO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입

    @Override
    public void write(FundingDTO fundingDTO, MultipartFile[] fundingFiles, MultipartFile thumbnailFile) {
        // Post 테이블에 저장할 데이터 설정
        PostVO postVO = new PostVO();
        postVO.setPostTitle(fundingDTO.getPostTitle());
        postVO.setPostContent(fundingDTO.getPostContent());
        postVO.setPostType(fundingDTO.getPostType());
        postVO.setMemberProfileId(fundingDTO.getMemberProfileId());

        // post 데이터 저장
        postDAO.save(postVO);
        Long postId = postVO.getId();

        // Funding 테이블에 저장할 데이터 설정
        fundingDTO.setId(postId);
        fundingDAO.save(fundingDTO);
        log.info("Saving FundingDTO: {}", fundingDTO);
        // 펀딩 상품 삽입 처리
        log.info("Saving FundingDTO: {}", fundingDTO);
        List<FundingProductVO> fundingProducts = fundingDTO.getFundingProducts();
        if (fundingProducts != null && !fundingProducts.isEmpty()) {
            for (FundingProductVO product : fundingProducts) {
                product.setFundingId(postId); // 펀딩 ID 설정
                fundingDAO.saveFundingProduct(product);
                log.info("Saving Product: {}", product);
            }
        } else {
            log.error("No products found in FundingDTO");
        }


        // 파일 저장 처리 (펀딩 파일)
        if (fundingFiles != null && fundingFiles.length > 0) {
            for (MultipartFile file : fundingFiles) {
                if (!file.isEmpty()) {
                    postFileService.saveFile(file, fundingDTO.getId());
                }
            }
        }

        // 썸네일 파일 처리
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            FileVO thumbnailFileVO = postFileService.saveFile(thumbnailFile, fundingDTO.getId());
            fundingDTO.setThumbnailFileId(thumbnailFileVO.getId()); // FileVO의 ID를 사용하여 설정
            log.info("Saving ThumbnailFile: {}", thumbnailFileVO);
            fundingDAO.updateFunding(fundingDTO); // 썸네일 ID 업데이트
        }
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
}
