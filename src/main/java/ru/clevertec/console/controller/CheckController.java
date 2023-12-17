package ru.clevertec.console.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.console.service.interfaces.CheckService;

import java.io.File;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public FileSystemResource getCheckStrings(@RequestParam("productsAndCard") String[] args) {

        File file = checkService.returnFile(args);

        return new FileSystemResource(file);
    }
}
