package com.turygin.api.server.cofig;

import com.turygin.api.server.resource.CourseResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.*;

@ApplicationPath("/")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(CourseResource.class));
    }
}
