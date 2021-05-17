package org.acme.opentracing.hello;

import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Traced
@ApplicationScoped
public class SpanishSpeakingService {

    public static final String BAGGAGE_KEY = "pass.the.dutchie";

    @Inject
    io.opentracing.Tracer configuredTracer;

    public String hola() {
        configuredTracer.activeSpan().setBaggageItem(BAGGAGE_KEY, "chevere! ");
        return "hola";
    }
}
