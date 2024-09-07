package wfcore.common.metatileentities;



import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import wfcore.WFCore;
import wfcore.common.metatileentities.multi.MetaTileEntityBoilerTower;
import wfcore.common.metatileentities.multi.electric.*;
import wfcore.common.metatileentities.multi.electric.computing.MetaTileEntityComputingServer;
import wfcore.common.metatileentities.multi.steam.*;

import static gregtech.common.metatileentities.MetaTileEntities.*;

public class WFCoreMetaTileEntities {

    public static MetaTileEntityProjector PROJECTOR;
    public static MetaTileEntityLargeSteamForgeHammer LARGESTEAMFORGEHAMMER;
    public static MetaTileEntityLargeSteamCompressor LARGESTEAMCOMPRESSOR;
    public static MetaTileEntityLargeSteamWasher LARGESTEAMWASHER;
    public static MetaTileEntityLargeSteamCentrifuge LARGESTEAMCENTRIFUGE;
    public static MetaTileEntityLargeSteamMixer LARGESTEAMMIXER;
    public static MetaTileEntitySteamWiremill STEAMWIREMILL;
    public static MetaTileEntityBoilerTower BOILERTOWER;
    public static MetaTileEntityComputingServer COMPUTINGSERVER;


    public static int id = 0;

    public static void init() {
        //Multis
        PROJECTOR = registerMetaTileEntity(id++, new MetaTileEntityProjector(location("projector")));
        LARGESTEAMFORGEHAMMER = registerMetaTileEntity(id++, new MetaTileEntityLargeSteamForgeHammer(location("largesteamforgehammer")));
        LARGESTEAMCOMPRESSOR = registerMetaTileEntity(id++, new MetaTileEntityLargeSteamCompressor(location("largesteamcompressor")));
        LARGESTEAMWASHER = registerMetaTileEntity(id++, new MetaTileEntityLargeSteamWasher(location("largesteamwasher")));
        LARGESTEAMCENTRIFUGE = registerMetaTileEntity(id++, new MetaTileEntityLargeSteamCentrifuge(location("largesteamcentrifuge")));
        LARGESTEAMMIXER = registerMetaTileEntity(id++, new MetaTileEntityLargeSteamMixer(location("largesteammixer")));
        STEAMWIREMILL = registerMetaTileEntity(id++, new MetaTileEntitySteamWiremill(location("steamwiremill")));
        BOILERTOWER = registerMetaTileEntity(id++, new MetaTileEntityBoilerTower(location("boilertower")));
        COMPUTINGSERVER = registerMetaTileEntity(id++, new MetaTileEntityComputingServer(location("computingserver")));
    }

    private static ResourceLocation location(@NotNull String name) {
        return new ResourceLocation("wfcore", name);
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