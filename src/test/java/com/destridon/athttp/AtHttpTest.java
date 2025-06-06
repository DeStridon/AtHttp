package com.destridon.athttp;

import java.util.HashMap;

import org.junit.jupiter.api.Test;


public class AtHttpTest {


    @AtHttp.Path(value = "/repos/{owner}/{repo}")
    public static interface AtHttpRepository {

        String getRepository(@AtHttp.RequestParam String owner, @AtHttp.RequestParam String repo);

        void patchRepository(@AtHttp.RequestParam String owner, @AtHttp.RequestParam String repo, @AtHttp.RequestParam String name, @AtHttp.RequestParam String description, @AtHttp.RequestParam String homepage);

        @AtHttp.Path("sub")
        public static interface subInterface {
            
            // node that defines http call
            @AtHttp.Path("method")
            String postResult();
            
        }

    }

    @Test
    public void test() {
        AtHttpRepository repository = AtHttp.generate(AtHttpRepository.class, new HashMap<>());
        String response = repository.getRepository("destridon", "athttp");
        System.out.println(response);
    }
    
    @Test
    public void subTest() {
    	AtHttpRepository.subInterface repository = AtHttp.generate(AtHttpRepository.subInterface.class, new HashMap<>());
        String response = repository.postResult();
        System.out.println(response);
    	
    }
    

}
