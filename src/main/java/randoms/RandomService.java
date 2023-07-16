/*
 * Copyright 2023 Matt Dean
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
package randoms;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import randoms.providers.BytesProvider;
import spark.Spark;

/**
 * Service that provides random data.
 */
public class RandomService 
{
    public static final Logger LOG = 
        Logger.getLogger(RandomService.class.getName());
    
    public static final String SERVICE_BASE = "/randoms";
    
    private final Random rng = new Random();
    
    /**
     * Start the service.
     */
    public void start(int listenPort)
    {
        Spark.port(listenPort);

        // Register the basic numeric types
        Spark.get(SERVICE_BASE+"/long", (req,res) -> this.rng.nextLong());
        Spark.get(SERVICE_BASE+"/int", (req,res) -> this.rng.nextInt());
        Spark.get(SERVICE_BASE+"/float", (req,res) -> this.rng.nextFloat());
        Spark.get(SERVICE_BASE+"/double", (req,res) -> this.rng.nextDouble());
        Spark.get(SERVICE_BASE+"/gaussian", (req,res) -> this.rng.nextGaussian());
        
        // Register the types we have providers for
        Spark.get(SERVICE_BASE+"/bytes/"+BytesProvider.params(), new BytesProvider(rng));
        // TODO - UUID
        // TODO - String
        // TODO - Int with max

        // Log exceptions
        Spark.exception(Exception.class, (ex, req, resp) -> {
            LOG.log(Level.WARNING, "Request {0} failed", req);
            LOG.log(Level.WARNING, "Request error", ex);
            
            resp.status(500);
        });
    }
    
    public static void main(String[] args)
    {
        int listenPort = (args.length > 0) ? Integer.parseInt(args[0]) : 9999;

        new RandomService().start(listenPort);
        LOG.info("Randoms service started. Listening on port "+listenPort+"....");
    }
    
}