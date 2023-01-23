package com.myproject.fileupload.fileupload.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import com.myproject.fileupload.fileupload.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("api/v1/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = {"/create"}, params = { "directory", "user"})
    public HashMap<String, Object> createFile(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "directory") String directory,
            @RequestParam(value = "user") String user
    ) throws IOException {
        return fileService.createFile(file, directory, user, new Random().nextInt(1001));
    }

    @DeleteMapping(path = "/remove", params = {"directory", "fileName"})
    public boolean deleteFile(
            @RequestParam(value = "directory") String directory,
            @RequestParam(value = "fileName") String fileName
    ){
        return fileService.deleteFile(directory,fileName);
    }

    @GetMapping(path = {"/download"}, params = {"directory", "fileName"})
    public byte[] downloadFile(
            @RequestParam(value = "directory") String directory,
            @RequestParam(value = "fileName") String fileName
    ) throws IOException {

        return fileService.downloadFile(directory,fileName);
    }

    @GetMapping(path = {"/list"})
    public ObjectListing allFiles() throws IOException {
        return fileService.allFile();
    }
}
