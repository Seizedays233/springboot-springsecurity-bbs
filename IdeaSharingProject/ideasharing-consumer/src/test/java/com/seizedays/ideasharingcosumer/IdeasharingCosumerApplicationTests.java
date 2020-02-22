package com.seizedays.ideasharingcosumer;


import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IdeasharingCosumerApplicationTests {

    @Reference
    PostService postService;

    @Test
    public void test01(){

    }

}
