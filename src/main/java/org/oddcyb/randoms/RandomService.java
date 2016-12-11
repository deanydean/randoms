/*
 * Copyright 2016 Matt Dean. All rights reserved.
 */
package org.oddcyb.randoms;

import java.util.Random;
import org.oddcyb.randoms.providers.BytesProvider;
import spark.Spark;

/**
 * Service that provides random data.
 */
public class RandomService 
{
    
    public static final String SERVICE_BASE = "/randoms";
    
    private final Random rng = new Random();
    
    /**
     * Start the service.
     */
    public void start()
    {
        // Register the basic numeric types
        Spark.get(SERVICE_BASE+"/long", (req,res) -> this.rng.nextLong());
        Spark.get(SERVICE_BASE+"/int", (req,res) -> this.rng.nextInt());
        Spark.get(SERVICE_BASE+"/float", (req,res) -> this.rng.nextFloat());
        Spark.get(SERVICE_BASE+"/double", (req,res) -> this.rng.nextDouble());
        Spark.get(SERVICE_BASE+"/gaussian", (req,res) -> this.rng.nextGaussian());
        
        // Register the types we have providers for
        Spark.get(SERVICE_BASE+"/bytes"+BytesProvider.params(), new BytesProvider(rng));
    }
    
    public static void main(String[] args)
    {
        new RandomService().start();
    }
    
}