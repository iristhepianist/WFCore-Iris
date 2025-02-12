package wfcore.api.recipes;


import gregtech.api.gui.GuiTextures;
import gregtech.api.recipes.RecipeMap;

import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

public class WFCoreRecipeMaps {

    public static final RecipeMap<SimpleRecipeBuilder> PROJECTOR_RECIPES = new RecipeMapBuilder<>("projector",
            new SimpleRecipeBuilder())
            .itemInputs(3)
            .itemOutputs(3)
            .fluidInputs(5)
            .fluidOutputs(2)
            .progressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE)
            .build();

    public static final RecipeMap<SimpleRecipeBuilder> STEAM_WIREMILL_RECIPES = new RecipeMapBuilder<>("steam_wiremill",
            new SimpleRecipeBuilder())
            .itemInputs(1)
            .itemOutputs(1)
            .fluidInputs(0)
            .fluidOutputs(0)
            .progressBar(GuiTextures.PROGRESS_BAR_WIREMILL)
            .build();
    public static final RecipeMap<PrimitiveRecipeBuilder> Large_Blast_Furnace = new RecipeMapBuilder<>("large_blast_furnace",
            new PrimitiveRecipeBuilder())
            .itemInputs(2)
            .itemOutputs(1)
            .fluidInputs(0)
            .fluidOutputs(1)
            .progressBar(GuiTextures.PROGRESS_BAR_ARROW)
            .build();
    public static final RecipeMap<PrimitiveRecipeBuilder> StrandCaster = new RecipeMapBuilder<>("strand_caster",
            new PrimitiveRecipeBuilder())
            .itemInputs(1)
            .itemOutputs(1)
            .fluidInputs(2)
            .fluidOutputs(1)
            .progressBar(GuiTextures.PROGRESS_BAR_ARROW)
            .build();
}
