package org.dave.islandquests.islands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.dave.islandquests.api.IIslandType;
import org.dave.islandquests.utility.Logz;

import java.util.*;

public class IslandType implements IIslandType {
    public String name;

    public Biome biome;
    public int minimumYLevel;
    public int yOffsetRange;

    public double weight;

    public Map<IBlockState, Float> oreWeights;

    public IBlockState topBlock;
    public IBlockState fillerBlock;
    public IBlockState bedrockBlock;

    public List<DecorateBiomeEvent.Decorate.EventType> blockedDecorationTypes = new ArrayList<>();

    public IslandType(String name) {
        this.name = name;
        this.biome = Biomes.PLAINS;
        this.minimumYLevel = 60;
        this.yOffsetRange = 40;
        this.weight = 100.0d;

        this.oreWeights = new HashMap<>();
    }

    public void setBiome(String biomeName) {
        Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
        if(biome == null) {
            Logz.warn("Could not find biome with name: %s", biomeName);
            Logz.warn("Available biomes are: %s", ForgeRegistries.BIOMES.getKeys().toArray());
            return;
        }

        setBiome(biome);
    }

    public int getRandomYOffset(Random rand) {
        return rand.nextInt(this.yOffsetRange);
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public void setMinimumYLevel(int minimumYLevel) {
        this.minimumYLevel = minimumYLevel;
    }

    public void setRangeYOffset(int yOffsetRange) {
        this.yOffsetRange = yOffsetRange;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @SuppressWarnings("deprecation")
    public void addOreSpawn(String blockName, int meta, float weight) {
        Block ore = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        if(ore == null) {
            Logz.warn("There is no '%s', skipping its ore generation for island '%s'", blockName, name);
            return;
        }

        IBlockState state = ore.getStateFromMeta(meta);
        oreWeights.put(state, weight);
    }

    @SuppressWarnings("deprecation")
    public void setTopBlock(String blockName, int meta) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        if(block == null) {
            Logz.warn("There is no '%s', can not set top block for island '%s'", blockName, name);
            return;
        }

        IBlockState state = block.getStateFromMeta(meta);
        this.topBlock = state;
    }

    @SuppressWarnings("deprecation")
    public void setFillerBlock(String blockName, int meta) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        if(block == null) {
            Logz.warn("There is no '%s', can not set filler block for island '%s'", blockName, name);
            return;
        }

        IBlockState state = block.getStateFromMeta(meta);
        this.fillerBlock = state;
    }

    @SuppressWarnings("deprecation")
    public void setBedrockBlock(String blockName, int meta) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        if(block == null) {
            Logz.warn("There is no '%s', can not set bedrock block for island '%s'", blockName, name);
            return;
        }

        IBlockState state = block.getStateFromMeta(meta);
        this.bedrockBlock = state;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Biome getBiome() {
        return biome;
    }

    @Override
    public IBlockState getTopBlock() {
        return topBlock != null ? topBlock : biome.topBlock;
    }

    @Override
    public IBlockState getFillerBlock() {
        return fillerBlock != null ? fillerBlock : biome.fillerBlock;
    }

    @Override
    public IBlockState getBedrockBlock() {
        return bedrockBlock != null ? bedrockBlock : null;
    }

    @Override
    public Map<IBlockState, Float> getOreWeights() {
        return this.oreWeights;
    }

    public void blockDecorationType(String decoration) {
        DecorateBiomeEvent.Decorate.EventType type = DecorateBiomeEvent.Decorate.EventType.valueOf(decoration);
        if(type != null) {
            blockedDecorationTypes.add(type);
        }
    }
}
