package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.console.dto.DiscountCardDto;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.mapper.MapUtil;
import ru.clevertec.console.repository.DiscountCardRepository;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository repository;
    private final MapUtil mapUtil;

    @Override
    public List <DiscountCardDto> findAll() {
        return repository.findAll().stream()
                .map(mapUtil::mapDiscountCardToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DiscountCardDto> findById(long id) {
        return repository.findById(id)
                .map(mapUtil::mapDiscountCardToDto);
    }

    @Override
    public Optional<DiscountCardDto> findByNumber(String number) {
        return repository.findByNumber(number)
                .map(mapUtil::mapDiscountCardToDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public int update(DiscountCardDto entity) {
        return repository.update(entity.getNumber(), entity.getId());
    }

    @Override
    @Transactional
    public DiscountCardDto save(DiscountCardDto dto) {
        DiscountCard discountCard = mapUtil.mapDtoToDiscountCard(dto);
        return mapUtil.mapDiscountCardToDto(repository.save(discountCard));
    }
}
