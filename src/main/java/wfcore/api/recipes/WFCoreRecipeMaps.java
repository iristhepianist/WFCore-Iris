package wfcore.api.recipes;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;

public class WFCoreRecipeMaps {
    public static final RecipeMap<SimpleRecipeBuilder> PROJECTOR_RECIPES = new RecipeMap<>("projector", 4, 4, 4, 4, new SimpleRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT, ProgressWidget.MoveType.HORIZONTAL)
            .setSound(GTSoundEvents.ASSEMBLER); //Temporary Sound

    public static final RecipeMap<SimpleRecipeBuilder> STEAM_WIREMILL_RECIPES = new RecipeMap<>("steamwiremill", 1, 1, 0, 0, new SimpleRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, ProgressWidget.MoveType.HORIZONTAL);
    public static final RecipeMap<SimpleRecipeBuilder> Large_Blast_Furnace = new RecipeMap<>("steamwiremill", 2, 1, 0, 1, new SimpleRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressWidget.MoveType.HORIZONTAL);
    public static final RecipeMap<SimpleRecipeBuilder> StrandCaster = new RecipeMap<>("steamwiremill", 1, 1, 2, 1, new SimpleRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, ProgressWidget.MoveType.HORIZONTAL);
}




