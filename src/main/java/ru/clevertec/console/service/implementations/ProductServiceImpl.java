package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.console.dto.ProductDto;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.mapper.MapUtil;
import ru.clevertec.console.repository.ProductsRepository;
import ru.clevertec.console.service.interfaces.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository repository;
    private final MapUtil mapUtil;

    @Override
    public List<ProductDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapUtil::mapProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> findById(long id) {
        return repository.findById(id)
                .map(mapUtil::mapProductToDto);
    }

    @Override
    public Optional<ProductDto> findByName(String name) {
        return repository.findByTitle(name)
                .map(mapUtil::mapProductToDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto dto) {
        ProductDto updatedProduct = null;
        int update = repository.update(dto.getTitle(), dto.getPrice(), dto.isDiscount(), dto.getId());
        if (update == 1) {
            if (repository.findById(dto.getId()).isPresent()) {
                updatedProduct = mapUtil.mapProductToDto(repository.findById(dto.getId()).get());
            }
        }
        return updatedProduct;
    }

    @Override
    @Transactional
    public ProductDto save(ProductDto dto) {
        Product product = mapUtil.mapDtoToProduct(dto);
        return mapUtil.mapProductToDto(repository.save(product));
    }
}
