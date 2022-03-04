package bedwarsboys.badwars;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The WorldBlockArea class is meant for saving, loading and manipulating a selected cuboid area, saved in a 3d array.
 */
public class WorldBlockArea {
    VirtualBlock[][][] blocks;

    /**
     * @param blocks Initializes the Object with the given block array
     */
    public WorldBlockArea(VirtualBlock[][][] blocks) {
        this.blocks = blocks;
    }

    /**
     * Creates a WorldBlockArea event with all blocks between the two Locations given
     *
     * @param pos1 The first position
     * @param pos2 The second position
     */
    public WorldBlockArea(Location pos1, Location pos2) {

        Dimension[] vs = getSizeBeginEnd(pos1, pos2);
        Dimension size = vs[0];
        Dimension begin = vs[1];
        Dimension end = vs[2];
        //fill block array
        blocks = new VirtualBlock[size.x][size.y][size.z];
        for (int x = begin.x; x <= end.x; x++) {
            for (int y = begin.y; y <= end.y; y++) {
                for (int z = begin.z; z <= end.z; z++) {
                    VirtualBlock block = new VirtualBlock(new Location(pos1.getWorld(), x, y, z), pos1.getWorld().getBlockAt(x, y, z).getBlockData().clone());
                    //Bukkit.getLogger().info(x + "," + y + "," + z);
                    blocks[x - begin.x][y - begin.y][z - begin.z] = block;
                }
            }
        }
    }

    /**
     * WARNING: leaves the blocks array as null
     */
    public WorldBlockArea() {

    }

    public VirtualBlock[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(VirtualBlock[][][] blocks) {
        this.blocks = blocks;
    }

    /**
     * @return Returns a Vector array with the size, the smallest and the biggest point of the area
     */
    Dimension[] getSizeBeginEnd(Location pos1, Location pos2) {
        // get area size
        int sizeX = Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
        int sizeY = Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1;
        int sizeZ = Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
        Dimension size = new Dimension(sizeX, sizeY, sizeZ);
        // get begin pos
        int beginX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int beginY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int beginZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        Dimension begin = new Dimension(beginX, beginY, beginZ);
        // get end pos
        int endX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int endY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int endZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        Dimension end = new Dimension(endX, endY, endZ);
        return new Dimension[]{size, begin, end};
    }

    /**
     * Saves an area to a file, with all block information.
     *
     * @param fileName  Where to save the block information
     * @param overwrite If set to true, if the file already exists, it will be overwritten.
     * @return If saving the area worked or not. False if not.
     */
    public boolean saveBlockArea(String fileName, boolean overwrite) {
        try {
            File f = new File(Badwars.PLUGIN.getDataFolder().getCanonicalPath() + "/" + fileName);
            if (f.exists()) {
                if (!overwrite) {
                    Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Tried saving a file that already exists.");
                    return false;
                }
                if (!f.delete()) {
                    Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Error deleting file: " + fileName);
                    return false;
                }
            }
            if (!f.createNewFile() || !f.canWrite()) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Can't create or write to the file.");
                return false;
            }
            if (blocks.length == 0 || blocks[0].length == 0 || blocks[0][0].length == 0) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "The area is 0 blocks wide on one axis.");
                return false;
            }
            FileWriter writer = new FileWriter(f);
            for (VirtualBlock[][] v1 : blocks) {
                for (VirtualBlock[] v2 : v1) {
                    for (VirtualBlock block : v2) {
                        if (block == null) {
                            Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "[WARNING] Block was null");
                            continue;
                        }
                        Location l = block.location;
                        String w = l.getWorld().getName();
                        int x = l.getBlockX();
                        int y = l.getBlockY();
                        int z = l.getBlockZ();
                        String d = block.blockData.getAsString();
                        String m = block.blockData.getMaterial().toString();
                        // 0-2=xyz 3=world 4=data 5=material
                        String data = x + ";" + y + ";" + z + ";" + w + ";" + d + ";" + m + System.lineSeparator();
                        //Bukkit.getLogger().info(data);
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

    public boolean loadBlockArea(String fileName) {
        try {
            File f = new File(Badwars.PLUGIN.getDataFolder().getCanonicalPath() + "/" + fileName);
            if (!f.exists()) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "File does not exist: " + fileName);
                return false;
            }
            if (!f.canRead()) {
                Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Can't read file.");
                return false;
            }
            // Read the blocks from the file
            Scanner reader = new Scanner(f);
            // First add all Blocks to a 1d list
            ArrayList<String> blockList = new ArrayList<>();
            while (reader.hasNextLine()) {
                blockList.add(reader.nextLine());
            }
            reader.close();
            // Now get the first and last block for dimensions
            String[] first = blockList.get(0).split(";");
            String[] last = blockList.get(blockList.size() - 1).split(";");
            Location pos1 = new Location(Bukkit.getWorld(first[3]),
                    Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]));
            Location pos2 = new Location(Bukkit.getWorld(last[3]),
                    Integer.parseInt(last[0]), Integer.parseInt(last[1]), Integer.parseInt(last[2]));
            Dimension[] vs = getSizeBeginEnd(pos1, pos2);
            //Bukkit.getLogger().info("pos1: " + pos1 + ", pos2: " + pos2);
            Dimension size = vs[0];
            Dimension begin = vs[1];
            Dimension end = vs[2];
            //Bukkit.getLogger().info("Size: " + size.x + "," + size.y + "," + size.z);
            // Next fill the array with the blocks
            blocks = new VirtualBlock[size.x][size.y][size.z];

