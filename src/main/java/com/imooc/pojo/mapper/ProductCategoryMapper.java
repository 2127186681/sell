package com.imooc.pojo.mapper;

import com.imooc.pojo.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.Map;

public interface ProductCategoryMapper {
    @Insert("insert into product_category(category_name,category_type) values (#{category_name,jdbcType=VARCHAR},#{category_type,jdbcType=INTEGER})")
    int insertByMap(Map<String,Object> map);

    @Insert("insert into product_category(category_name,category_type) values (#{categoryName,jdbcType=VARCHAR},#{categoryType,jdbcType=INTEGER})")
    int insertBy(ProductCategory category);

    @Select("select * from product_category where category_type=#{cate}")
    @Results({
            @Result(column = "category_name",property = "categoryName"),
            @Result(column = "category_type",property = "categoryType"),
            @Result(column = "category_id",property = "categoryId")
    }

    )
    ProductCategory findByCategoryType(Integer cate);

    @Update("update product_category set category_name =#{categoryName}where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName")String categoryNam,
                             @Param("categoryType")Integer categoryType);
}
