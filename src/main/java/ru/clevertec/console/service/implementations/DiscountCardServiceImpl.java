package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.repository.DiscountCardRepository;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository repository;

    @Override
    public List <DiscountCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DiscountCard> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<DiscountCard> findByNumber(String number) {
        return Optional.ofNullable(repository.findByNumber(number));
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(DiscountCard entity) {
        repository.update(entity.getNumber(), entity.getId());
    }

    @Override
    public DiscountCard save(DiscountCard entity) {
        return repository.save(entity);
    }
}
