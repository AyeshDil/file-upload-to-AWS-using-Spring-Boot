package com.myproject.fileupload.fileupload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface FileService {

    public HashMap<String,Object> createFile(MultipartFile file) throws IOException;

}
