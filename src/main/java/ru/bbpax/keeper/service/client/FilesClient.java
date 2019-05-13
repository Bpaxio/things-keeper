package ru.bbpax.keeper.service.client;

/**
 * Client to call FilesProvider for saving or getting files.
 */
public interface FilesClient {
    /**
     * Call to save file.
     * Can return some errors, todo: describe it.
     * @param file file to save.
     * @return query which can be used to get this file again.
     */
    String saveFile(Object file);

    /**
     * Call to find and get file using query.
     * @param query "path" where file is placed.
     * @return file or null if file is missing.
     */
    Object getFile(String query);
}
