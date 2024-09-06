package wfcore.common.recipe.chain;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import wfcore.common.block.WFCoreMetaBlocks;


import static gregtech.api.GTValues.L;
import static gregtech.api.recipes.RecipeMaps.ASSEMBLER_RECIPES;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.blocks.MetaBlocks.OPTICAL_PIPES;
import static wfcore.api.recipes.WFCoreRecipeMaps.PROJECTOR_RECIPES;
import static wfcore.api.recipes.WFCoreRecipeMaps.STEAM_WIREMILL_RECIPES;
import static wfcore.common.block.blocks.WFCoreComputerParts.CasingType.COMPUTER_VENT;

public class SwordOfTheDesmosChain {
    public static void init() {
        PROJECTOR_RECIPES.recipeBuilder()
                .input(OrePrefix.dust, Materials.StainlessSteel)
                .outputs(OreDictUnifier.get(OrePrefix.gem, Materials.Diamond, 1))
                .duration(10000000)
                .EUt(32)
                .buildAndRegister();

        /*ASSEMBLER_RECIPES.recipeBuilder()
                .input(frameGt,Steel, 4)
                .input(plate, Aluminium, 4)
                .input(OPTICAL_PIPES[0], 2)
                .input(plateDense, Aluminium, 4)
                .fluidInputs(Polytetrafluoroethylene.getFluid(L*8))
                .outputs( WFCoreMetaBlocks.ComputerParts.getItemVariant(COMPUTER_VENT))
                .duration(1000).EUt(120).buildAndRegister();*/

    }
}
