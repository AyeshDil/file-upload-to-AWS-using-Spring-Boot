package com.myproject.fileupload.fileupload.service;

import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

// FileService interface
public interface FileService {

    HashMap<String,Object> createFile(MultipartFile file, String directory, String user, int randomId) throws IOException;
    boolean deleteFile(String directory, String fileName);
    byte[] downloadFile(String directory, String fileName) throws IOException;
    ObjectListing allFile();

}
