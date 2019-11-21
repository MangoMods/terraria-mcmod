package mangomods.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Terraria.MODID, useMetadata = true)
public class Terraria {

    public static final String MODID = "terraria";

    @Instance
    public static Terraria instance;

    @SidedProxy(clientSide = "mangomods.client.ClientProxy", serverSide = "mangomods.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }
}
