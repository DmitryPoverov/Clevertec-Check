package ru.clevertec.console.service.interfaces;

import org.springframework.core.io.FileSystemResource;
import ru.clevertec.console.entities.Check;

import java.io.File;
import java.util.List;

public interface CheckService {

    List<String> handleArrayAndGetStrungList(String[] args);

    File returnFile(String[] args);
}
