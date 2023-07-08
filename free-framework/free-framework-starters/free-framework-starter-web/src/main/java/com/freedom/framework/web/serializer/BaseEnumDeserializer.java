package com.freedom.framework.web.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.freedom.framework.web.util.ReflectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

public class BaseEnumDeserializer extends JsonDeserializer<Enum> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        Class findPropertyType;
        /*
          解释一下这里， currentName，currentValue，对象的属性是这样的 private IsOk isOk;
         */
        if (StringUtils.isNotEmpty(currentName) && ObjectUtils.isNotEmpty(currentValue)) {
            findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        } else {
            /*
             * 上面的两个值为空时 对象的定义为集合时 private List<IsOk> isOk;
             * 我们使用这个api jp.getParsingContext().getParent().getCurrentName()
             */
            String parentCurrentName = jp.getParsingContext().getParent().getCurrentName();
            Object parentCurrentValue = jp.getParsingContext().getParent().getCurrentValue();
            /*
             * 下面使用反射的方法获取到具体属性值对应枚举值就是 private List<IsOk> isOk;中的IsOk 枚举
             */
            findPropertyType = ReflectUtil.getFieldType(parentCurrentName, parentCurrentValue.getClass());
        }
        Enum valueOf = null;
        if (ObjectUtils.isNotEmpty(findPropertyType)) {
            JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
            /*
             * node.get("name") 的name 调用的就是枚举中的
             * public String getName() {
             *   return this.name();
             * }
             * 所以就要求所有的枚举都必须添加getName()方法，否则就会报错
             */
            JsonNode name = node.get("code");
            if (annotation == null || annotation.shape() != JsonFormat.Shape.OBJECT || name == null) {
                valueOf = Enum.valueOf(findPropertyType, node.asText());
            } else {
                valueOf = Enum.valueOf(findPropertyType, name.asText());
            }
        }
        return valueOf;
    }
}

