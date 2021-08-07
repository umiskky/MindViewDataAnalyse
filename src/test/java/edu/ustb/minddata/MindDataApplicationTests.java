package edu.ustb.minddata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MindDataApplicationTests {

    @Test
    void contextLoads() throws Exception{
//        String nature = "{\"name\" : \"希儿\"}";
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> map = objectMapper.readValue(nature, new TypeReference<Map<String,String>>(){});
//        System.out.println(map.get("name"));
//
//        System.out.println(objectMapper.writeValueAsString(map));
    }

}
