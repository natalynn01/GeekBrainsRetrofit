package ru.retrofit.utils;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.retrofit.db.mapper.CategoriesMapper;
import ru.retrofit.db.mapper.ProductsMapper;

import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class DbMapperFactory {
    String resource = "mybatisConfig.xml";
    Properties props = ConfigUtils.getProperties();

    @SneakyThrows
    public <T> T getDbMapper(Class<T> clazz) {
        if (clazz.isInstance(CategoriesMapper.class) || clazz.isInstance(ProductsMapper.class)) {
            throw new IllegalArgumentException("Type of class " + clazz.getName() + " was not mapped");
        }
        var inputStream  = Resources.getResourceAsStream(resource);
        var sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);
        var session = sqlSessionFactory.openSession();
        session.getConfiguration().addMapper(clazz);

        return session.getMapper(clazz);
    }
}
