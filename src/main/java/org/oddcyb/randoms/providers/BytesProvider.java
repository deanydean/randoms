/*
 * Copyright 2016 Matt Dean
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.oddcyb.randoms.providers;

import java.util.Random;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Route that can provide a random number of bytes for the service.
 */
public class BytesProvider implements Route
{
    public static final String COUNT_PARAM = ":count";
    
    public static final int REQ_ERROR = 400;
    public static final int MAX_BYTES = 1024;
    public static final String RESP_TYPE_BIN = "application/octet-stream";
 
    private final Random rng;
    
    /**
     * Create a new BytesProvider that uses the provided RNG to generate the 
     * random bytes.
     * 
     * @param rng the RNG to use the generate the random bytes
     */
    public BytesProvider(Random rng)
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
        
        // Get the bytes
        byte[] bytes = new byte[count];
        rng.nextBytes(bytes);
        
        response.type(RESP_TYPE_BIN);
        return bytes;
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
