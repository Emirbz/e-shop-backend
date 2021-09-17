package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Picture;
import com.shop.myShop.Services.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(path = "image")
public class PictureController {

    @Autowired
    FilesStorageService storageService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        HashMap<String, String> response = new HashMap<>();
        try {
            String fileName = storageService.save(file);
//            Resource uploadedFile = storageService.load(fileName);
            response.put("fileName", fileName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            response.put("error", message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}