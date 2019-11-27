package mangomods.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class TBlock extends Block implements IHasModel {

    public TBlock(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName())));
        IHasModel.HAS_MODELS.add(this);
    }

    @Override
    public void registerModels() {
        Terraria.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
