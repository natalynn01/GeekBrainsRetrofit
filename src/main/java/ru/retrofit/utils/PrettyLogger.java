package ru.retrofit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.retrofit.db.mapper.CategoriesMapper;
import ru.retrofit.db.mapper.ProductsMapper;
import ru.retrofit.db.model.Categories;
import ru.retrofit.db.model.CategoriesExample;
import ru.retrofit.db.model.ProductsExample;
import ru.retrofit.service.CategoryService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class PrettyLogger implements HttpLoggingInterceptor.Logger{
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void log(String message) {
        String trimmedMessage = message.trim();
        if ((trimmedMessage.startsWith("{") && trimmedMessage.endsWith("}"))
                || (trimmedMessage.startsWith("[") && trimmedMessage.endsWith("]"))) {
            try {Object value = mapper.readValue(message, Object.class);
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
                Platform.get().log(Platform.INFO, prettyJson, null);
            } catch (JsonProcessingException e) {
                Platform.get().log(Platform.WARN, message, e);
            }
        } else {
            Platform.get().log(Platform.INFO, message, null);
        }
    }

    public static class Main {
        //NyBatis Configuration file
        static String resource = "mybatisConfig.xml";
        public static void main(String[] args) throws IOException {
            CategoriesMapper categoriesMapper = DbMapperFactory.getDbMapper(CategoriesMapper.class);
            ProductsMapper productsMapper = DbMapperFactory.getDbMapper(ProductsMapper.class);
            CategoriesExample categoriesExample = new CategoriesExample();
            ProductsExample productsExample = new ProductsExample();

            categoriesExample.createCriteria().andIdBetween(1, 3);
            List<Categories> categories = categoriesMapper.selectByExample(categoriesExample);
            categories.forEach(row -> System.out.println(row.getTitle()));
            categoriesExample.clear();

            categoriesExample.createCriteria().andTitleEqualTo("Food");
            categories = categoriesMapper.selectByExample(categoriesExample);
            categories.forEach(row -> System.out.println(row.getTitle()));

        }

        @SneakyThrows
        public static Integer countNumberOfAllCategories() {
            CategoriesMapper categoriesMapper = getCategoriesMapper(resource);
            CategoriesExample example = new CategoriesExample();
            example.createCriteria().andIdBetween(0,1000);
            long qty = categoriesMapper.countByExample(example);
            return Math.toIntExact(qty);
        }

        public static List<Categories> getCategories(CategoriesMapper categoriesMapper, CategoriesExample categoriesExample) {
            categoriesExample.createCriteria().andIdBetween(1, 3);
            return categoriesMapper.selectByExample(categoriesExample);
        }

        private static CategoriesMapper getCategoriesMapper(String resource) throws IOException {
            Properties props = ConfigUtils.getProperties();
            InputStream inputStream  = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);
            SqlSession session = sqlSessionFactory.openSession();
            session.getConfiguration().addMapper(CategoriesMapper.class);
            return session.getMapper(CategoriesMapper.class);
        }
    }
}
