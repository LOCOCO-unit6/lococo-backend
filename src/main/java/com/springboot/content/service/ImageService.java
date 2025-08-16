package com.springboot.content.service;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    // AmazonS3 멤버변수 선언
    private final AmazonS3 amazonS3;

    // application.yml 내의 버킷이름 가져오기
    @Value("${cloud.aws.s3.bucketname}")
    private String bucketName;

    // 이미지 이름 중복 방지를 위해 UUID 사용
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + originName.substring(originName.lastIndexOf("."));
    }

    // 이미지를 S3에 업로드 후, URL 반환
    public String uploadImageToS3(MultipartFile file) {
        String originName = file.getOriginalFilename();
        String changedName = changedImageName(originName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, changedName, file.getInputStream(), metadata));
                    //.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 업로드 중 오류 발생", e);
        }
        return amazonS3.getUrl(bucketName, changedName).toString();
    }

    public void deleteImageFromS3(String imageUrl) {
        // 이미지 URL에서 파일 이름(S3 객체 키) 추출
        String key = extractKeyFromUrl(imageUrl);

        // S3에 삭제 요청
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            // S3에서 파일이 이미 삭제되었거나 다른 문제가 발생해도
            // 애플리케이션 흐름이 중단되지 않도록 예외를 로깅만 합니다.
            System.err.println("S3 이미지 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // URL에서 S3 객체 키를 추출하는 헬퍼 메서드
    private String extractKeyFromUrl(String imageUrl) {
        // S3 URL에서 버킷 이름 부분 이후의 경로를 추출
        String urlWithoutBucket = imageUrl.substring(imageUrl.indexOf(bucketName) + bucketName.length() + 1);
        return urlWithoutBucket.substring(urlWithoutBucket.indexOf('/') + 1);
    }
}
