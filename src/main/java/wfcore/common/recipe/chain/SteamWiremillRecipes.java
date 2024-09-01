package wfcore.common.recipe.chain;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;

import static gregtech.api.unification.ore.OrePrefix.wireFine;
import static gregtech.api.unification.ore.OrePrefix.wireGtSingle;
import static wfcore.api.recipes.WFCoreRecipeMaps.PROJECTOR_RECIPES;
import static wfcore.api.recipes.WFCoreRecipeMaps.STEAM_WIREMILL_RECIPES;

public class SteamWiremillRecipes {
    public static void init() {
        STEAM_WIREMILL_RECIPES.recipeBuilder()
                .input(OrePrefix.ingot, Materials.Copper)
                .outputs(OreDictUnifier.get(wireGtSingle, Materials.Copper, 2))
                .duration(200)
                .EUt(32)
                .buildAndRegister();
        STEAM_WIREMILL_RECIPES.recipeBuilder()
                .input(OrePrefix.ingot, Materials.Tin)
                .outputs(OreDictUnifier.get(wireGtSingle, Materials.Tin, 2))
                .duration(200)
                .EUt(32)
                .buildAndRegister();
        STEAM_WIREMILL_RECIPES.recipeBuilder()
                .input(OrePrefix.ingot, Materials.RedAlloy)
                .outputs(OreDictUnifier.get(wireGtSingle, Materials.RedAlloy, 2))
                .duration(200)
                .EUt(32)
                .buildAndRegister();
        STEAM_WIREMILL_RECIPES.recipeBuilder()
                .input(OrePrefix.ingot, Materials.Lead)
                .outputs(OreDictUnifier.get(wireGtSingle, Materials.Lead, 2))
                .duration(200)
                .EUt(32)
                .buildAndRegister();
        STEAM_WIREMILL_RECIPES.recipeBuilder()
                .input(wireGtSingle, Materials.Copper)
                .outputs(OreDictUnifier.get(wireFine, Materials.Copper, 2))
                .duration(200)
                .EUt(32)
                .buildAndRegister();

            }
}
