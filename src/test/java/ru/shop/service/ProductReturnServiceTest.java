package ru.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shop.exception.BadProductReturnCountException;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Order;
import ru.shop.model.ProductReturn;
import ru.shop.repository.ProductReturnRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProductReturnServiceTest {

    private final ProductReturnRepository repository = Mockito.mock(ProductReturnRepository.class);
    private final ProductReturnService productReturnService = new ProductReturnService(repository);

    @Test
    void shouldAddProductReturn() {
        // given
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), orderId, UUID.randomUUID(), 10, 1000);
        long count = 5;

        // when
        productReturnService.add(order, count);

        // then
        verify(repository).save(any(ProductReturn.class));
    }

    @Test
    void shouldThrowBadProductReturnCountExceptionWhenCountExceedsOrder() {
        // given
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), orderId, UUID.randomUUID(), 10, 1000);
        long count = 15;

        // then
        Assertions.assertThrows(
                BadProductReturnCountException.class,
                () -> productReturnService.add(order, count)
        );
    }

    @Test
    void shouldFindAllProductReturns() {
        // given
        List<ProductReturn> productReturns = List.of(new ProductReturn(), new ProductReturn());
        when(repository.findAll()).thenReturn(productReturns);

        // when
        List<ProductReturn> result = productReturnService.findAll();

        // then
        Assertions.assertEquals(productReturns, result);
        assertThat(result).isEqualTo(productReturns);
    }

    @Test
    void shouldFindProductReturnById() {
        // given
        UUID returnId = UUID.randomUUID();
        ProductReturn productReturn = new ProductReturn(returnId, UUID.randomUUID(), LocalDate.now(), 5);
        when(repository.findById(returnId)).thenReturn(Optional.of(productReturn));

        // when
        ProductReturn result = productReturnService.findById(returnId);

        // then
        Assertions.assertEquals(productReturn, result);
        assertThat(result).isEqualTo(productReturn);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenProductReturnNotFound() {
        // given
        UUID returnId = UUID.randomUUID();
        when(repository.findById(returnId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> productReturnService.findById(returnId)
        );
        assertThatThrownBy(() -> productReturnService.findById(returnId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
