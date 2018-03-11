package org.dave.islandquests.islands;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.ChunkPos;
import org.dave.islandquests.api.IIsland;

import java.util.ArrayList;
import java.util.List;

public class Island implements IIsland {
    List<ChunkPos> chunks;
    private IslandType islandType;
    private int heightOffset;

    public Island(IslandType islandType, int heightOffset) {
        this.chunks = new ArrayList<>();
        this.islandType = islandType;
        this.heightOffset = heightOffset;
    }

    public Island(NBTTagCompound compound) {
        this.chunks = new ArrayList<>();

        this.islandType = IslandTypeRegistry.instance.getIslandType(compound.getString("type"));
        this.heightOffset = compound.getInteger("height");
        int[] chunkArray = compound.getIntArray("chunks");
        for(int i = 0; i < chunkArray.length; i+=2) {
            this.chunks.add(new ChunkPos(chunkArray[i], chunkArray[i+1]));
        }
    }

    @Override
    public IslandType getIslandType() {
        return this.islandType;
    }

    @Override
    public List<ChunkPos> getIslandChunks() {
        return this.chunks;
    }

    public int getHeightOffset() {
        return heightOffset;
    }

    public void setChunks(List<ChunkPos> chunks) {
        this.chunks = chunks;
    }

    public NBTTagCompound createTagCompound() {
        NBTTagCompound result = new NBTTagCompound();
        result.setString("type", islandType.name);
        result.setInteger("height", heightOffset);

        List<Integer> serialChunkPos = new ArrayList<>();
        for(ChunkPos pos : chunks) {
            serialChunkPos.add(pos.x);
            serialChunkPos.add(pos.z);
        }

        result.setTag("chunks", new NBTTagIntArray(serialChunkPos));
        return result;
    }
}