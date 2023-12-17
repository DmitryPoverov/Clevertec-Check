package ru.clevertec.console.service.interfaces;

import java.io.File;
import java.util.List;

public interface CheckService {

    List<String> handleArrayAndGetStrungList(String[] args);

    File returnFile(String[] args);
}
