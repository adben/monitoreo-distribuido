package org.acme.opentracing;

import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

@Traced
@ApplicationScoped
public class NapService {

    @Inject
    io.opentracing.Tracer configuredTracer;

    public String nap() {
        configuredTracer.activeSpan().log("Received request on Thread: " + Thread.currentThread().getName());
        final BigDecimal bigDecimal = Pi.computePi(20000);
        configuredTracer.activeSpan().setTag("out.computed.pi", bigDecimal);
        configuredTracer.activeSpan().log("Doing some load");
        configuredTracer.activeSpan().log("Back from the nap: " + Thread.currentThread().getName());
        return "Nap from " + new Date().toString();
    }

}
