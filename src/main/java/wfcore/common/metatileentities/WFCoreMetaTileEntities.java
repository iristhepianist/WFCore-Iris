package wfcore.common.metatileentities;



import net.minecraft.util.ResourceLocation;
import wfcore.WFCore;
import wfcore.common.metatileentities.multi.electric.MetaTileEntityProjector;
import wfcore.common.metatileentities.multi.steam.*;

import static gregtech.common.metatileentities.MetaTileEntities.*;

public class WFCoreMetaTileEntities {

    public static MetaTileEntityProjector PROJECTOR;
    public static MetaTileEntityLargeSteamForgeHammer LARGESTEAMFORGEHAMMER;
    public static MetaTileEntityLargeSteamCompressor LARGESTEAMCOMPRESSOR;
    public static MetaTileEntityLargeSteamWasher LARGESTEAMWASHER;
    public static MetaTileEntityLargeSteamCentrifuge LARGESTEAMCENTRIFUGE;
    public static MetaTileEntityLargeSteamMixer LARGESTEAMMIXER;





    public static void init() {
        //Multis
        PROJECTOR = registerMetaTileEntity(10001, new MetaTileEntityProjector(location("projector")));
        LARGESTEAMFORGEHAMMER = registerMetaTileEntity(10002, new MetaTileEntityLargeSteamForgeHammer(location("largesteamforgehammer")));
        LARGESTEAMCOMPRESSOR = registerMetaTileEntity(10003, new MetaTileEntityLargeSteamCompressor(location("largesteamcompressor")));
        LARGESTEAMWASHER = registerMetaTileEntity(10004, new MetaTileEntityLargeSteamWasher(location("largesteamwasher")));
        LARGESTEAMCENTRIFUGE = registerMetaTileEntity(10005, new MetaTileEntityLargeSteamCentrifuge(location("largesteamcentrifuge")));
        LARGESTEAMMIXER = registerMetaTileEntity(10006, new MetaTileEntityLargeSteamMixer(location("largesteammixer")));


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