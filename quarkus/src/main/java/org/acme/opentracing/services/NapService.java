package org.acme.opentracing.services;

import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import org.acme.opentracing.Pi;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;

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

    public String longerNap() {

        final String filename = "lorem-ipsum" + Thread.currentThread().getName() + ".txt";
        final Span span = GlobalTracer.get().buildSpan("someBusinessSpan").asChildOf(configuredTracer.activeSpan()).start();
        span.log("Received request on Thread: " + Thread.currentThread().getName());
//        final FileSystem fileSystem = vertx.fileSystem();
//        fileSystem.createFile(filename)
//                .onFailure(x -> spanTagError(span, "failed_creating", x))
//                .compose(__ -> fileSystem.open(filename, new OpenOptions()))
//                .onFailure(x -> spanTagError(span, "failed_opening", x))
//                .onSuccess(open -> open.write(Buffer.buffer(Pi.computePi(20000).toString())))
//                .onSuccess(write -> {
//                    span.log("done writing file");
//                    LOG.info("wrote " + filename);
//                })
//                .onFailure(x -> spanTagError(span, "failed_writing", x))
//                .compose(__ -> fileSystem.props(filename))
//                .onFailure(x -> spanTagError(span, "failed_reading_props", x))
//                .onSuccess(props -> span.setTag("file.size", props.size()))
//                .compose(__ -> fileSystem.delete(filename))
//                .onSuccess(deleted -> {
//                    span.log("deleted");
//                    span.finish();
//                })
//                .onFailure(x -> spanTagError(span, "failed_deleting", x))
//                .mapEmpty();
        vertx.fileSystem().createFile(filename, toCreate -> {
            if (toCreate.succeeded()) {
                vertx.fileSystem().open(filename, new OpenOptions(), toOpen -> {
                    if (toOpen.succeeded()) {
                        Buffer buff = Buffer.buffer(Pi.computePi(20000).toString()); //heavy shit
                        AsyncFile file = toOpen.result();
                        file.write(buff, toWrite -> {
                            if (toWrite.succeeded()) {
                                span.log("done writing file");
                                LOG.info("wrote " + filename);
                                vertx.fileSystem().props(filename, toReadProps -> {
                                    if (toReadProps.succeeded()) {
                                        span.setTag("file.size", toReadProps.result().size());
                                        vertx.fileSystem().delete(filename, toDelete -> {
                                            if (toDelete.succeeded()) {
                                                span.log("deleted");
                                                span.finish();
                                            } else {
                                                spanTagError(span, "failed_deleting", toDelete.cause());
                                            }
                                        });
                                    } else {
                                        spanTagError(span, "failed_reading", toReadProps.cause());
                                    }
                                });
                            } else {
                                spanTagError(span, "failed_writing", toWrite.cause());
                            }
                        });
                    } else {
                        spanTagError(span, "failed_opening", toOpen.cause());
                    }
                });
            } else {
                spanTagError(span, "failed_creating", toCreate.cause());
            }

        });

        return "longer nap";
    }

    private void spanTagError(Span span, String kibanaFlag, Throwable cause) {
        LOG.error("error at " + kibanaFlag);
        Tags.ERROR.set(span, true);
        span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, cause, Fields.MESSAGE, cause.getMessage()));
        span.setTag("error-recorded", kibanaFlag);
        span.finish();
    }

}
