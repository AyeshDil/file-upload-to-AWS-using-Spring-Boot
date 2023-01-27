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
    private String bucket; // Assign bucket name to the bucket variable.

//    Constructor based Dependency Injection
    public FileServiceIMPL(AmazonS3 s3, AmazonS3Client amazonS3Client) {
        this.s3 = s3;
        this.amazonS3Client = amazonS3Client;
    }

//    Implementation of createFile()
    @Override
    public HashMap<String, Object> createFile(
            MultipartFile file,
            String directory,
            String user,
            int randomId
    ) throws IOException {

        String finalizedDirectory = user + "/" + directory + "/"; // Set finalized directory name
        String originalFilename = file.getOriginalFilename(); // get original file name
        String renamedFilename = randomId + "-" + file.getOriginalFilename(); // renamed the original file using random value. Ex: 25461-abc.jpg

//        Put object (the file) to the AWS S3 bucket
        PutObjectResult putObjectResult = amazonS3Client.putObject(
                new PutObjectRequest(
                        bucket,
                        finalizedDirectory + renamedFilename,
                        file.getInputStream(),
                        new ObjectMetadata()
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );

//        Create the HashMap
        HashMap<String, Object> hMap = new HashMap<>();

        hMap.put("hash", putObjectResult.getContentMd5());
        hMap.put("resource", amazonS3Client.getResourceUrl(bucket, directory + renamedFilename));
        hMap.put("directory", finalizedDirectory);
        hMap.put("renamedFile", renamedFilename);
        hMap.put("originalFile", originalFilename);

        return hMap; // return the HashMap to the controller
    }

//    Implementation of deleteFile()
    @Override
    public boolean deleteFile(String directory, String fileName) {
//        Check whether the file is existing in the AWS S3 bucket
        if (amazonS3Client.doesObjectExist(bucket, directory + fileName)) {
//            If it is yes, delete object from the AWS S3 bucket
            amazonS3Client.deleteObject(bucket, directory + fileName);
//            Then return True;
            return true;
        }
//        If it is not existing return False;
        return false;
    }

//    Implementation of downloadFile()
    @Override
    public byte[] downloadFile(String directory, String fileName) throws IOException {
//        get the file from AWS
        S3Object s3Object = s3.getObject(bucket, directory + fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        // return byte array to the controller
        return IOUtils.toByteArray(s3ObjectInputStream);
    }

//    Implementation of allFile()
    @Override
    public ObjectListing allFile(){
//        Return list of all object to the controller
        return s3.listObjects(bucket);

    }

}
