package ru.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.retrofit.dto.Product;

public interface ProductService {
    @GET("products")
    Call<Product[]> getAllProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("products")
    Call<Product> createProduct(@Body Product newProduct);

    @PUT("products")
    Call<Product> modifyProduct(@Body Product modifiedProduct);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
