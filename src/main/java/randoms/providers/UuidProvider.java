package randoms.providers;

import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Route that can provide a random number of bytes for the service.
 */
public class UuidProvider implements Route
{
    
    public static final int REQ_ERROR = 400;
    public static final String RESP_TYPE_TEXT = "text/plain";
    
    /**
     * Handle the route request.
     * 
     * @param request the request
     * @param response the response
     * @return the UUID or null if something went wrong.
     */
    @Override
    public Object handle(Request request, Response response)
    {
        var uuid = UUID.randomUUID();
        
        response.type(RESP_TYPE_TEXT);
        return uuid.toString();
    }
    
}
