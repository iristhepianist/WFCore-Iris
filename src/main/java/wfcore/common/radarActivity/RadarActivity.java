package wfcore.common.radarActivity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import wfcore.api.utils.ChunkAssociatedData;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static wfcore.WFCore.dimensionWiseRadar;

public class RadarActivity {

    private static final Storage STORAGE = new Storage();

    private List<ChunkPos> radarList = new ArrayList<>(); // chunks left to process in this cycle
    private final Set<ChunkPos> radarChunks = new HashSet<>(); // a global list of all chunks with positive activity
    private int operationsPerTick = 0; // how much chunks should be processed in each cycle
    private static final short cycleLen = 1200;
    private final World world;
    private boolean blank = true;

    public RadarActivity(World world) {
        this.world = world;

    }


    private void tickActivityInWorld(int aTickID) {
        // gen data set
        if (aTickID == 0 || blank) {
            // make a snapshot of what to work on
            radarList = new ArrayList<>(radarChunks);
            // set operations per tick
            if (!radarList.isEmpty()) operationsPerTick = Math.max(1, radarList.size() / cycleLen);
            else operationsPerTick = 0; // SANity
            blank = false;
        }

        for (int chunksProcessed = 0; chunksProcessed < operationsPerTick; chunksProcessed++) {
            if (radarList.isEmpty()) break;
            ChunkPos actualPos = radarList.remove(radarList.size() - 1);
            // get activity
            int activity = STORAGE.get(world, actualPos).getAmount();
            // do something here if you want to have high activity give some other wacky effect
        }
    }

    private static RadarActivity getRadarManager(World world) {
        return dimensionWiseRadar.computeIfAbsent(world.provider.getDimension(), i -> new RadarActivity(world));
    }

    /** @see #addActivity(World, int, int, int) */
    public static void addActivity(Chunk ch, int aActivity) {
        addActivity(ch.getWorld(), ch.x, ch.z, aActivity);
    }

    /**
     * Add some activity to given chunk. Can pass in negative to remove activity. Will clamp the final activity
     * number to 0 if it would be changed into negative.
     *
     * @param w          world to modify. do nothing if it's a client world
     * @param chunkX     chunk coordinate X, i.e. blockX >> 4
     * @param chunkZ     chunk coordinate Z, i.e. blockZ >> 4
     * @param aActivity desired delta. Positive means the activity in chunk would go higher.
     */
    public static void addActivity(World w, int chunkX, int chunkZ, int aActivity) {
        if (aActivity == 0 || w.isRemote) return;
        mutateActivity(w, chunkX, chunkZ, d -> d.changeAmount(aActivity), null);
    }

    private static void mutateActivity(World world, int x, int z, Consumer<ChunkData> mutator,
                                        @Nullable Set<ChunkPos> chunks) {
        ChunkData data = STORAGE.get(world, x, z);
        boolean hadActivity = data.getAmount() > 0;
        mutator.accept(data);
        boolean hasActivity = data.getAmount() > 0;
        if (hasActivity != hadActivity) {
            if (chunks == null) chunks = getRadarManager(world).radarChunks;
            if (hasActivity) chunks.add(new ChunkPos(x, z));
            else chunks.remove(new ChunkPos(x, z));
        }
    }

    /** @see #getActivity(World, int, int) */
    public static int getActivity(TileEntity te) {
        return getActivity(te.getWorld(), te.getPos().getX() >> 4, te.getPos().getZ() >> 4);
    }

    /** @see #getActivity(World, int, int) */
    public static int getActivity(Chunk ch) {
        return getActivity(ch.getWorld(), ch.getPos().x, ch.getPos().z);
    }

    /**
     * Get the activity in specified chunk
     *
     * @param w      world to look in. can be a client world, but that limits the knowledge to what server side send us
     * @param chunkX chunk coordinate X, i.e. blockX >> 4
     * @param chunkZ chunk coordinate Z, i.e. blockZ >> 4
     * @return activity amount. may be 0 if it's a client world and server did not send us
     *         info about this chunk
     */
    public static int getActivity(World w, int chunkX, int chunkZ) {
        if (w.isRemote)
            // it really should be querying the client side stuff instead
            return 0;
        return STORAGE.get(w, chunkX, chunkZ)
                .getAmount();
    }

    public static boolean hasActivity(Chunk ch) {
        return STORAGE.isCreated(ch.getWorld(), ch.getPos()) && STORAGE.get(ch)
                .getAmount() > 0;
    }



    @ParametersAreNonnullByDefault
    private static final class Storage extends ChunkAssociatedData<ChunkData> {

        private Storage() {
            super("Activity", ChunkData.class, 64, (byte) 0, false);
        }

        @Override
        protected void writeElement(DataOutput output, ChunkData element, World world, int chunkX, int chunkZ)
                throws IOException {
            output.writeInt(element.getAmount());
        }


        @Override
        protected ChunkData readElement(DataInput input, int version, World world, int chunkX, int chunkZ)
                throws IOException {
            if (version != 0) throw new IOException("Region file corrupted");
            ChunkData data = new ChunkData(input.readInt());
            if (data.getAmount() > 0)
                getRadarManager(world).radarChunks.add(new ChunkPos(chunkX, chunkZ));
            return data;
        }

        @Override
        protected ChunkData createElement(World world, int chunkX, int chunkZ) {
            return new ChunkData();
        }

        @Override
        public void loadAll(World w) {
            super.loadAll(w);
        }

        public boolean isCreated(World world, ChunkPos coord) {
            return isCreated(world.provider.getDimension(), coord.x, coord.z);
        }
    }

    private static final class ChunkData implements ChunkAssociatedData.IData {

        public int amount;

        private ChunkData() {
            this(0);
        }

        private ChunkData(int amount) {
            this.amount = Math.max(0, amount);
        }

        /**
         * Current activity amount.
         */
        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = Math.max(amount, 0);
        }

        public void changeAmount(int delta) {
            this.amount = Math.max(safeInt(amount + (long) delta, 0), 0);
        }

        public static int safeInt(long number, int margin) {
            return number > Integer.MAX_VALUE - margin ? Integer.MAX_VALUE - margin : (int) number;
        }

        @Override
        public boolean isSameAsDefault() {
            return amount == 0;
        }
    }
}
