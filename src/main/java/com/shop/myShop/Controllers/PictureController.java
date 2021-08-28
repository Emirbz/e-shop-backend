package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Picture;
import com.shop.myShop.Services.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(path = "image")
public class PictureController {

    @Autowired
    FilesStorageService storageService;


    @PostMapping("/upload/{productId}")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long productId) {
        String message = "";
        try {
            String fileName = storageService.save(file);
            Resource uploadedFile = storageService.load(fileName);
            return ResponseEntity.status(HttpStatus.OK).body(storageService.saveToDB(uploadedFile, productId));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<Picture>> getListFiles() {
        List<Picture> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(PictureController.class, "getFile", path.getFileName().toString()).build().toString();

            return new Picture(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}