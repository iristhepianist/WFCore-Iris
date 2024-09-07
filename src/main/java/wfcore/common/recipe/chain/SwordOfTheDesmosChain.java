package wfcore.common.recipe.chain;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;


import static wfcore.api.recipes.WFCoreRecipeMaps.PROJECTOR_RECIPES;
import static wfcore.api.recipes.WFCoreRecipeMaps.STEAM_WIREMILL_RECIPES;

public class SwordOfTheDesmosChain {
    public static void init() {
        PROJECTOR_RECIPES.recipeBuilder()
                .input(OrePrefix.dust, Materials.StainlessSteel)
                .outputs(OreDictUnifier.get(OrePrefix.gem, Materials.Diamond, 1))
                .duration(10000000)
                .EUt(32)
                .buildAndRegister();
    }
}
