package com.lvivjavaclub;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class quarkus {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Uni.createFrom().item("Hello RESTEasy Reactive")
            .onItem().transform(String::toUpperCase);
    }
}
