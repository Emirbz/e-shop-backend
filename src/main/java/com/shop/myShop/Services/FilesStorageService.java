package com.shop.myShop.Services;


import com.shop.myShop.Entities.Picture;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void init();

    public String save(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    public Picture saveToDB(Resource uploadedFile, Long id) throws IOException;
}
