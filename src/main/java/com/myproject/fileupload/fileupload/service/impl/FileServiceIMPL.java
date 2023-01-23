package com.myproject.fileupload.fileupload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
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

    @Value("${bucketName}")
    private String bucket;

    public FileServiceIMPL(AmazonS3 s3, AmazonS3Client amazonS3Client) {
        this.s3 = s3;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public HashMap<String, Object> createFile(
            MultipartFile file,
            String directory,
            String user,
            int randomId
    ) throws IOException {

        String finalizedDirectory = user +"/"+ directory +"/";
        String originalFilename = file.getOriginalFilename();
        String renamedFilename = randomId +"-"+ file.getOriginalFilename(); //25461abc.jpg

        PutObjectResult putObjectResult = amazonS3Client.putObject(
                new PutObjectRequest(
                        bucket,
                        finalizedDirectory + renamedFilename,
                        file.getInputStream(),
                        new ObjectMetadata()
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );

        HashMap<String, Object> hMap = new HashMap<>();

        hMap.put("hash", putObjectResult.getContentMd5());
        hMap.put("resource", amazonS3Client.getResourceUrl(bucket, directory + renamedFilename));
        hMap.put("directory", finalizedDirectory);
        hMap.put("renamedFile", renamedFilename);
        hMap.put("originalFile", originalFilename);

        return hMap;
    }

    @Override
    public boolean deleteFile(String directory, String fileName){
        amazonS3Client.deleteObject(bucket, directory + fileName);
        return true;
    }

    @Override
    public byte[] downloadFile(String directory, String fileName) throws IOException {
        S3Object s3Object = s3.getObject(bucket, directory+fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(s3ObjectInputStream);
    }

    @Override
    public ObjectListing allFile() throws IOException {
        return s3.listObjects(bucket);

    }

}
