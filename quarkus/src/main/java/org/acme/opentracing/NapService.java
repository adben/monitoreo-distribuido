package org.acme.opentracing;

import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

@Traced
@ApplicationScoped
public class NapService {

    public String nap() {
        System.out.println("Received request on Thread: " + Thread.currentThread().getName());
        System.out.println("Doing some load " + Pi.computePi(20000));
        System.out.println("Back from the nap: " + Thread.currentThread().getName());
        return "Nap from " + new Date().toString();
    }

}