            for (int i = 0; i < size.x; i++) {
                for (int j = 0; j < size.y; j++) {
                    for (int k = 0; k < size.z; k++) {
                        // Split the string of the line to extract the data stored
                        String[] bs = blockList.get(0).split(";");
                        blockList.remove(0);
                        Location l = new Location(Bukkit.getWorld(bs[3]),
                                Integer.parseInt(bs[0]), Integer.parseInt(bs[1]), Integer.parseInt(bs[2]));
                        BlockData data = Bukkit.createBlockData(bs[4]);
                        blocks[i][j][k] = new VirtualBlock(l, data);
                        //Bukkit.getLogger().info("Loaded block at " + l);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void pasteArea() {
        for (VirtualBlock[][] v1 : blocks) {
            for (VirtualBlock[] v2 : v1) {
                for (VirtualBlock block : v2) {
                    if (block == null) {
                        Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "[WARNING] Block was null");
                        continue;
                    }
                    Location l = block.location;
                    BlockData d = block.blockData;
                    //Bukkit.getLogger().info(l.toString() + " :: " + d.toString());
                    l.getBlock().setType(d.getMaterial());
                    l.getBlock().setBlockData(d);
                    if (d.getMaterial().data == Bed.class){
                        //Bukkit.getLogger().info("BETT");
                        BlockFace f = ((Bed)l.getBlock().getBlockData()).getFacing();
                        Bed.Part p = ((Bed)l.getBlock().getBlockData()).getPart();
                        if (p == Bed.Part.HEAD && (f == BlockFace.SOUTH || f == BlockFace.EAST)) {
                            //l.getBlock().setType(Material.BEDROCK);
                            l.getBlock().getRelative(f.getOppositeFace()).setType(d.getMaterial());
                            //l.getBlock().getRelative(f.getOppositeFace()).setBlockData(d);
                            Bed b = ((Bed)l.getBlock().getRelative(f.getOppositeFace()).getBlockData());
                            b.setFacing(f);
                            b.setPart(Bed.Part.FOOT);
                            l.getBlock().getRelative(f.getOppositeFace()).setBlockData(b);
                        }
                    }
                }
            }
        }
    }

    static class Dimension {
        public int x, y, z;

        public Dimension(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

    static class VirtualBlock {
        public Location location;
        public BlockData blockData;

        public VirtualBlock(Location location, BlockData blockData) {
            this.location = location;
            this.blockData = blockData;
        }
    }

}

