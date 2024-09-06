package wfcore;


import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import wfcore.CommonProxy;
import wfcore.common.block.WFCoreMetaBlocks;

@Mod.EventBusSubscriber({Side.CLIENT})
public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    public void construction() {
    }
    public void preInit() {
    }
    public void init() {
    }

    public void postInit() {
    }

    public void loadComplete() {
    }

    public void preLoad()
    {
        super.preLoad();
    }
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        WFCoreMetaBlocks.registerItemModels();
        System.out.println("Hello World!");
    }
    private long clientTick = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }
}
