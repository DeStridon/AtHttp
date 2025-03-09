package com.destridon.athttp;

import org.junit.jupiter.api.Test;

public class AtHttpTest {

    @Test
    public void test() {
        AtHttpRepository repository = AtHttp.generate(AtHttpRepository.class);
        String response = repository.getRepository("destridon", "athttp");
        System.out.println(response);
    }

}
