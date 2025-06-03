package com.dbproject.api.File;

public interface FileService {

    String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception;
    void deleteFile(String filePath) throws Exception;
}
