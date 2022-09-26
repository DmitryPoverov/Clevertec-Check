package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.entities.Check;

import java.util.List;

public interface CheckService {

    List<String> handleArrayAndGetStrungList(String[] args);

    Check getGoodsAndCard(String[] args);

    List<String> createList(Check check);
}
