package wfcore.common.metatileentities.multi.electric;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.List;


import static gregtech.api.util.RelativeDirection.*;
import static wfcore.api.recipes.WFCoreRecipeMaps.PROJECTOR_RECIPES;

public class MetaTileEntityProjector extends RecipeMapMultiblockController {
    public MetaTileEntityProjector(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PROJECTOR_RECIPES);
        this.recipeMapWorkable = new MultiblockRecipeLogic(this);
    }

    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityProjector(this.metaTileEntityId);
    }

    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCCCC")
                .aisle("CCCCC")
                .aisle("CCCCC")
                .aisle("CCCCC")
                .aisle("CCSCC")
                .where('S', this.selfPredicate())
                .where('C', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID)).or(autoAbilities(true, true, true, true, true, true, false)))
                .build();
    }
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.SOLID_STEEL_CASING;
    }

    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("gregtech.machine.perfect_oc", new Object[0]));
    }

    @Nonnull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.BLAST_FURNACE_OVERLAY;
    }


}

