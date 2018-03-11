package org.dave.islandquests.islands;

import net.minecraft.nbt.NBTTagCompound;
import org.dave.islandquests.api.IIslandChunk;

public class IslandChunk implements IIslandChunk {
    private int chunkX;
    private int chunkZ;
    private boolean processed;
    private IslandType islandType;
    private int heightOffset;

    public IslandChunk(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.processed = false;
    }

    @Override
    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public IslandType getIslandType() {
        return islandType;
    }

    public void setIslandType(IslandType islandType) {
        this.islandType = islandType;
    }

    public void setHeightOffset(int heightOffset) {
        this.heightOffset = heightOffset;
    }

    @Override
    public int getHeightOffset() {
        return heightOffset;
    }

    @Override
    public int getChunkX() {
        return chunkX;
    }

    @Override
    public int getChunkZ() {
        return chunkZ;
    }

    public NBTTagCompound createTagCompound() {
        NBTTagCompound result = new NBTTagCompound();
        result.setString("type", islandType.name);
        result.setInteger("x", chunkX);
        result.setInteger("z", chunkZ);
        result.setInteger("y", heightOffset);

        return result;
    }

    public static IslandChunk newFromTagCompound(NBTTagCompound tag) {
        IslandChunk result = new IslandChunk(tag.getInteger("x"), tag.getInteger("z"));
        result.setProcessed(true);
        result.setIslandType(IslandTypeRegistry.instance.getIslandType(tag.getString("type")));
        result.setHeightOffset(tag.getInteger("y"));

        return result;
    }
}
