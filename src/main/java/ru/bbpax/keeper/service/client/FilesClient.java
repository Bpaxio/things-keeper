package ru.bbpax.keeper.service.client;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Client to call FilesProvider for saving or getting files.
 */
public interface FilesClient {
    /**
     * Call to save file.
     * Can return some errors, todo: describe it.
     *
     *
     * @param id
     * @param imageId
     * @param file file to save.
     * @return link which can be used to get this file again.
     */
    String saveFile(String id, String imageId, MultipartFile file);

    /**
     * Call to update file.
     * Can return some errors, todo: describe it.
     *
     * @param link
     * @param file file to save.
     * @return link which can be used to get this file again.
     */
    String updateFile(String link, MultipartFile file);

    /**
     * Call to find and get file using link.
     * @param link "path" where file is placed.
     * @return file or null if file is missing.
     */
    File getFile(String link);

    /**
     * Call to find and delete file using link.
     * @param link "path" where file is placed.
     */
    void deleteFile(String link);
}
