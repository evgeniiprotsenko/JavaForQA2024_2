package ru.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shop.exception.BadProductReturnCountException;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Order;
import ru.shop.model.ProductReturn;
import ru.shop.repository.ProductReturnRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReturnService {

    private final ProductReturnRepository productReturnRepository;

    public void add(Order order, long count) {
        if (count > order.getCount()) {
            throw new BadProductReturnCountException();
        }

        ProductReturn productReturn = new ProductReturn(UUID.randomUUID(), order.getId(), LocalDate.now(), (int) count);

        productReturnRepository.save(productReturn);
    }

    public List<ProductReturn> findAll() {
        return productReturnRepository.findAll();
    }

    public ProductReturn findById(UUID id) {
        return productReturnRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
