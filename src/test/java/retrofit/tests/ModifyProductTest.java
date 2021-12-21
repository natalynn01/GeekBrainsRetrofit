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
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.DbMapperFactory;
import ru.retrofit.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Modify products test suite")
@Tag("ModifyProduct")
public class ModifyProductTest {
    static ProductService productService;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static CategoriesExample categoriesExample;
    static int productId;
    static Faker faker;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        faker = new Faker();
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        productsMapper = DbMapperFactory.getDbMapper(ProductsMapper.class);
        categoriesMapper = DbMapperFactory.getDbMapper(CategoriesMapper.class);
        categoriesExample = new CategoriesExample();
    }

    @DisplayName("Test_case_7. Modify product from Food category to Electronic category test")
    @Description("That test sending request for modify Food product and getting response body")
    @Tag("ModifyFoodProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromFoodToElectronicTest(){
        productId = createProduct(
                faker.food().ingredient(),
                CategoryType.FOOD.getTitle(),
                (int) ((Math.random() + 2) * 17)).getId();
        var modifiedProduct = modifyProductAndCheckResponse(
                productId,
                faker.company().name(),
                CategoryType.ELECTRONIC.getTitle(),
                (int) ((Math.random() + 12) * 17));
        checkProductDbEntry(modifiedProduct);
    }

    @DisplayName("Test_case_8. Modify product from Electronic category to Furniture category test")
    @Description("That test sending request for modify Electronic product and getting response body")
    @Tag("ModifyElectronicProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromElectronicToFurniture(){
        productId = createProduct(
                faker.ancient().hero(),
                CategoryType.ELECTRONIC.getTitle(),
                (int) ((Math.random() + 3) * 18)).getId();
        var modifiedProduct = modifyProductAndCheckResponse(
                productId,
                faker.gameOfThrones().city(),
                CategoryType.FURNITURE.getTitle(),
                (int) ((Math.random() + 13) * 18));
        checkProductDbEntry(modifiedProduct);
    }

    @DisplayName("Test_case_9. Modify product from Furniture category to Food category test")
    @Description("That test sending request for modify Furniture product and getting response body")
    @Tag("ModifyFurnitureProduct")
    @SneakyThrows
    @Test
    void putModifiedProductFromFurnitureToFood(){
        productId = createProduct(
                        faker.harryPotter().character(),
                        CategoryType.FURNITURE.getTitle(),
                        (int) ((Math.random() + 4) * 19)).getId();
        var modifiedProduct = modifyProductAndCheckResponse(
                productId,
                faker.food().ingredient(),
                CategoryType.FOOD.getTitle(),
                (int) ((Math.random() + 14) * 19));
        checkProductDbEntry(modifiedProduct);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        assertThat(
                productService.deleteProduct(productId).execute().isSuccessful(),
                CoreMatchers.is(true)
        );
        categoriesExample.clear();
    }

    @SneakyThrows
    @Step("Create new product with Title={0}, Category={1} and Price={2}")
    Product createProduct(String title, String category, int price) {
        var product = productService.createProduct(new Product()
                .withTitle(title)
                .withCategoryTitle(category)
                .withPrice(price))
                .execute().body();
        Allure.addAttachment("New product", product.toString());
        return product;
    }

    @SneakyThrows
    @Step("Modify product with Id={0} and check Response")
    Product modifyProductAndCheckResponse(int productId,
                                          String newProductTitle,
                                          String newProductCategory,
                                          int newProductPrice){
        var modifiedProduct = new Product()
                .withId(productId)
                .withTitle(newProductTitle)
                .withCategoryTitle(newProductCategory)
                .withPrice(newProductPrice);
        Response<Product> response = productService.modifyProduct(modifiedProduct).execute();
        var product = response.body();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(product.getId(), equalTo(modifiedProduct.getId()));
        assertThat(product.getTitle(), equalTo(modifiedProduct.getTitle()));
        assertThat(product.getCategoryTitle(), equalTo(modifiedProduct.getCategoryTitle()));
        assertThat(product.getPrice(), equalTo(modifiedProduct.getPrice()));
        Allure.addAttachment("Modified product from API", product.toString());
        return product;
    }

    @Step("Check product in DB")
    void checkProductDbEntry(Product product){
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
