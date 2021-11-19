package retrofit.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("Delete product test suite")
@Tag("DeleteProduct")
public class DeleteProductTest {
    static ProductService productService;
    static Faker faker = new Faker();
    static Product product;
    static int productId;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        product = new Product()
                .withTitle(faker.gameOfThrones().house())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                .withPrice((int) ((Math.random() + 3) * 19));
        productId = productService.createProduct(product).execute().body().getId();
    }

    @DisplayName("Test_case_12. Delete product on Id test.")
    @Description("That test sending request for deleting product with certain id")
    @Tag("DeleteProductTest")
    @SneakyThrows
    @Test
    void deleteProductOnIdPositiveTest() {
        assertThat(productService.deleteProduct(productId).execute().isSuccessful(), CoreMatchers.is(true));
    }
}
