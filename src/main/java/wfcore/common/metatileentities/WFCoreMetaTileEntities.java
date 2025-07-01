package wfcore.common.metatileentities;




import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import wfcore.common.metatileentities.multi.MetaTileEntityRadar;
import wfcore.common.metatileentities.multi.primitive.MetaTileEntityStrandCaster;
import wfcore.common.metatileentities.multi.primitive.MetaTileEntityWarfactoryBlastFurnace;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import gregtech.api.util.GTLog;
import wfcore.common.metatileentities.multi.MetaTileEntityBoilerTower;
import wfcore.common.metatileentities.multi.electric.*;
import wfcore.common.metatileentities.multi.steam.*;

import java.util.function.Function;

import static gregtech.api.util.GTUtility.gregtechId;
import static gregtech.common.metatileentities.MetaTileEntities.*;

public class WFCoreMetaTileEntities {
    public static MetaTileEntityProjector PROJECTOR;
    public static MetaTileEntitySteamWiremill STEAMWIREMILL;
    public static MetaTileEntityBoilerTower BOILERTOWER;
    public static SteamPCBFactory STEAM_PCB_FACTORY_STEEL;
    public static MetaTileEntityWarfactoryBlastFurnace LARGEBLASTFURNACE;
    public static MetaTileEntityStrandCaster STRANDCASTER;
    public static MetaTileEntityRadar RADAR;


    public static int id = 0;

    public static void init() {
        //Multis
        PROJECTOR = registerMetaTileEntity(id++, new MetaTileEntityProjector(location("projector")));
        BOILERTOWER = registerMetaTileEntity(id++, new MetaTileEntityBoilerTower(location("boilertower")));
        LARGEBLASTFURNACE = registerMetaTileEntity(id++, new MetaTileEntityWarfactoryBlastFurnace(location("largeblastfurnace")));
        STRANDCASTER = registerMetaTileEntity(id++, new MetaTileEntityStrandCaster(location("strandcaster")));
        RADAR = registerMetaTileEntity(id++, new MetaTileEntityRadar((location("radar"))));

        GTLog.logger.info("Who the fuck reads these lmaoooo");
        STEAM_PCB_FACTORY_STEEL = registerMetaTileEntity(16000,
                new SteamPCBFactory(gregtechId("steam_pcb_factory"), true));
    }


    private static ResourceLocation location(@NotNull String name) {
        return new ResourceLocation("wfcore", name);
    }

//      public static void registerWFCoreSimpleMetaTileEntity(GCYWSimpleMachineMetaTileEntity[] machines, int startId, int maxTier, String name, RecipeMap<?> map, ICubeRenderer texture, boolean hasFrontFacing, Function<String, ResourceLocation> resourceId, Function<Integer, Integer> tankScalingFunction) {
//        for(int i = 0; i < maxTier; ++i) {
//            String voltageName = GTValues.VN[i + 1].toLowerCase();
//            machines[i + 1] = registerMetaTileEntity(startId + i, new WFCoreSimpleMachineMetaTileEntity(resourceId.apply(String.format("%s.%s", name, voltageName)), map, texture, i + 1, hasFrontFacing, tankScalingFunction));
//        }
//
//    }


}