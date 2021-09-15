package io.github.benas.jql.contants;

public class Contants {
    /**
     *xml路径(解析类和code映射关系)
     */
    public static final String RESOLVE_XMLPATH = "src\\main\\resources\\SYSDATA\\SERVICEREGCFG";


    /**
     *变更类和方法的json串
     */
    public static final String CLASS_DIFFCODE_JSON = "[\n" +
            "        {\n" +
            "            \"className\": \"com.example.springbootdemo.Test1.java\",\n" +
            "            \"methodName\": \"threeMethod\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"className\": \"com.example.springbootdemo.utils.IdsBase64Utils.java\",\n" +
            "            \"methodName\": \"base64Encode\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"className\": \"com.example.springbootdemo.HelloController.java\",\n" +
            "            \"methodName\": \"main\"\n" +
            "        }\n" +
            "    ]";
}
