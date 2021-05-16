package org.acme.opentracing;

import io.opentracing.Span;
import io.opentracing.tag.Tags;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@Traced
@ApplicationScoped
public class NapService {

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
        final Span span = configuredTracer.activeSpan();
        span.log("Received request on Thread: " + Thread.currentThread().getName());

        vertx.fileSystem().open("filename", new OpenOptions(), toOpen -> {
            if (toOpen.succeeded()) {
                Buffer buff = Buffer.buffer(Pi.computePi(20000).toString()); //heavy shit
                AsyncFile file = toOpen.result();
                file.write(buff, toWrite -> {
                    if (toWrite.succeeded()) {
                        span.log("done");
                        vertx.fileSystem().props(filename, toReadProps -> {
                            if (toReadProps.succeeded()) {
                                span.setTag("file.size", toReadProps.result().size());
                                vertx.fileSystem().delete(filename, toDelete -> {
                                    if (toDelete.succeeded()) {
                                        span.log("deleted");
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

        return "longer nap";
    }

    private void spanTagError(Span span, String message, Throwable cause) {
        span.setTag(String.valueOf(Tags.ERROR), true);
        span.log(message + cause);
    }

}
