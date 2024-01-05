package randoms.providers;

import java.util.Base64;
import java.util.Random;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Route that can provide a random number of bytes for the service.
 */
public class Base64Provider implements Route
{
    public static final String COUNT_PARAM = ":count";
    
    public static final int REQ_ERROR = 400;
    public static final int DEFAULT_BYTES = 32;
    public static final int MAX_BYTES = 1024;
    public static final String RESP_TYPE_TEXT = "text/plain";
 
    private final Random rng;
    
    /**
     * Create a new BytesProvider that uses the provided RNG to generate the 
     * random bytes.
     * 
     * @param rng the RNG to use the generate the random bytes
     */
    public Base64Provider(Random rng)
    {
        this.rng = rng;
    }
    
    /**
     * Handle the route request.
     * 
     * @param request the request
     * @param response the response
     * @return the requested number of random bytes or null if something went 
     * wrong.
     */
    @Override
    public Object handle(Request request, Response response)
    {
        // Get the number of bytes that have been requested
        String countParam = request.params(COUNT_PARAM);

        int count;

        if ( countParam != null ) 
        {
            try
            {
                count = Integer.parseInt(countParam);
            }
            catch ( NumberFormatException nfe )
            {
                Spark.halt(REQ_ERROR);
                return null;
            }

            // Check the requested count is < MAX_BYTES
            if ( count > MAX_BYTES )
            {
                Spark.halt(REQ_ERROR);
                return null;
            }
        }
        else
        {
            count = DEFAULT_BYTES;
        }
        
        // Get the bytes
        byte[] bytes = new byte[count];
        rng.nextBytes(bytes);
        
        response.type(RESP_TYPE_TEXT);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    /**
     * Get a String containing the parameters for this provider.
     * 
     * @return a string containing the parameters for this provider.
     */
    public static String params()
    {
        return "/"+COUNT_PARAM;
    }
}
