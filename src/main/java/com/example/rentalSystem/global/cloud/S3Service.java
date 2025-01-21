package com.example.rentalSystem.global.cloud;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final long expirationTimeMillis = 1000 * 60 * 5; // 5분 동안 유효한 URL

    public String generateFacilityS3Key(String fileName) {
        return "facility/" + UUID.randomUUID() + "_" + fileName;
    }

    public String generatePresignedUrl(String fileName) {
        // Presigned URL 요청 설정
        String s3Key = "facility/" + UUID.randomUUID() + "_" + fileName;

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucket, s3Key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));

        // Presigned URL 생성
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
