package wfcore.common.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.FluidPipeProperties;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.GTValues;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.properties.BlastProperty.GasTier;

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static gregtech.api.util.GTUtility.gregtechId;
import static gregtech.api.GTValues.MV;
import static gregtech.api.GTValues.V;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.util.GTUtility.gregtechId;
import static wfcore.common.materials.WFCoreMaterials.*;
public class FirstDegreeMaterials {
    public static void register() {
        Dirt = new Material.Builder(700, gregtechId("dirt"))
                .color(0x732602)
                .cableProperties(0, 0, 0, true)
                .build();
        Brick.setProperty(PropertyKey.FLUID_PIPE, new FluidPipeProperties(2200, 20, false, false, false, false));
        AdvancedAlloy = new Material.Builder(701, gregtechId("Advanced_Alloy"))
                .cableProperties(GTValues.V[GTValues.LV], 4, 0, true)
                .color(0xE24207).ingot()
                .liquid(new FluidBuilder().temperature(1373))
                .iconSet(METALLIC)
                .flags(STD_METAL, GENERATE_LONG_ROD, GENERATE_FINE_WIRE, GENERATE_SPRING, GENERATE_FOIL, GENERATE_FRAME,
                        GENERATE_DOUBLE_PLATE)
                .fluidPipeProperties(1200, 40, true)
                .blast(b -> b.temp(1373, GasTier.LOW).blastStats(VA[MV], 200))
                .build();
    }
    /*
    *public static final Material Dirt = new Material.Builder(10001, gregtechId("dirt"))
            .ingot()
                .liquid(new FluidBuilder().temperature(1811))
            .plasma()
                .ore()
                .color(0xC8C8C8).iconSet(METALLIC)
                .flags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_GEAR,
                       GENERATE_SPRING_SMALL, GENERATE_SPRING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                       BLAST_FURNACE_CALCITE_TRIPLE)
                .element(Elements.Fe)
                .toolStats(ToolProperty.Builder.of(2.0F, 2.0F, 256, 2)
                        .enchantability(14).build())
            .rotorStats(7.0f, 2.5f, 256)

                .build();*/
}
