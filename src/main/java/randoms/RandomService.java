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

import randoms.providers.AlphanumericProvider;
import randoms.providers.Base64Provider;
import randoms.providers.BytesProvider;
import randoms.providers.UuidProvider;
import spark.Spark;

/**
 * Service that provides random data.
 */
public class RandomService 
{
    public static final Logger LOG = 
        Logger.getLogger(RandomService.class.getName());
    
    public static final String SERVICE_BASE = 
        System.getenv().getOrDefault("RANDOM_BASE_PATH", "");
    
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
        Spark.get(SERVICE_BASE+"/boolean", (req,res) -> this.rng.nextBoolean());
        Spark.get(SERVICE_BASE+"/bool", (req,res) -> this.rng.nextBoolean());
        
        // Register the types we have providers for
        var bytesProvider = new BytesProvider(rng);
        Spark.get(SERVICE_BASE+"/bytes", bytesProvider);
        Spark.get(SERVICE_BASE+"/bytes"+BytesProvider.params(), bytesProvider);

        var alphanumericProvider = new AlphanumericProvider(rng);
        Spark.get(SERVICE_BASE+"/alphanum", alphanumericProvider);
        Spark.get(SERVICE_BASE+"/string", alphanumericProvider);
        Spark.get(SERVICE_BASE+"/str", alphanumericProvider);
        Spark.get(SERVICE_BASE+"/alphanum"+AlphanumericProvider.params(), alphanumericProvider);
        Spark.get(SERVICE_BASE+"/string"+AlphanumericProvider.params(), alphanumericProvider);
        Spark.get(SERVICE_BASE+"/str"+AlphanumericProvider.params(), alphanumericProvider);

        var base64Provider = new Base64Provider(rng);
        Spark.get(SERVICE_BASE+"/base64", base64Provider);
        Spark.get(SERVICE_BASE+"/base64"+Base64Provider.params(), base64Provider);

        Spark.get(SERVICE_BASE+"/uuid", new UuidProvider());
        
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