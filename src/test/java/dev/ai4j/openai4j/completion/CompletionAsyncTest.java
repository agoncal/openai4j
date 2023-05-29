package dev.ai4j.openai4j.completion;

import dev.ai4j.openai4j.OpenAiService;
import dev.ai4j.openai4j.RateLimitAwareTest;
import dev.ai4j.openai4j.ResponseHandler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class CompletionAsyncTest extends RateLimitAwareTest {

    private static final String PROMPT = "write exactly the following 2 words: 'hello world'";

    private final OpenAiService openAiService = new OpenAiService(System.getenv("OPENAI_API_KEY"));

    @Test
    void testWithBuilder() throws ExecutionException, InterruptedException, TimeoutException {

        CompletionRequest request = CompletionRequest.builder()
                .prompt(PROMPT)
                .build();

        CompletableFuture<CompletionResponse> future = new CompletableFuture<>();


        openAiService.getCompletionsAsync(request, new ResponseHandler<CompletionResponse>() {

            @Override
            public void onResponse(CompletionResponse response) {
                future.complete(response);
            }

            @Override
            public void onFailure(Throwable t) {
                future.completeExceptionally(t);
            }
        });


        CompletionResponse response = future.get(30, SECONDS);

        assertThat(response.choices()).hasSize(1);
        assertThat(response.choices().get(0).text()).containsIgnoringCase("hello world");

        assertThat(response.text()).containsIgnoringCase("hello world");
    }

    @Test
    void testWithPrompt() throws ExecutionException, InterruptedException, TimeoutException {

        CompletableFuture<String> future = new CompletableFuture<>();


        openAiService.getCompletionAsync(PROMPT, new ResponseHandler<String>() {

            @Override
            public void onResponse(String response) {
                future.complete(response);
            }

            @Override
            public void onFailure(Throwable t) {
                future.completeExceptionally(t);
            }
        });


        String response = future.get(30, SECONDS);

        assertThat(response).containsIgnoringCase("hello world");
    }
}