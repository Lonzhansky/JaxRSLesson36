package repository.product;

import config.HibernateConfig;
import dto.product.ProductDtoRequest;
import entity.Product;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductRepositoryImpl implements ProductRepository {

    private static final Logger LOGGER =
            Logger.getLogger(ProductRepository.class.getName());

    @Override
    public void save(ProductDtoRequest request) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Отримання користувача
            User user = session.get(User.class, request.user_id());
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + request.user_id() + " not found.");
            }

            // Створення сутності Product
            Product product = new Product();
            product.setUser(user);
            product.setProductName(request.productName());
            product.setMeasure(request.measure());
            product.setQuota(request.quota());
            product.setPrice(request.price());

            // Збереження сутності
            session.save(product);

            // Підтвердження транзакції
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, "Error saving product: " + e.getMessage(), e);
            throw e; // Прокидаємо виняток вище
        }
    }

    @Override
    public Optional<List<Product>> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<Product> list =
                    session.createQuery("FROM Product", Product.class).list();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з колецією даних
            return Optional.of(list);
        } catch (Exception e) {
            // Якщо помилка повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    // ---- Path Params ----------------------

    @Override
    public Optional<Product> getById(Long id) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<Product> query =
                    session.createQuery("FROM Product WHERE id = :id", Product.class);
            query.setParameter("id", id);
            query.setMaxResults(1);
            Product product = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з об'єктом
            return Optional.ofNullable(product);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            // Якщо помилка або такого об'єкту немає в БД,
            // повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    @Override
    public void update(Long id, ProductDtoRequest request) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Початок транзакції
            transaction = session.beginTransaction();

            // Пошук існуючого продукту
            Product product = session.get(Product.class, id);
            if (product == null) {
                throw new IllegalArgumentException("Product with ID " + id + " not found.");
            }

            // Оновлення полів продукту
            product.setProductName(request.productName());
            product.setMeasure(request.measure());
            product.setQuota(request.quota());
            product.setPrice(request.price());

            // Hibernate автоматично відстежує зміни, commit зберігає їх
            session.update(product);

            // Підтвердження транзакції
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, "Error updating product: " + e.getMessage(), e);
            throw e; // Прокидаємо виняток вище
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            String hql = "DELETE FROM Product WHERE id = :id";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
            // Транзакція виконується
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    // ---- Utils ----------------------

    public boolean isIdExists(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Перевірка наявності об'єкту за певним id
            Product product = new Product();
            product.setId(id);
            product = session.get(Product.class, product.getId());

            if (product != null) {
                Query<Product> query =
                        session.createQuery("FROM Product", Product.class);
                query.setMaxResults(1);
                query.getResultList();
            }
            return product != null;
        }
    }

    public Optional<Product> getLastEntity() {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<Product> query =
                    session.createQuery("FROM Product ORDER BY id DESC", Product.class);
            query.setMaxResults(1);
            Product product = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(product);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
