package com.myproject.fileupload.fileupload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.myproject.fileupload.fileupload.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Service
public class FileServiceIMPL implements FileService {

    private final AmazonS3 s3;
    private final AmazonS3Client amazonS3Client;

    @Value("bucketName")
    private String bucket;

    public FileServiceIMPL(AmazonS3 s3, AmazonS3Client amazonS3Client) {
        this.s3 = s3;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public HashMap<String, Object> createFile(MultipartFile file) throws IOException {
        String directory= "sample/resource";

        String originalFilename = file.getOriginalFilename();
        PutObjectResult putObjectResult = amazonS3Client.putObject(
                new PutObjectRequest(
                        bucket,
                        directory+"/"+ originalFilename,
                        file.getInputStream(),
                        new ObjectMetadata()
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );

        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("hash", putObjectResult.getContentMd5());
        hMap.put("resource", amazonS3Client.getResourceUrl(bucket, directory+"/"+originalFilename));

        return hMap;
    }

}
