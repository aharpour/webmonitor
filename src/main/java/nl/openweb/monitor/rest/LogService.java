package nl.openweb.monitor.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.openweb.monitor.Monitor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/log")
public class LogService {

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/change")
    public Response changeLogLevel(LogLevel level) {
        boolean result = false;
        Logger monitorLogger = LogManager.getLogger(Monitor.class);
        Level l = getLevel(level);
        if (monitorLogger instanceof org.apache.logging.log4j.core.Logger && l != null) {
            ((org.apache.logging.log4j.core.Logger) monitorLogger).setLevel(l);
            result = true;
        }
        return Response.ok().entity(result).build();
    }

    private Level getLevel(LogLevel level) {
        Level result;
        switch (level.getLevel()) {
        case "OFF":
            result = Level.OFF;
            break;
        case "FATAL":
            result = Level.FATAL;
            break;
        case "ERROR":
            result = Level.ERROR;
            break;
        case "WARN":
            result = Level.WARN;
            break;
        case "INFO":
            result = Level.INFO;
            break;
        case "DEBUG":
            result = Level.DEBUG;
            break;
        case "TRACE":
            result = Level.TRACE;
            break;
        case "ALL":
            result = Level.ALL;
            break;
        default:
            result = null;
            break;
        }
        return result;

    }
}
