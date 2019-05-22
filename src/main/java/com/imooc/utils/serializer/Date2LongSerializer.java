package com.imooc.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 时间date 转 long
 * 为了对应前端  默认转的是毫秒   前端需要的是秒
 */
public class Date2LongSerializer extends JsonSerializer<Date> {

    @Override
    /*具体的转换实现*/
    //如何使用   在需要时间转换的pojo类的属性上面加注解即可  @JsonSerialize(using = Date2LongSerializer.class)
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeNumber(date.getTime() / 1000);
    }
}
