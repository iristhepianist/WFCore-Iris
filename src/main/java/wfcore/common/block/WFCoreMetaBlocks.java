package wfcore.common.block;

import gregtech.common.blocks.MetaBlocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wfcore.Tags;
import wfcore.common.block.blocks.BlockWFMetalCasing;
import wfcore.common.block.blocks.WFCoreComputerParts;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class WFCoreMetaBlocks {

    public static WFCoreComputerParts COMPUTERPARTS;
   // public static BlockWFMetalCasing WFMETALCASING;

    private WFCoreMetaBlocks() {}
    public static void init() {
        COMPUTERPARTS = new WFCoreComputerParts();
        COMPUTERPARTS.setRegistryName("computer_part");
        //WFMETALCASING = new BlockWFMetalCasing();
       // WFMETALCASING.setRegistryName("wf_metal_casing");
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels() {
        registerItemModel(COMPUTERPARTS);
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Block block) {

        for (IBlockState state : block.getBlockState().getValidStates()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
                    block.getMetaFromState(state),
                    new ModelResourceLocation(block.getRegistryName(),
                            MetaBlocks.statePropertiesToString(state.getProperties())));
        }

    }

}
