package retrofit.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.retrofit.dto.GetCategoryResponse;
import ru.retrofit.service.CategoryService;
import ru.retrofit.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Get products category test suite")
@Tag("GetProductsCategory")
public class GetCategoryTest {
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @DisplayName("Test_case_1. Categories test for Food.")
    @Description("That test sending request for getting response body with all items with FOOD")
    @Tag("FoodCategoriesTest")
    @SneakyThrows
    @Test
    void getFoodCategoryPositiveTest() {
        createProductAndCheckResponse(1, "Food");
    }

    @DisplayName("Test_case_2. Categories test for Electronic.")
    @Description("That test sending request for getting response body with all items with Electronic")
    @Tag("ElectronicCategoriesTest")
    @SneakyThrows
    @Test
    void getElectronicCategoryPositiveTest() {
        createProductAndCheckResponse(2, "Electronic");
    }

    @DisplayName("Test_case_3. Categories test for Furniture.")
    @Description("That test sending request for getting response body with all items with Furniture")
    @Tag("FurnitureCategoriesTest")
    @SneakyThrows
    @Test
    void getFurnitureCategoryPositiveTest() {
        createProductAndCheckResponse(3, "Furniture");
    }

    @SneakyThrows
    @Step("Get list of products with category id={0} and category name={1}")
    void createProductAndCheckResponse(int categoryId, String categoryName) {
        Response<GetCategoryResponse> response = categoryService.getCategory(categoryId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(categoryId));
        assertThat(response.body().getTitle(), equalTo(categoryName));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(categoryName)));
    }
}
