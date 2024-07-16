package wfcore.common.metatileentities.multi.electric;



import gregtech.api.GTValues;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityEnergyHatch;
import gregtech.common.metatileentities.storage.MetaTileEntityDrum;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import wfcore.WFCore;

import java.util.function.Function;

import static gregtech.common.metatileentities.MetaTileEntities.*;

public class WFCoreMetaTileEntities {

    public static MetaTileEntityProjector PROJECTOR;

    public static void init() {
        //Multis
        PROJECTOR = registerMetaTileEntity(10001, new MetaTileEntityProjector(location("projector")));

    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation(WFCore.MODID, name);
    }

    /*public static void registerWFCoreSimpleMetaTileEntity(GCYWSimpleMachineMetaTileEntity[] machines, int startId, int maxTier, String name, RecipeMap<?> map, ICubeRenderer texture, boolean hasFrontFacing,  Function<String, ResourceLocation> resourceId, Function<Integer, Integer> tankScalingFunction) {
        for(int i = 0; i < maxTier; ++i) {
            String voltageName = GTValues.VN[i + 1].toLowerCase();
            machines[i + 1] = registerMetaTileEntity(startId + i, new WFCoreSimpleMachineMetaTileEntity(resourceId.apply(String.format("%s.%s", name, voltageName)), map, texture, i + 1, hasFrontFacing, tankScalingFunction));
        }

    }*/



    static {
    }
}