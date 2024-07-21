package wfcore.common.materials;

import gregtech.api.unification.material.Material;

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
