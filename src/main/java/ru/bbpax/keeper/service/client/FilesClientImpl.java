package ru.bbpax.keeper.service.client;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.bbpax.keeper.service.client.exception.SaveFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static ru.bbpax.keeper.util.Helper.getFileExtention;

@Slf4j
@Component
public class FilesClientImpl implements FilesClient {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${filer.host}")
    private String host;

    @Value("${filer.port}")
    private String port;

    @Override
    public String saveFile(String id, String imageId, MultipartFile file) {
        String url = host + ":" + port + "/files/";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file");
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(createFileBody(file), httpHeaders);
        ResponseEntity<ImageResponse> exchange = restTemplate
                .exchange(url, HttpMethod.POST, request, ImageResponse.class);
        log.info("result: {} - {}", exchange.getStatusCode(), exchange.getBody());
        if (exchange.getBody() == null) throw new SaveFileException(exchange.getStatusCode(), exchange.getStatusCodeValue());
        return url + exchange.getBody().getPath();
    }

    @Override
    public String updateFile(String link, MultipartFile file) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file");
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(createFileBody(file), httpHeaders);
        ResponseEntity<ImageResponse> exchange = restTemplate
                .exchange(link, HttpMethod.PUT, request, ImageResponse.class);// TODO: 2019-05-14 java.net.ConnectException
        log.info("result: {} - {}", exchange.getStatusCode(), exchange.getBody());
        if (exchange.getBody() == null) throw new SaveFileException(exchange.getStatusCode(), exchange.getStatusCodeValue());
        String url = host + ":" + port + "/files/";
        return url + exchange.getBody().getPath();
    }

    @Override
    // TODO: 2019-05-14 Broken
    public File getFile(String link) {
        log.info("get file by url: {}", link);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        HttpEntity<Object> request =
                new HttpEntity<>(httpHeaders);
        ResponseEntity<MultiValueMap<String, Object>> exchange = restTemplate
                .exchange(link, HttpMethod.GET, request, ParameterizedTypeReference.forType(LinkedMultiValueMap.class));
        log.info("result: {} - {}", exchange.getStatusCode(), exchange.getBody());
        return Paths.get("/Users/Bpaxio/Documents/workspace/otus/things-keeper/src/main/resources/Bug.jpg").toFile();
    }

    @Override
    public void deleteFile(String link) {
        restTemplate.delete(link);
        log.info("file, located in {} was successfully deleted", link);
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
