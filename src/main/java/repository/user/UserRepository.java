package repository.user;


import dto.user.request.UserDtoRequest;
import entity.User;
import repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, UserDtoRequest> {
    // Створення нового запису
    void save(UserDtoRequest request);
    // Отримання всіх записів
    Optional<List<User>> getAll();

    // ---- Path Params ----------------------
    // Отримання запису за id
    Optional<User> getById(Long id);
    // Оновлення запису за id
    void update(Long id, UserDtoRequest request);
    // Видалення запису за id
    boolean deleteById(Long id);

    // ---- Utils ----------------------
    // Отримання останнього запису
    Optional<User> getLastEntity();

    // ---- Query Params ----------------------
    Optional<List<User>> fetchByFirstName(String firstName);
    Optional<List<User>> fetchByLastName(String lastName);
    Optional<List<User>> fetchAllOrderBy(String orderBy);
    Optional<List<User>> fetchByLastNameOrderBy(String lastName, String orderBy);
    Optional<List<User>> fetchBetweenIds(Integer from, Integer to);
    Optional<List<User>> fetchLastNameIn(String lastName1, String lastName2);
}
