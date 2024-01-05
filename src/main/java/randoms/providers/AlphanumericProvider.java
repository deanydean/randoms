package randoms.providers;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Random;

public class AlphanumericProvider implements Route
{

    public static final String COUNT_PARAM = ":count";

    public static final int REQ_ERROR = 400;
    public static final int DEFAULT_LENGTH = 64;
    public static final int MAX_LENGTH = 2*1024;
    public static final String RESP_TYPE_TEXT = "text/plain";
    public static int LOWER_LIMIT = 48; // numeral '0'
    public static int UPPER_LIMIT = 122; // letter 'z'
 
    private final Random rng;
    
    /**
     * Create a new AlphanumericProvider that uses the provided RNG to generate
     * the random alpha-numeric string.
     * 
     * @param rng the RNG to use the generate the alphanumeric string
     */
    public AlphanumericProvider(Random rng)
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
        // Get the length of the string that has been requested
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
            if ( count > MAX_LENGTH )
            {
                Spark.halt(REQ_ERROR);
                return null;
            }
        }
        else
        {
            count = DEFAULT_LENGTH;
        }

        var generatedString = 
            this.rng
                .ints(LOWER_LIMIT, UPPER_LIMIT + 1)
                .filter(i -> AlphanumericProvider.isPrintableAlphanum(i))
                .limit(count)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        response.type(RESP_TYPE_TEXT);
        return generatedString;
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

    private static boolean isPrintableAlphanum(int value)
    {
        return (value <= 57 || value >= 65) && (value <= 90 || value >= 97);
    }
}
