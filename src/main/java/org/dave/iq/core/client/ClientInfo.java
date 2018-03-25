package org.dave.iq.core.client;

import org.dave.iq.api.IIClientInfo;
import org.dave.iq.core.islands.Island;

public class ClientInfo implements IIClientInfo {
    public static ClientInfo instance = new ClientInfo();

    Island currentIsland = null;

    public void setCurrentIsland(Island currentIsland) {
        this.currentIsland = currentIsland;
    }

    @Override
    public Island getCurrentIsland() {
        return this.currentIsland;
    }

    @Override
    public boolean isOnIsland() {
        return this.currentIsland != null;
    }
}
