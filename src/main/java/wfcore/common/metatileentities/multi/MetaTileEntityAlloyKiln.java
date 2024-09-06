package wfcore.common.metatileentities.multi;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.CubeRendererState;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.cclop.ColourOperation;
import gregtech.client.renderer.cclop.LightMapOperation;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import wfcore.api.recipes.NoEnergyMultiblockController;
import wfcore.api.recipes.NoEnergyMultiblockRecipeLogic;
import wfcore.api.recipes.WFCoreRecipeMaps;

import java.util.List;

import static gregtech.api.unification.material.Materials.Lava;

public class MetaTileEntityAlloyKiln extends NoEnergyMultiblockController {

    int temp=30000;
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setInteger("temp", temp);
        return super.writeToNBT(data);
    }
    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        temp = data.getInteger("temp");
    }
    int cost;
    private static final TraceabilityPredicate SNOW_PREDICATE = new TraceabilityPredicate(
            bws -> GTUtility.isBlockSnow(bws.getBlockState()));

    public MetaTileEntityAlloyKiln(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, WFCoreRecipeMaps.ALLOY_KILN_RECIPES);
        this.recipeMapWorkable = new AKLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityAlloyKiln(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "#X#")
                .aisle("XXX", "X&X", "#X#")
                .aisle("XXX", "XYX", "#X#")
                .where('X', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.PRIMITIVE_BRICKS))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS).setMinGlobalLimited(1))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMinGlobalLimited(1))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMinGlobalLimited(1)))
                .where('#', any())
                .where('&', air())
                // running
                .where('Y', selfPredicate())
                .build();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.PRIMITIVE_BRICKS;
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("Happy Lava", new Object[0]));
        tooltip.add(I18n.format("gtsteam.machine.ak.tooltip.1"));
        tooltip.add(I18n.format("gtsteam.machine.ak.tooltip.2"));
        tooltip.add(I18n.format("gtsteam.machine.ak.tooltip.3"));
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed()) {
            textList.add(new TextComponentTranslation("gtsteam.multiblock.ak.amount",temp/100,1000));
            textList.add(new TextComponentTranslation("gtsteam.multiblock.ak.level", 10*cost));
            if (getInputFluidInventory() != null) {
                FluidStack STACK = getInputFluidInventory().drain(Lava.getFluid(Integer.MAX_VALUE), false);
                int liquidOxygenAmount = STACK == null ? 0 : STACK.amount;
                textList.add(new TextComponentTranslation("gtsteam.multiblock.ak.amount", TextFormattingUtil.formatNumbers((liquidOxygenAmount))));
            }

        }
    }

    @Override
    protected void addWarningText(List<ITextComponent> textList) {
        super.addWarningText(textList);
        if (isStructureFormed()) {
            if (temp<=50000) {
                textList.add(new TextComponentTranslation("gtqtcore.multiblock.ak.temp"));
            }
        }
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(),
                recipeMapWorkable.isActive(), recipeMapWorkable.isWorkingEnabled());
        if (recipeMapWorkable.isActive() && isStructureFormed()) {
            EnumFacing back = getFrontFacing().getOpposite();
            Matrix4 offset = translation.copy().translate(back.getXOffset(), -0.3, back.getZOffset());
            CubeRendererState op = Textures.RENDER_STATE.get();
            Textures.RENDER_STATE.set(new CubeRendererState(op.layer, CubeRendererState.PASS_MASK, op.world));
            Textures.renderFace(renderState, offset,
                    ArrayUtils.addAll(pipeline, new LightMapOperation(240, 240), new ColourOperation(0xFFFFFFFF)),
                    EnumFacing.UP, Cuboid6.full, TextureUtils.getBlockTexture("lava_still"),
                    BloomEffectUtil.getEffectiveBloomLayer());
            Textures.RENDER_STATE.set(op);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.PRIMITIVE_BLAST_FURNACE_OVERLAY;
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeInt(this.temp);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.temp = buf.readInt();
    }
    protected class AKLogic extends NoEnergyMultiblockRecipeLogic {

        FluidStack HEAT_STACK = Lava.getFluid(1);

        @Override
        public void update() {
            super.update();
            if (temp<=100000) {
                IMultipleTankHandler inputTank = getInputFluidInventory();
                if (HEAT_STACK.isFluidStackIdentical(inputTank.drain(HEAT_STACK, false))) {
                    inputTank.drain(HEAT_STACK, true);
                    temp = temp + 400;
                }
            }
            if(temp>80000){temp=temp-2;cost=4;}
            else if(temp>60000){temp=temp-2;cost=3;}
            else if(temp>40000){temp=temp-2;cost=2;}
            else if(temp>30000){temp=temp-2;cost=1;}
        }

        public void setMaxProgress(int maxProgress) {
            this.maxProgressTime = maxProgress*(100-cost*20)/100;

        }

        protected void updateRecipeProgress() {
            if (canRecipeProgress) {
                if (temp > 50000) {
                   // if(++progressTime%3==0)maxProgressTime=maxProgressTime-cost;
                    if (++progressTime > maxProgressTime) {
                        completeRecipe();
                    }
                }

            }
        }
        public AKLogic(NoEnergyMultiblockController tileEntity) {
            super(tileEntity, tileEntity.recipeMap);
        }
    }
}