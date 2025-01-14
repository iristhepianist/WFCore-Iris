package wfcore.common.recipe.chain;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;

import static gregtech.api.unification.ore.OrePrefix.wireFine;
import static gregtech.api.unification.ore.OrePrefix.wireGtSingle;
import static wfcore.api.recipes.WFCoreRecipeMaps.Steam_PCB_Factory_Recipes;


public class CircuitFabricatorRecipes {
    public static void init() {
        Steam_PCB_Factory_Recipes.recipeBuilder()
                .input(OrePrefix.ingot, Materials.Copper)
                .outputs(OreDictUnifier.get(wireGtSingle, Materials.Copper, 2))
                .duration(200)
                .EUt(16)
                .buildAndRegister();
            }
}
