package org.acme.opentracing.services;

import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import org.acme.opentracing.Pi;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Traced(operationName = "NapService")
@ApplicationScoped
public class NapService {

    private static final Logger LOG = Logger.getLogger(NapService.class);

    @Inject
    io.opentracing.Tracer configuredTracer;

    @Inject
    Vertx vertx;

    public String nap() {
        final Span span = configuredTracer.activeSpan();
        span.log("Received request on Thread: " + Thread.currentThread().getName());
        final BigDecimal bigDecimal = Pi.computePi(20000);
        span.setTag("out.computed.pi", bigDecimal);
        span.log("Doing some load");
        span.log("Back from the nap: " + Thread.currentThread().getName());
        return "Nap";
    }

    public Future<String> longerNap() {
        final String filename = "lorem-ipsum" + Thread.currentThread().getName() + ".txt";
        //final String filename = "lorem-ipsum" + UUID.randomUUID() + ".txt";
        final Span span = GlobalTracer.get().buildSpan("someBusinessSpan").asChildOf(configuredTracer.activeSpan()).start();
        span.log("Received request on Thread: " + Thread.currentThread().getName());
        final FileSystem fileSystem = vertx.fileSystem();

        return fileSystem.createFile(filename)
                .onFailure(cause -> spanTagError(span, "failed_creating", cause))
                .compose(__ -> fileSystem.open(filename, new OpenOptions()))
                .onFailure(cause -> spanTagError(span, "failed_opening", cause))
                .onSuccess(open -> open.write(Buffer.buffer(Pi.computePi(20000).toString())))
                .onSuccess(write -> span.log(String.format("done writing file %s", filename)))
                .onComplete(__ -> LOG.info("wrote " + filename))
                .onFailure(cause -> spanTagError(span, "failed_writing", cause))
                .compose(__ -> fileSystem.props(filename))
                .onFailure(cause -> spanTagError(span, "failed_reading_props", cause))
                .onSuccess(props -> span.setTag("file.size", props.size()))
                .compose(__ -> fileSystem.delete(filename))
                .onSuccess(deleted -> span.log("deleted"))
                .onSuccess(complete -> span.finish())
                .onFailure(x -> spanTagError(span, "failed_deleting", x))
                .map(__ -> "longer Nap");
    }

    private void spanTagError(Span span, String kibanaFlag, Throwable cause) {
        LOG.error("error at " + kibanaFlag);
        Tags.ERROR.set(span, true);
        span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, cause, Fields.MESSAGE, cause.getMessage()));
        span.setTag("error-recorded", kibanaFlag);
        span.finish();
    }

}
