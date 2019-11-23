package mangomods.common;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mod.EventBusSubscriber
public class WorldProgression extends WorldSavedData {

    private static final String DATA_NAME = Terraria.MODID + "_WorldProgression";
    public boolean cowHasDied; // Progression data

    private WorldProgression() {
        super(DATA_NAME);
    }

    // Seems to be required for saving to NBT
    @SuppressWarnings("unused")
    public WorldProgression(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        cowHasDied = nbt.getBoolean("cowHasDied");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("cowHasDied", cowHasDied);
        return compound;
    }

    /**
     * Gets the progression for a world, creating and attaching a new one if one doesn't exist.
     * @param world - world to get the progression of
     * @return The world's progression.
     */
    public static WorldProgression get(World world) {
        MapStorage storage = Objects.requireNonNull(world.getMapStorage());
        WorldProgression instance = (WorldProgression) storage.getOrLoadData(WorldProgression.class, DATA_NAME);

        if(instance == null) {
            instance = new WorldProgression();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        WorldProgression.get(event.getWorld());
    }

    // Below are just events for testing
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof EntityCow) {
            EntityCow cow = (EntityCow) event.getEntityLiving();
            WorldProgression worldProgression = WorldProgression.get(cow.getEntityWorld());
            if(!worldProgression.cowHasDied) {
                worldProgression.cowHasDied = true;
                worldProgression.markDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDrops(PlayerDropsEvent event) {
        WorldProgression worldProgression = WorldProgression.get(event.getEntityPlayer().getEntityWorld());
        if(worldProgression.cowHasDied && event.getDrops().size() > 0) event.getDrops().remove(0);
    }
}
