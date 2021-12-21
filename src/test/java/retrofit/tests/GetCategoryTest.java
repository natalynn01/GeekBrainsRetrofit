package retrofit.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.retrofit.db.mapper.CategoriesMapper;
import ru.retrofit.db.mapper.ProductsMapper;
import ru.retrofit.dto.GetCategoryResponse;
import ru.retrofit.dto.Product;
import ru.retrofit.service.CategoryService;
import ru.retrofit.utils.DbMapperFactory;
import ru.retrofit.utils.RetrofitUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Get products category test suite")
@Tag("GetProductsCategory")
public class GetCategoryTest {
    static CategoryService categoryService;
    static CategoriesMapper categoriesMapper;
    static ProductsMapper productsMapper;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
        categoriesMapper = DbMapperFactory.getDbMapper(CategoriesMapper.class);
        productsMapper = DbMapperFactory.getDbMapper(ProductsMapper.class);
    }

    @DisplayName("Test_case_1. Categories test for Food.")
    @Description("That test sending request for getting response body with all items with FOOD")
    @Tag("FoodCategoriesTest")
    @SneakyThrows
    @Test
    void getFoodCategoryPositiveTest() {
        var productList = getProductsOnCategoryAndCheckResponse(1, "Food");
        checkCategoryForDbEntry(productList, 1);
    }

    @DisplayName("Test_case_2. Categories test for Electronic.")
    @Description("That test sending request for getting response body with all items with Electronic")
    @Tag("ElectronicCategoriesTest")
    @SneakyThrows
    @Test
    void getElectronicCategoryPositiveTest() {
        var productList = getProductsOnCategoryAndCheckResponse(2, "Electronic");
        checkCategoryForDbEntry(productList, 2);
    }

    @DisplayName("Test_case_3. Categories test for Furniture.")
    @Description("That test sending request for getting response body with all items with Furniture")
    @Tag("FurnitureCategoriesTest")
    @SneakyThrows
    @Test
    void getFurnitureCategoryPositiveTest() {
        var productList = getProductsOnCategoryAndCheckResponse(3, "Furniture");
        checkCategoryForDbEntry(productList, 3);
    }

    @SneakyThrows
    @Step("Get list of products with category id={0} and category name={1}")
    List<Product> getProductsOnCategoryAndCheckResponse(int categoryId, String categoryName) {
        Response<GetCategoryResponse> response = categoryService.getCategory(categoryId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(categoryId));
        assertThat(response.body().getTitle(), equalTo(categoryName));
        var productsList = response.body().getProducts();
        productsList.forEach(product -> {
                    assertThat(product.getCategoryTitle(), equalTo(categoryName));
                    Allure.addAttachment("Product " + product.getId(), product.toString());
                });
        return productsList;
    }

    @Step("Check category in DB entry for each product")
    void checkCategoryForDbEntry(List<Product> productList, int categoryId) {
        productList.forEach(product -> {
            var productDbEntry =  productsMapper.selectByPrimaryKey((long) product.getId());
            var dbEntryTitle = productDbEntry.getTitle();
            var title = product.getTitle();
            if (dbEntryTitle != null && title != null) {
                dbEntryTitle = dbEntryTitle.trim();
                title = title.trim();
            }
            assertThat(productDbEntry.getId(), equalTo((long) product.getId()));
            assertThat(dbEntryTitle, equalTo(title));
            assertThat(productDbEntry.getPrice(), equalTo(product.getPrice()));
            assertThat(productDbEntry.getCategory_id(), equalTo((long) categoryId));
            Allure.addAttachment("DB Entry for product with Id=" + product.getId(), productDbEntry.toString());
        });
    }
}
