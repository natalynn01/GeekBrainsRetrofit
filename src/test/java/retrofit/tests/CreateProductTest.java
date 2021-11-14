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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Create products test suite")
@Tag("CreateProduct")
public class CreateProductTest {
    static ProductService productService;
    static Faker faker = new Faker();
    int productId;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @DisplayName("Test_case_4. Create a new product with Food category test.")
    @Description("That test sending request for Food product creating and getting response body")
    @Tag("CreateFoodProduct")
    @SneakyThrows
    @Test
    void postCreateFoodProductTest(){
        productId = createProductAndCheckResponse(new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) ((Math.random() + 1) * 17)));
    }

    @DisplayName("Test_case_5. Create a new product with Electronic category test.")
    @Description("That test sending request for Electronic product creating and getting response body")
    @Tag("CreateElectronicProduct")
    @SneakyThrows
    @Test
    void postCreateElectronicProductTest(){
        productId = createProductAndCheckResponse(new Product()
                .withTitle(faker.ancient().hero())
                .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                .withPrice((int) ((Math.random() + 2) * 18)));
    }

    @DisplayName("Test_case_6. Create a new product with Furniture category test.")
    @Description("That test sending request for Furniture product creating and getting response body")
    @Tag("CreateFurnitureProduct")
    @SneakyThrows
    @Test
    void postCreateFurnitureProductTest(){
        productId = createProductAndCheckResponse(new Product()
                .withTitle(faker.gameOfThrones().house())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                .withPrice((int) ((Math.random() + 3) * 19)));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        assertThat(productService.deleteProduct(productId).execute().isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Step("Create new product {0}")
    int createProductAndCheckResponse(Product product){
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        return response.body().getId();
    }
}
