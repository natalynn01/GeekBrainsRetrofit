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

@DisplayName("Modify products test suite")
@Tag("ModifyProduct")
public class ModifyProductTest {
    static ProductService productService;
    int productId;
    Faker faker = new Faker();

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @DisplayName("Test_case_7. Modify product from Food category to Electronic category test")
    @Description("That test sending request for modify Food product and getting response body")
    @Tag("ModifyFoodProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromFoodToElectronicTest(){
        productId = productService.createProduct(new Product()
                        .withTitle(faker.food().ingredient())
                        .withCategoryTitle(CategoryType.FOOD.getTitle())
                        .withPrice((int) ((Math.random() + 2) * 17)))
                .execute().body().getId();
        modifyProductAndCheckResponse(new Product()
                .withId(productId)
                .withTitle(faker.company().name())
                .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                .withPrice((int) (Math.random() + productId)));
    }

    @DisplayName("Test_case_8. Modify product from Electronic category to Furniture category test")
    @Description("That test sending request for modify Electronic product and getting response body")
    @Tag("ModifyElectronicProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromElectronicToFurniture(){
        productId = productService.createProduct(new Product()
                        .withTitle(faker.ancient().hero())
                        .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                        .withPrice((int) ((Math.random() + 3) * 18)))
                .execute().body().getId();
        modifyProductAndCheckResponse(new Product()
                .withId(productId)
                .withTitle(faker.gameOfThrones().city())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                .withPrice((int) ((Math.random() + productId))));
    }

    @DisplayName("Test_case_9. Modify product from Furniture category to Food category test")
    @Description("That test sending request for modify Furniture product and getting response body")
    @Tag("ModifyFurnitureProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromFurnitureToFood(){
        productId = productService.createProduct(new Product()
                        .withTitle(faker.harryPotter().character())
                        .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                        .withPrice((int) ((Math.random() + 4) * 19)))
                .execute().body().getId();
        modifyProductAndCheckResponse(new Product()
                .withId(productId)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) ((Math.random() + productId))));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        assertThat(productService.deleteProduct(productId).execute().isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Step("Modify product {0} and check ")
    void modifyProductAndCheckResponse(Product modifiedProduct){
        Response<Product> response = productService.modifyProduct(modifiedProduct).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(modifiedProduct.getId()));
        assertThat(response.body().getTitle(), equalTo(modifiedProduct.getTitle()));
        assertThat(response.body().getCategoryTitle(), equalTo(modifiedProduct.getCategoryTitle()));
        assertThat(response.body().getPrice(), equalTo(modifiedProduct.getPrice()));
    }
}
