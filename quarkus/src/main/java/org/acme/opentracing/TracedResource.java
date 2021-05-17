package org.acme.opentracing;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import io.jaegertracing.thriftjava.BaggageRestrictionManager;
import org.acme.opentracing.hello.FrancophoneService;
import org.acme.opentracing.hello.SpanishSpeakingService;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;

import java.util.Optional;

import static org.acme.opentracing.hello.SpanishSpeakingService.BAGGAGE_KEY;

@Path("/")
public class TracedResource {

    private static final Logger LOG = Logger.getLogger(TracedResource.class);

    @Inject
    io.opentracing.Tracer configuredTracer;

    @Inject
    FrancophoneService frenchBean;

    @Inject
    SpanishSpeakingService spanishBean;

    @Inject
    NapService piBean;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        LOG.info("hello");
        return Optional.ofNullable(configuredTracer.activeSpan().getBaggageItem(BAGGAGE_KEY))
                .map(baggage -> {
                    configuredTracer.activeSpan().setTag(BAGGAGE_KEY, baggage);
                    return baggage + "hello";
                })
                .orElse("hello");
    }

    @GET
    @Path("/chain")
    @Produces(MediaType.TEXT_PLAIN)
    public String chain() {
        ResourceClient client = RestClientBuilder.newBuilder()
                .baseUri(uriInfo.getBaseUri())
                .build(ResourceClient.class);
        return "chain -> " + spanishBean.hola() + " -> " + frenchBean.bonjour() + " -> " + client.hello();
    }

    @GET
    @Path("/nap")
    @Produces(MediaType.TEXT_PLAIN)
    public String doNap() {
        ResourceClient client = RestClientBuilder.newBuilder()
                .baseUri(uriInfo.getBaseUri())
                .build(ResourceClient.class);
        return "chain -> " + piBean.nap() + " -> " + client.hello();
    }

    @GET
    @Path("/long-nap")
    @Produces(MediaType.TEXT_PLAIN)
    public String doLongNap() {
        return piBean.longerNap();
    }
}
