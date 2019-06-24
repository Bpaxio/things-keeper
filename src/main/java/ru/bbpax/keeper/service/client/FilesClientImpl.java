package ru.bbpax.keeper.service.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.bbpax.keeper.service.client.exception.SaveFileException;
import ru.bbpax.keeper.service.exception.FileServiceIsNotAvailable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static ru.bbpax.keeper.util.Helper.getFileExtention;

@Slf4j
@Component
public class FilesClientImpl implements FilesClient {
    private final String url;
    private final String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public FilesClientImpl(@Value("${filer.url}") String url, @Value("${filer.base-url}")String baseUrl) {
        this.url = url;
        this.baseUrl = baseUrl;
    }

    @Override
    public String saveFile(String noteId, String imageId, MultipartFile file) {
        String link = this.url + noteId + "/" + imageId;
        return exchangeFile(link, HttpMethod.POST, file);
    }

    @Override
    public String updateFile(String link, MultipartFile file) {
        return exchangeFile(link, HttpMethod.PUT, file);
    }

    @Override
    @SneakyThrows
    public File getFile(String link) {
        // TODO: 2019-05-14 Broken
        log.info("get file by url: {}", link);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.IMAGE_JPEG_VALUE);
        HttpEntity<Object> request =
                new HttpEntity<>(httpHeaders);
        ResponseEntity<MultiValueMap<String, Object>> exchange = restTemplate
                .exchange(link, HttpMethod.GET, request, ParameterizedTypeReference.forType(LinkedMultiValueMap.class));
        log.info("result: {} - {}", exchange.getStatusCode(), exchange.getBody());
        return Paths.get(getClass().getClassLoader().getResource("Bug.jpg").toURI()).toFile();
    }

    @Override
    public void deleteFile(String link) {
        log.info("delete file by url: {}", link);
        restTemplate.delete(link);
        log.info("file, located in {} was successfully deleted", link);
    }

    private String exchangeFile(String target, HttpMethod method,  MultipartFile file) {
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(createFileBody(file), setUpHeaders(file));

        try {
            ResponseEntity<ImageResponse> exchange = restTemplate
                    .exchange(target, method, request, ImageResponse.class);

        log.info("result: {} - {}", exchange.getStatusCode(), exchange.getBody());
        if (exchange.getBody() == null) throw new SaveFileException(exchange.getStatusCode(), exchange.getStatusCodeValue());
        return this.baseUrl + exchange.getBody().getPath();
        } catch (HttpClientErrorException e) {
            throw new FileServiceIsNotAvailable(e);
        }
    }

    private HttpHeaders setUpHeaders(MultipartFile file) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file");
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));
        return httpHeaders;
    }

    private MultiValueMap<String, Object> createFileBody(MultipartFile file) {
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        try {
            String extension = getFileExtention(file.getOriginalFilename());
            File tempFile = File.createTempFile("temp", extension);
            file.transferTo(tempFile);
            map.add("file", new FileSystemResource(tempFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
