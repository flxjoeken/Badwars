package bedwarsboys.badwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class LobbyManager {

    Location lobbyLocation;
    WorldBlockArea lobbyBackup;


    public LobbyManager() {
        loadLobbyLocation();
        backupLobbyLocation();
    }

    void loadLobbyLocation() {
        int x, y, z;
        String w;
        if (Badwars.CONFIG.isSet("lobby")) {
            x = Badwars.CONFIG.getInt("lobby.x");
            y = Badwars.CONFIG.getInt("lobby.y");
            z = Badwars.CONFIG.getInt("lobby.z");
            w = Badwars.CONFIG.getString("lobby.w");
            if (w != null) {
                if (Bukkit.getWorld(w) == null) {
                    return;
                }
                lobbyLocation = new Location(Bukkit.getWorld(w), x, y, z);
            }
        }
    }

    boolean backupLobbyLocation() {
        if (lobbyLocation == null) {
            Bukkit.getLogger().info("Lobby location not set.");
            loadLobbyLocation();
            if (lobbyLocation == null) return false;
        }
        lobbyBackup = new WorldBlockArea(lobbyLocation.clone().add(-4, -1, -4), lobbyLocation.clone().add(4, 5, 4));
        if (!lobbyBackup.saveBlockArea("lobby_area_orig", true)) {
            Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Couldn't backup the lobby area!");
        } else {
            //we were able to backup the area, perfect.
            return true;
        }
        return false;
    }

    public void createLobbyContainer() {
        if (lobbyLocation == null) {
            Bukkit.getLogger().info("Lobby location not set.");
            loadLobbyLocation();
            if (lobbyLocation == null) return;
        }
        if (!backupLobbyLocation()) {
            Bukkit.getLogger().info("Will not create container as backup of original area failed. Try reloading the plugin.");
            return;
        }
        Material b = Material.BARRIER;
        int minX = lobbyLocation.getBlockX() - 4;
        int minY = lobbyLocation.getBlockY() - 1;
        int minZ = lobbyLocation.getBlockZ() - 4;
        int maxX = lobbyLocation.getBlockX() + 4;
        int maxY = lobbyLocation.getBlockY() + 5;
        int maxZ = lobbyLocation.getBlockZ() + 4;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        lobbyLocation.getWorld().getBlockAt(x, y, z).setType(b);
                    } else {
                        lobbyLocation.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }

                }
            }
        }
    }

    public void removeLobbyContainer() {
        lobbyBackup.pasteArea();
    }

}
