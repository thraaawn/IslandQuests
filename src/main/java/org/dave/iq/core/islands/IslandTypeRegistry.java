package org.dave.iq.core.islands;

import com.google.gson.stream.JsonReader;
import org.dave.iq.api.IIslandType;
import org.dave.iq.api.IIslandTypeRegistry;
import org.dave.iq.core.configuration.ConfigurationHandler;
import org.dave.iq.core.utility.Logz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IslandTypeRegistry implements IIslandTypeRegistry {
    public static IslandTypeRegistry instance = new IslandTypeRegistry();

    public Map<String, IIslandType> islandTypes;
    private double totalWeight = 0.0;

    public void init() {
        Logz.info("Initializing");
        reload();
    }

    public void reload() {
        islandTypes = new HashMap<>();

        if(!ConfigurationHandler.islandDir.exists()) {
            Logz.info("Island directory %s does not exist", ConfigurationHandler.islandDir);
            return;
        }

        for(File file : ConfigurationHandler.islandDir.listFiles()) {
            if(file.getName().endsWith(".json")) {
                Logz.info(" > Loading island type from file: '%s'", file.getName());
                try {
                    IslandType type = IslandTypeJsonLoader.GSON.fromJson(new JsonReader(new FileReader(file)), IslandType.class);
                    IslandTypeRegistry.instance.registerIslandType(type);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if(islandTypes.size() == 0) {
            Logz.warn("No island types registered. This is bad, the game will probably crash!");
        } else {
            Logz.info("Loaded %d island types.", islandTypes.size());
        }
    }

    @Override
    public void registerIslandType(IIslandType type) {
        if(islandTypes.containsKey(type.getName())) {
            Logz.warn("Overwriting island type: %s (weight=%.1f, biome=%s)", type.getName(), type.getWeight(), type.getBiome().getRegistryName());
        } else {
            Logz.info("Registering island type: %s (weight=%.1f, biome=%s, top=%s, filler=%s, bedrock=%s)",
                    type.getName(), type.getWeight(), type.getBiome().getRegistryName(),
                    type.getTopBlock().getBlock().getUnlocalizedName(),
                    type.getFillerBlock().getBlock().getUnlocalizedName(),
                    type.getBedrockBlock() != null ? type.getBedrockBlock().getBlock().getUnlocalizedName() : "-none-"
            );
        }

        totalWeight += type.getWeight();
        islandTypes.put(type.getName(), type);
    }

    public IIslandType getRandomIslandType(Random rand) {
        double treshold = rand.nextDouble() * totalWeight;
        double position = 0.0;
        for(IIslandType type : islandTypes.values()) {
            position += type.getWeight();
            if(position >= treshold) {
                return type;
            }
        }

        return null;
    }

    public IIslandType getIslandType(String name) {
        return islandTypes.get(name);
    }
}
