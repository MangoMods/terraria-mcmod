package mangomods.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entityLivingBase = event.getEntityLiving();

        // Drop half of currency if keepInventory is true (akin to Softcore)
        // Otherwise everything is dropped anyways
        if(entityLivingBase instanceof EntityPlayer && entityLivingBase.getEntityWorld().getGameRules().getBoolean("keepInventory")) {
            EntityPlayer player = (EntityPlayer) entityLivingBase;

            for(ItemStack itemStack : player.inventory.mainInventory) {
                // Arrow is a placeholder for currency
                if(itemStack.getItem() == Items.ARROW) {
                    int loss = MathHelper.ceil(itemStack.getCount() / 2.0);
                    itemStack.shrink(loss);
                    player.dropItem(Items.ARROW, loss);
                }
            }
        }
    }
}
