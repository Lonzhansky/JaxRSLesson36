package service.product;

import dto.product.ProductDtoRequest;
import entity.Product;
import service.BaseService;

import java.util.List;

public interface ProductService extends BaseService<Product, ProductDtoRequest> {

    // Створення нового запису
    Product create(ProductDtoRequest request);
    // Отримання всіх записів
    List<Product> getAll();

    // ---- Path Params ----------------------

    // Отримання запису за id
    Product getById(Long id);
    // Оновлення запису за id
    Product update(Long id, ProductDtoRequest request);
    // Видалення запису за id
    boolean deleteById(Long id);

}
