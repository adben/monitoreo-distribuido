package org.acme.opentracing;

import io.smallrye.mutiny.Uni;
import org.acme.opentracing.services.FrancophoneService;
import org.acme.opentracing.services.NapService;
import org.acme.opentracing.services.SpanishSpeakingService;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class TracedResource {

    private static final Logger LOG = Logger.getLogger(TracedResource.class);

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
        return "hello";
    }

    @GET
    @Path("/chain")
    @Produces(MediaType.TEXT_PLAIN)
    public String chain() {
        LOG.info("chain");
        ResourceClient client = RestClientBuilder.newBuilder()
                .baseUri(uriInfo.getBaseUri())
                .build(ResourceClient.class);
        return "chain -> " + spanishBean.hola() + " -> " + frenchBean.bonjour() + " -> " + client.hello();

    }

    @GET
    @Path("/nap")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> doNap() {
        LOG.info("nap");
        ResourceClient client = RestClientBuilder.newBuilder()
                .baseUri(uriInfo.getBaseUri())
                .build(ResourceClient.class);
        return Uni.createFrom().item("chain -> " + piBean.nap() + " -> " + client.hello());
    }

    @GET
    @Path("/long-nap")
    @Produces(MediaType.TEXT_PLAIN)
    @Traced(operationName = "long-nap")
    public Uni<String> doLongNap() {
        LOG.info("long-nap");
        return Uni.createFrom().item(piBean.longerNap().result());
    }

}