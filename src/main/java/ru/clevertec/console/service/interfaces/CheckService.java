package ru.clevertec.console.service.interfaces;

import java.util.List;

public interface CheckService<K, T> {

    List<K> handleArrayAndGetStrungList(K[] args);

    T getGoodsAndCard(K[] args);

    List<K> createList(T check);
}
