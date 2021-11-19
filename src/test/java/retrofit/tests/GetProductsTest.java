package retrofit.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.RetrofitUtils;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Get products test suite")
@Tag("GetProducts")
public class GetProductsTest {
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

    @DisplayName("Test_case_10. Get all products test.")
    @Description("That test sending request for getting response body with all products")
    @Tag("GetAllProductsTest")
    @SneakyThrows
    @Test
    void getAllProductsPositiveTest() {
        getAllProductsAndCheckResponse();
    }

    @DisplayName("Test_case_11. Get product on Id test.")
    @Description("That test sending request for getting response body with certain product")
    @Tag("GetProductTest")
    @SneakyThrows
    @Test
    void getProductOnIdPositiveTest() {
        getProductAndCheckResponse(productId);
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        assertThat(productService.deleteProduct(productId).execute().isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Step("Get list of all products")
    private static void getAllProductsAndCheckResponse() {
        Response<Product[]> response = productService.getAllProducts().execute();
        Arrays.stream(response.body()).forEach(product -> {
            assertThat(product.getId(), notNullValue());
            if (product.getTitle() != null) {
                assertThat(product.getTitle(), anyOf(instanceOf(String.class)));
            }
            assertThat(product.getPrice(), notNullValue());
            assertThat(product.getCategoryTitle(), notNullValue());
        });
    }

    @SneakyThrows
    @Step("Get product with id={0}")
    private static void getProductAndCheckResponse(int productId) {
        Response<Product> response = productService.getProduct(productId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
    }
}
