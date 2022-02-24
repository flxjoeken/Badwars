package bedwarsboys.badwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorldBlockArea {
    Block[][][] blocks;

    public WorldBlockArea(Block[][][] blocks) {
        this.blocks = blocks;
    }

    public WorldBlockArea(Location pos1, Location pos2) {
        // get area size
        int sizeX = Math.abs(pos1.getBlockX() - pos2.getBlockX());
        int sizeY = Math.abs(pos1.getBlockY() - pos2.getBlockY());
        int sizeZ = Math.abs(pos1.getBlockZ() - pos2.getBlockZ());
        // get begin pos
        int beginX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int beginY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int beginZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        // get end pos
        int endX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int endY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int endZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        //fill block array
        blocks = new Block[sizeX][sizeY][sizeZ];
        for (int x = beginX; x < endX; x++) {
            for (int y = beginY; y < endY; y++) {
                for (int z = beginZ; z < endZ; z++) {
                    blocks[x - beginX][y - beginY][z - beginZ] = pos1.getWorld().getBlockAt(x, y, z);
                }
            }
        }
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][][] blocks) {
        this.blocks = blocks;
    }

    public boolean saveBlockArea(String fileName, boolean overwrite) {
        try {
            File f = new File(Badwars.PLUGIN.getDataFolder().getCanonicalPath() + "/" + fileName);
            if (f.exists()) {
                if (!overwrite) {
                    Bukkit.getLogger().info(Badwars.PLUGIN_NAME+"Tried saving a file that already exists.");
                    return false;
                }
                if (!f.delete()) {
                    Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Error deleting file: " + fileName);
                    return false;
                }
            }
            if (!f.createNewFile() || !f.canWrite()) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME+"Can't create or write to the file.");
                return false;
            }
            if (blocks.length == 0 || blocks[0].length == 0 || blocks[0][0].length == 0) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME+"The area is 0 blocks wide on one axis.");
                return false;
            }
            FileWriter writer = new FileWriter(f);
            for (Block[][] block1 : blocks) {
                for (Block[] block2 : block1) {
                    for (Block block : block2) {
                        if (block == null) {
                            Bukkit.getLogger().info(Badwars.PLUGIN_NAME+"[WARNING] Block was null");
                            continue;
                        }
                        Location l = block.getLocation();
                        int x = l.getBlockX();
                        int y = l.getBlockY();
                        int z = l.getBlockZ();
                        String d = block.getBlockData().getAsString();
                        String m = block.getBlockData().getMaterial().toString();

                        String data = x + ";" + y + ";" + z + ";" + d + ";" + m + System.lineSeparator();
                        writer.write(data);
                    }
                }
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
