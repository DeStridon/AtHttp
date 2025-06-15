package com.destridon.athttp;

import java.util.List;

public class OpenAIChatGPTTest {

    @AtHttp.Path("https://api.openai.com/v1")
    @AtHttp.Header(key="cache-control", value = "no-cache")
    @AtHttp.Header(key="Authorization", value="Bearer {apiKey}")
    public static abstract class OpenAIChatGPT {

        @AtHttp.Path("assistants?order=desc&limit=100")
        public abstract Embedded<List<Assistant>> getAssistants();

    }

    public static class Embedded<U> {
        public U data;
    }

    public static class Assistant {
        public String id;
        public String name;
        public String created_at;
    }

}
