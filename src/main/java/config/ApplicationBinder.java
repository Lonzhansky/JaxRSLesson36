package config;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import repository.product.ProductRepository;
import repository.product.ProductRepositoryImpl;
import repository.user.UserRepository;
import repository.user.UserRepositoryImpl;
import service.product.ProductService;
import service.product.ProductServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

public class ApplicationBinder implements Feature {
    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(UserRepositoryImpl.class).to(UserRepository.class);
                bind(UserServiceImpl.class).to(UserService.class);
                bind(ProductRepositoryImpl.class).to(ProductRepository.class);
                bind(ProductServiceImpl.class).to(ProductService.class);
                bind(JacksonJsonProvider.class);
            }
        });
        return true;
    }
}
