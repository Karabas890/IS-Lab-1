package services;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.InputStream;

@Stateless
public class MinioService {
    private static final String BUCKET_NAME = "import-files";
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при инициализации MinIO: " + e.getMessage());
        }
    }

    public String uploadFile(String fileName, InputStream fileStream, long size) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(fileName)
                        .stream(fileStream, size, -1)
                        .contentType("text/csv")
                        .build()
        );
        return fileName;
    }

    public InputStream downloadFile(String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(fileName)
                        .build()
        );
    }
}
