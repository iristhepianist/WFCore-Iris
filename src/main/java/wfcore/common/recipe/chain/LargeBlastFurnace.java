package wfcore.common.recipe.chain;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;

import static gregtech.api.unification.ore.OrePrefix.ingot;
import static gregtech.api.unification.ore.OrePrefix.wireGtSingle;
import static wfcore.api.recipes.WFCoreRecipeMaps.Large_Blast_Furnace;
import static wfcore.api.recipes.WFCoreRecipeMaps.Steam_PCB_Factory_Recipes;


public class LargeBlastFurnace {
    public static void init() {
        Large_Blast_Furnace.recipeBuilder()
                .input(OrePrefix.gem, Materials.Coal, 2)
                .input(OrePrefix.ingot, Materials.Iron)
                .fluidOutputs(Materials.Steel.getFluid(144))
                .duration(200)
                .buildAndRegister();
            }
}
