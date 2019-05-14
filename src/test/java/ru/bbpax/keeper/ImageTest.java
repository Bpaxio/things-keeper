package ru.bbpax.keeper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

@Slf4j
public class ImageTest {
    @Test
    void test() {
        File img = Paths.get("/Users/Bpaxio/Documents/workspace/otus/things-keeper/src/test/resources/Bug.jpg").toFile();
        log.info("\nfile: {\n    " +
                "exists:{},\n    " +
                "name: {}\n" +
                "}", img.exists(), img.getName());
    }
}
