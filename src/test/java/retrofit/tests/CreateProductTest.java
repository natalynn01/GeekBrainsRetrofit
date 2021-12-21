package retrofit.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.retrofit.db.mapper.ProductsMapper;
import ru.retrofit.db.model.Products;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.DbMapperFactory;
import ru.retrofit.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Create products test suite")
@Tag("CreateProduct")
public class CreateProductTest {
    static ProductService productService;
    static ProductsMapper productsMapper;
    static Faker faker;
    int productId;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        productsMapper = DbMapperFactory.getDbMapper(ProductsMapper.class);
        faker = new Faker();
    }

    @DisplayName("Test_case_4. Create a new product with Food category test.")
    @Description("That test sending request for Food product creating and getting response body")
    @Tag("CreateFoodProduct")
    @SneakyThrows
    @Test
    void postCreateFoodProductTest(){
        var productTitle = faker.food().ingredient();
        var price = (int) ((Math.random() + 1) * 17);

        productId = createProductAndCheckResponse(
                new Product()
                        .withTitle(productTitle)
                        .withCategoryTitle(CategoryType.FOOD.getTitle())
                        .withPrice(price)
        );

        checkProductDbEntry( 1L ,productTitle, price);
    }

    @DisplayName("Test_case_5. Create a new product with Electronic category test.")
    @Description("That test sending request for Electronic product creating and getting response body")
    @Tag("CreateElectronicProduct")
    @SneakyThrows
    @Test
    void postCreateElectronicProductTest(){
        var productTitle = faker.ancient().hero();
        var price = (int) ((Math.random() + 2) * 18);

        productId = createProductAndCheckResponse(
                new Product()
                        .withTitle(productTitle)
                        .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                        .withPrice(price)
        );

        checkProductDbEntry(2L, productTitle, price);
    }

    @DisplayName("Test_case_6. Create a new product with Furniture category test.")
    @Description("That test sending request for Furniture product creating and getting response body")
    @Tag("CreateFurnitureProduct")
    @SneakyThrows
    @Test
    void postCreateFurnitureProductTest(){
        var productTitle = faker.gameOfThrones().house();
        var price = (int) ((Math.random() + 3) * 19);

        productId = createProductAndCheckResponse(
                new Product()
                        .withTitle(productTitle)
                        .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                        .withPrice(price)
        );

        checkProductDbEntry(3L, productTitle, price);
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
        Allure.addAttachment("Response body", response.body().toString());
        return response.body().getId();
    }

    @Step("Check DB entry with CategoryId={0}, Title={1}, Price={2}")
    void checkProductDbEntry(long productCategoryId ,String productTitle, int price) {
        Products product = productsMapper.selectByPrimaryKey((long) productId);
        Allure.addAttachment("Product", product.toString());
        assertThat(product.getTitle(), equalTo(productTitle));
        assertThat(product.getCategory_id(), equalTo(productCategoryId));
        assertThat(product.getPrice(), equalTo(price));
    }
}
