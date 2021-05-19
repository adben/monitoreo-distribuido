package org.acme.opentracing.services;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

@Traced(operationName = "FranceService")
@ApplicationScoped
public class FrancophoneService {

    public String bonjour() {
        return "bonjour";
    }
}
