package org.acme.opentracing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Path("")
public class DelayedResource {

    public static final int AMOUNT_CALLS = 1000;
    public static final int DURATION_MS = 1000;
    private final AtomicInteger someCounter = new AtomicInteger();

    @GET
    @Path("/concurrent-nap")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> concurrentNap(@Context UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder().path(DelayedResource.class, "delayed").build();
        WebTarget target = ClientBuilder.newClient().target(uri);
        CompletionStageRxInvoker invocation = target.request().rx();
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String>[] calls = new CompletableFuture[AMOUNT_CALLS];
        IntStream.range(0, AMOUNT_CALLS).forEach(i -> calls[i] = invocation.get(String.class)
                .whenComplete((val, t) -> sb.append(val).append(", "))
                .toCompletableFuture());
        return CompletableFuture.allOf(calls).thenApply(v -> sb.toString());
    }

    @GET
    @Path("delayed")
    @Produces(MediaType.TEXT_PLAIN)
    public int delayed() throws InterruptedException {
        Thread.sleep(DURATION_MS);
        return someCounter.getAndIncrement();
    }
}