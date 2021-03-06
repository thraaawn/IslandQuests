package org.dave.iq.core.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import org.dave.iq.api.IIslandType;
import org.dave.iq.core.islands.Island;
import org.dave.iq.core.islands.IslandRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidIslandsChunkGenerator implements IChunkGenerator {
    private final World world;
    private final VoidIslandsTerrainGenerator terrainGen;

    public Random rand;

    public VoidIslandsChunkGenerator(World world) {
        this.rand = new Random(world.getSeed());

        this.world = world;
        this.terrainGen = new VoidIslandsTerrainGenerator(world, this);
        VoidIslandsDimensionRegistry.instance.registerTerrainGen(world, this.terrainGen);
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkZ) {
        ChunkPrimer cp = new ChunkPrimer();

        this.terrainGen.generate(chunkX, chunkZ, cp);

        Chunk chunk = new Chunk(this.world, cp, chunkX, chunkZ);

        final byte[] biomeArray = new byte[256];
        if(IslandRegistry.instance.hasIsland(chunkX, chunkZ)) {
            Island island = IslandRegistry.instance.getIsland(chunkX, chunkZ);
            IIslandType islandType = island.getIslandType();
            Arrays.fill(biomeArray, (byte) Biome.getIdForBiome(islandType.getBiome()));
        } else {
            Arrays.fill(biomeArray, (byte) Biome.getIdForBiome(Biomes.VOID));
        }

        chunk.setBiomeArray(biomeArray);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {
        if(!IslandRegistry.instance.hasIsland(chunkX, chunkZ)) {
            return;
        }
        Island island = IslandRegistry.instance.getIsland(chunkX, chunkZ);
        IIslandType islandType = island.getIslandType();

        islandType.getBiome().decorate(this.world, this.rand, new BlockPos(chunkX << 4, islandType.getMinimumYLevel() + island.getHeightOffset(), chunkZ << 4));
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        if(!IslandRegistry.instance.hasIsland(chunkX, chunkZ)) {
            return ImmutableList.of();
        }

        Island island = IslandRegistry.instance.getIsland(chunkX, chunkZ);
        IIslandType islandType = island.getIslandType();

        return islandType.getBiome().getSpawnableList(creatureType);
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }

    // XXX: Maybe use this to place Structures on islands?
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }
}
