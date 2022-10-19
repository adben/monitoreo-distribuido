package org.acme.opentracing;

import org.acme.opentracing.services.IngestorService;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/")
public class FileSystemIngestorResource {

    private static final Logger LOG = Logger.getLogger(FileSystemIngestorResource.class);

    @Inject
    IngestorService ingestor;

//    @Inject
//    io.opentracing.Tracer configuredTracer; //TODO


    @GET
    @Path("ingestor")
    @Produces(MediaType.TEXT_PLAIN)
    @Traced(operationName = "ingestor")
    public CompletionStage<String> doIngest() {
        LOG.info("ingestor");
        return ingestor.obtainData();
    }
}
