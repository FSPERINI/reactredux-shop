package com.ecommerce.api.data;

import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.CategoryRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.PromotionRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PromotionRepository promotionRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository,
                           UserRepository userRepository, PasswordEncoder passwordEncoder,
                           PromotionRepository promotionRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            System.out.println("Data already initialized, skipping.");
            return;
        }

        // Categories
        Category electronics = categoryRepository.save(new Category("Electronics", "Electronic devices and gadgets"));
        Category clothing = categoryRepository.save(new Category("Clothing", "Fashion and apparel"));
        Category books = categoryRepository.save(new Category("Books", "Books and literature"));

        // Electronics (5 products)
        Product headphones = productRepository.save(new Product(
                "Wireless Headphones",
                "Premium noise-cancelling wireless headphones with 30h battery life",
                99.99, "https://picsum.photos/seed/headphones/400/400", electronics));

        productRepository.save(new Product(
                "Smartphone Stand",
                "Adjustable aluminum smartphone and tablet stand",
                29.99, "https://picsum.photos/seed/stand/400/400", electronics));

        productRepository.save(new Product(
                "Bluetooth Speaker",
                "Portable waterproof Bluetooth speaker with deep bass",
                49.99, "https://picsum.photos/seed/speaker/400/400", electronics));

        productRepository.save(new Product(
                "USB-C Hub",
                "7-in-1 USB-C hub with HDMI, USB 3.0, and SD card reader",
                39.99, "https://picsum.photos/seed/usbhub/400/400", electronics));

        productRepository.save(new Product(
                "Wireless Mouse",
                "Ergonomic wireless mouse with silent clicks and long battery life",
                24.99, "https://picsum.photos/seed/mouse/400/400", electronics));

        // Clothing (4 products)
        productRepository.save(new Product(
                "Classic T-Shirt",
                "Comfortable cotton t-shirt available in multiple colors",
                19.99, "https://picsum.photos/seed/tshirt/400/400", clothing));

        productRepository.save(new Product(
                "Denim Jacket",
                "Vintage-style denim jacket with modern fit",
                79.99, "https://picsum.photos/seed/jacket/400/400", clothing));

        productRepository.save(new Product(
                "Running Shoes",
                "Lightweight running shoes with responsive cushioning",
                89.99, "https://picsum.photos/seed/shoes/400/400", clothing));

        productRepository.save(new Product(
                "Wool Beanie",
                "Warm knitted wool beanie for cold weather",
                14.99, "https://picsum.photos/seed/beanie/400/400", clothing));

        // Books (4 products)
        productRepository.save(new Product(
                "Clean Code",
                "A Handbook of Agile Software Craftsmanship by Robert C. Martin",
                34.99, "https://picsum.photos/seed/cleancode/400/400", books));

        productRepository.save(new Product(
                "Design Patterns",
                "Elements of Reusable Object-Oriented Software by Gang of Four",
                44.99, "https://picsum.photos/seed/designpatterns/400/400", books));

        productRepository.save(new Product(
                "The Pragmatic Programmer",
                "Your journey to mastery by David Thomas and Andrew Hunt",
                39.99, "https://picsum.photos/seed/pragmatic/400/400", books));

        productRepository.save(new Product(
                "JavaScript: The Good Parts",
                "Unearthing the excellence in JavaScript by Douglas Crockford",
                25.99, "https://picsum.photos/seed/javascript/400/400", books));

        // Demo user
        User demoUser = new User();
        demoUser.setEmail("demo@shop.com");
        demoUser.setPassword(passwordEncoder.encode("password123"));
        demoUser.setFirstName("Demo");
        demoUser.setLastName("User");
        demoUser.setRole(Role.CUSTOMER);
        userRepository.save(demoUser);

        // Promotions
        LocalDateTime now = LocalDateTime.now();

        // Summer Electronics Sale: active immediately, -20% on electronics
        Promotion summerSale = new Promotion(
                "Summer Electronics Sale",
                "20% off all electronics this summer!",
                20, now.minusHours(1), now.plusHours(24), false);
        summerSale.setActive(true);
        summerSale.setCategories(List.of(electronics));
        promotionRepository.save(summerSale);

        // Flash Deal Headphones: starts in 2min, lasts 10min, -40% flash sale
        Promotion flashDeal = new Promotion(
                "Flash Deal Headphones",
                "Limited time: 40% off Wireless Headphones!",
                40, now.plusMinutes(2), now.plusMinutes(12), true);
        flashDeal.setProducts(List.of(headphones));
        promotionRepository.save(flashDeal);

        // Book Week: starts in 5min, -15% on books
        Promotion bookWeek = new Promotion(
                "Book Week",
                "15% off all books this week!",
                15, now.plusMinutes(5), now.plusMinutes(60), false);
        bookWeek.setCategories(List.of(books));
        promotionRepository.save(bookWeek);

        System.out.println("Data initialized: 3 categories, 13 products, 1 demo user, 3 promotions");
    }
}
