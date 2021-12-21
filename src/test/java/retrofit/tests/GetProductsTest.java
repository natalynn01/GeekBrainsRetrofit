package retrofit.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.retrofit.db.mapper.CategoriesMapper;
import ru.retrofit.db.mapper.ProductsMapper;
import ru.retrofit.db.model.CategoriesExample;
import ru.retrofit.db.model.ProductsExample;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.DbMapperFactory;
import ru.retrofit.utils.RetrofitUtils;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Get products test suite")
@Tag("GetProducts")
public class GetProductsTest {
    static ProductService productService;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static CategoriesExample categoriesExample;
    static Faker faker;
    static Product product;
    static int productId;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        faker = new Faker();
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        product = new Product()
                .withTitle(faker.gameOfThrones().house())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle())
                .withPrice((int) ((Math.random() + 3) * 19));
        productId = productService.createProduct(product).execute().body().getId();
        productsMapper = DbMapperFactory.getDbMapper(ProductsMapper.class);
        categoriesMapper = DbMapperFactory.getDbMapper(CategoriesMapper.class);
        categoriesExample = new CategoriesExample();
    }

    @DisplayName("Test_case_10. Get all products test.")
    @Description("That test sending request for getting response body with all products")
    @Tag("GetAllProductsTest")
    @SneakyThrows
    @Test
    void getAllProductsPositiveTest() {
        var products =  getAllProductsAndCheckResponse();
        checkAllProductsInDb(products);
    }

    @DisplayName("Test_case_11. Get product on Id test.")
    @Description("That test sending request for getting response body with certain product")
    @Tag("GetProductTest")
    @SneakyThrows
    @Test
    void getProductOnIdPositiveTest() {
        var product = getProductAndCheckResponse(productId);
        checkProductInDb(product);
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        assertThat(productService.deleteProduct(productId).execute().isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Step("Get list of all products")
    Product[] getAllProductsAndCheckResponse() {
        Response<Product[]> response = productService.getAllProducts().execute();
        var products = response.body();
        Arrays.stream(products).forEach(product -> {
            assertThat(product.getId(), notNullValue());
            if (product.getTitle() != null) {
                assertThat(product.getTitle(), anyOf(instanceOf(String.class)));
            }
            assertThat(product.getPrice(), notNullValue());
            assertThat(product.getCategoryTitle(), notNullValue());
            Allure.addAttachment("Product with Id=" + product.getId(), product.toString());
        });
        return products;
    }

    @SneakyThrows
    @Step("Get product with id={0}")
    Product getProductAndCheckResponse(int productId) {
        Response<Product> response = productService.getProduct(productId).execute();
        var product = response.body();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(product.getTitle(), equalTo(product.getTitle()));
        assertThat(product.getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(product.getPrice(), equalTo(product.getPrice()));
        Allure.addAttachment("Response with productId=" + productId, product.toString());
        return product;
    }

    @Step("Check all products in DB")
    void checkAllProductsInDb(Product[] products) {
        Arrays.stream(products).forEach(this::checkProductInDb);
    }

    @Step("Check product in DB")
    void checkProductInDb(Product product) {
        var productDbEntry =  productsMapper.selectByPrimaryKey((long) product.getId());
        categoriesExample.createCriteria().andTitleEqualTo(product.getCategoryTitle());
        var productCategoryId = categoriesMapper.selectByExample(categoriesExample).get(0).getId();
        var dbEntryTitle = productDbEntry.getTitle();
        var title = product.getTitle();
        if (dbEntryTitle != null && title != null) {
            dbEntryTitle = dbEntryTitle.trim();
            title = title.trim();
        }
        assertThat(productDbEntry.getId(), equalTo((long) product.getId()));
        assertThat(dbEntryTitle, equalTo(title));
        assertThat(productDbEntry.getPrice(), equalTo(product.getPrice()));
        assertThat(productDbEntry.getCategory_id(), equalTo((long) productCategoryId));
        Allure.addAttachment("DB Entry for product with Id=" + product.getId(), productDbEntry.toString());
    }
}
