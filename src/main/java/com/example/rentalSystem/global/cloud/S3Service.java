package com.example.rentalSystem.global.cloud;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.rentalSystem.domain.facility.entity.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final long expirationTimeMillis = 1000 * 60 * 5;

    public String generateFacilityS3Key(String fileName) {
        return "facility/" + UUID.randomUUID() + "_" + fileName;
    }

    public String generateFacilityS3Key(String originalFileName, Long facilityId) {
        return "facilities/" + facilityId + "/images/" + UUID.randomUUID() + "_" + originalFileName;
    }

    public String generatePresignedUrlForPut(String objectKey) {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));

        URL url = amazonS3.generatePresignedUrl(req);
        return url.toString();
    }

    public List<String> generatePresignedUrlsForGet(Facility facility) {
        return facility.getImages().stream()
                .map(this::generatePresignedUrlForGet)
                .collect(Collectors.toList());
    }

    public String generatePresignedUrlForGet(String objectKey) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));
        URL url = amazonS3.generatePresignedUrl(req);
        return url.toString();
    }

    public boolean objectExists(String objectKey) {
        try {
            return amazonS3.doesObjectExist(bucket, objectKey);
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteObjectIfExists(String objectKey) {
        try {
            if (amazonS3.doesObjectExist(bucket, objectKey)) {
                amazonS3.deleteObject(bucket, objectKey);
            }
        } catch (Exception ignore) {
        }
    }
}