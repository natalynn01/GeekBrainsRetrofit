# GeekBrainsRetrofit
для запуска тестов и автоматического открытия отчета в браузере необходимо выполнить команду `mvn clean test -Durl=Base_URL allure:serve` где 
`Base_URL` - адрес тестируемого приложения

## `GetCategoryTest` - проверка получения продуктов определенной категории при запросе к `category-controller`:
1) Categories test for Food - проверка получения продуктов с категорией "Food"
2) Categories test for Electronic - проверка получения продуктов с категорией "Electronic"
3) Categories test for Furniture - проверка получения продуктов с категорией "Furniture"

## `CreateProductTest` - проверка создания нового продукта для всех категорий при запросе к `product-controller`:
4) Create a new product with Food category test - проверка создания нового продукта с категорией "Food"
5) Create a new product with Electronic category test - проверка создания нового продукта с категорией "Electronic"
6) Create a new product with Furniture category test - проверка создания нового продукта с категорией "Furniture"

## `ModifyProductTest` - проверка создания нового продукта для всех категорий при запросе к `product-controller`:
7) Modify product from Food category to Electronic test - проверка изменения продукта с категорией "Food"
8) Modify product from Electronic category to Furniture category test - проверка изменения продукта с категорией "Electronic"
9) Modify product from Furniture category to Food category test - проверка изменения продукта с категорией "Furniture"

## `GetProductsTest` - проверка получения продуктов
10) Get all products test - проверка получения массива всех продуктов
11) Get product on Id test - проверка получения продукта по Id

## `DeleteProduct` - проверка удаления продуктов по id
12) Delete product on Id test - проверка удаления продукта по id