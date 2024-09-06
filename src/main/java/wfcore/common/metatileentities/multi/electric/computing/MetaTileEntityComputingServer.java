package wfcore.common.metatileentities.multi.electric.computing;


import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.*;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.*;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.IProgressBarMultiblock;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.util.TextComponentUtil;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import wfcore.api.blocks.impl.WrappedIntTired;
import wfcore.api.predicate.TiredTraceabilityPredicate;
import wfcore.api.utils.GTQTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import static gregtech.api.unification.material.Materials.PCBCoolant;
import static gregtech.api.unification.material.Materials.Water;
//import static keqing.gtqtcore.api.unification.GTQTMaterials.LiquidNitrogen;

public class MetaTileEntityComputingServer extends MultiblockWithDisplayBase implements IOpticalComputationProvider, IProgressBarMultiblock {
    private boolean isActive;
    private boolean isWorkingEnabled;

    private IFluidHandler coolantHandler;

    float HOT;
    private int CPU1;
    private int GPU1;
    private int RAM1;

    private int CPU2;
    private int GPU2;
    private int RAM2;

    private int CPU3;
    private int GPU3;
    private int RAM3;
    private int CPU4;
    private int GPU4;
    private int RAM4;
    int thresholdPercentage=0;
    private IEnergyContainer energyContainer;


    @Override
    @Nonnull
    protected Widget getFlexButton(int x, int y, int width, int height) {
        WidgetGroup group = new WidgetGroup(x, y, width, height);
        group.addWidget(new ClickButtonWidget(0, 0, 9, 18, "", this::decrementThreshold)
                .setButtonTexture(GuiTextures.BUTTON_THROTTLE_MINUS)
                .setTooltipText("wfcore.multiblock.computingserver.threshold_decrement"));
        group.addWidget(new ClickButtonWidget(9, 0, 9, 18, "", this::incrementThreshold)
                .setButtonTexture(GuiTextures.BUTTON_THROTTLE_PLUS)
                .setTooltipText("wfcore.multiblock.computingserver.threshold_increment"));
        return group;
    }
    private void incrementThreshold(Widget.ClickData clickData) {
        this.thresholdPercentage = MathHelper.clamp(thresholdPercentage + 1, 0, 2);
    }

    private void decrementThreshold(Widget.ClickData clickData) {
        this.thresholdPercentage = MathHelper.clamp(thresholdPercentage - 1, 0, 2);
    }


    public MetaTileEntityComputingServer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        this.energyContainer = new EnergyContainerList(new ArrayList<>());
    }
    FluidStack COLD_STACK = Water.getFluid(10);
    FluidStack COLD_STACKA = PCBCoolant.getFluid(5);
   // FluidStack COLD_STACKB = LiquidNitrogen.getFluid(1);

    FluidStack COLD_STACK1 = Water.getFluid(40);
    FluidStack COLD_STACKA1 = PCBCoolant.getFluid(20);
    //FluidStack COLD_STACKB1 = LiquidNitrogen.getFluid(4);
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setFloat("HOT", HOT);
        data.setInteger("thresholdPercentage", thresholdPercentage);
        return super.writeToNBT(data);
    }
   
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        HOT = data.getFloat("HOT");
        thresholdPercentage = data.getInteger("thresholdPercentage");
    }
    @Override
    protected void updateFormedValid() {
        consumeEnergy();
        if(HOT>10) HOT=HOT-10;

        if(HOT>5000) {

            if(thresholdPercentage!=2) {
                if (COLD_STACK.isFluidStackIdentical(coolantHandler.drain(COLD_STACK, false))) {
                    coolantHandler.drain(COLD_STACK, true);
                    if(HOT>770) HOT = HOT - 770;
                }
                if (COLD_STACKA.isFluidStackIdentical(coolantHandler.drain(COLD_STACKA, false))) {
                    coolantHandler.drain(COLD_STACKA, true);
                    if(HOT>1440) HOT = HOT - 1440;
                }
                /*if (COLD_STACKB.isFluidStackIdentical(coolantHandler.drain(COLD_STACKB, false))) {
                    coolantHandler.drain(COLD_STACKB, true);
                    if(HOT>2880) HOT = HOT - 2880;
                }*/
            }

            if(thresholdPercentage==2) {
                if (COLD_STACK1.isFluidStackIdentical(coolantHandler.drain(COLD_STACK1, false))){
                    coolantHandler.drain(COLD_STACK1, true);
                    if(HOT>1440)HOT = HOT - 1440;
                }
                if (COLD_STACKA1.isFluidStackIdentical(coolantHandler.drain(COLD_STACKA1, false))) {
                    coolantHandler.drain(COLD_STACKA1, true);
                    if(HOT>2880)HOT = HOT - 2880;
                }
                /*if (COLD_STACKB1.isFluidStackIdentical(coolantHandler.drain(COLD_STACKB1, false))) {
                    coolantHandler.drain(COLD_STACKB1, true);
                    if(HOT>5760)HOT = HOT - 5760;
                }*/
            }
        }
    }
    private boolean hasNotEnoughEnergy;
    private void consumeEnergy() {
        int energyToConsume = CWTT()*15*thresholdPercentage;
        boolean hasMaintenance = ConfigHolder.machines.enableMaintenance && hasMaintenanceMechanics();
        if (hasMaintenance) {
            // 10% more energy per maintenance problem
            energyToConsume += getNumMaintenanceProblems() * energyToConsume / 10;
        }

        if (this.hasNotEnoughEnergy && energyContainer.getInputPerSec() > 19L * energyToConsume) {
            this.hasNotEnoughEnergy = false;
        }

        if (this.energyContainer.getEnergyStored() >= energyToConsume) {
            if (!hasNotEnoughEnergy) {
                long consumed = this.energyContainer.removeEnergy(energyToConsume);
                if (consumed == -energyToConsume) {
                    isWorkingEnabled= HOT < 45000;
                } else {
                    this.hasNotEnoughEnergy = true;
                    isWorkingEnabled=false;
                }
            }
        } else {
            this.hasNotEnoughEnergy = true;
            isWorkingEnabled=false;
        }
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("NN","NN","NN","NN")
                .aisle("CN","BN","AN","NN")
                .aisle("FN","EN","DN","NN")
                .aisle("IN","HN","GN","NN")
                .aisle("LN","KN","JN","NN")
                .aisle("XN","SN","NN","NN")
                .where('S', selfPredicate())
                .where('X', abilities(MultiblockAbility.COMPUTATION_DATA_TRANSMISSION))
                .where('N', states(getCasingState())
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMinGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2).setPreviewCount(1)))
                .where('A', TiredTraceabilityPredicate.CP_GPU_CASING1.get())
                .where('B', TiredTraceabilityPredicate.CP_CPU_CASING1.get())
                .where('C', TiredTraceabilityPredicate.CP_RAM_CASING1.get())

                .where('D', TiredTraceabilityPredicate.CP_GPU_CASING2.get())
                .where('E', TiredTraceabilityPredicate.CP_CPU_CASING2.get())
                .where('F', TiredTraceabilityPredicate.CP_RAM_CASING2.get())

                .where('G', TiredTraceabilityPredicate.CP_GPU_CASING3.get())
                .where('H', TiredTraceabilityPredicate.CP_CPU_CASING3.get())
                .where('I', TiredTraceabilityPredicate.CP_RAM_CASING3.get())

                .where('J', TiredTraceabilityPredicate.CP_GPU_CASING4.get())
                .where('K', TiredTraceabilityPredicate.CP_CPU_CASING4.get())
                .where('L', TiredTraceabilityPredicate.CP_RAM_CASING4.get())
                .where('#', any())

                .build();
    }
    public boolean hasMufflerMechanics() {
        return false;
    }
    @Override
    protected boolean shouldShowVoidingModeButton() {
        return false;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public SoundEvent getSound() {
        return GTSoundEvents.COMPUTATION;
    }
    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    protected IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.PTFE_INERT_CASING);
    }
    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("wfcore.multiblock.computingserver.description")};
    }
    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.eu", CWTT()*30,HOT));
        textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.start", thresholdPercentage));

        if((RAM1+RAM2+RAM3+RAM4)>=(GPU1+GPU2+GPU3+GPU4)&&(RAM1+RAM2+RAM3+RAM4)>=(CPU1+CPU2+CPU3+CPU4))
            textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.normal"));
        else
            textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.lack"));

        if (coolantHandler != null) {
            FluidStack STACKA = coolantHandler.drain(Water.getFluid(Integer.MAX_VALUE), false);
            int liquidWaterAmount = STACKA == null ? 0 : STACKA.amount;

            FluidStack STACKB = coolantHandler.drain(PCBCoolant.getFluid(Integer.MAX_VALUE), false);
            int liquidPCBAmount = STACKB == null ? 0 : STACKB.amount;

            //FluidStack STACKC = coolantHandler.drain(LiquidNitrogen.getFluid(Integer.MAX_VALUE), false);
            //int liquidNITAmount = STACKC == null ? 0 : STACKC.amount;
            textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.water.amount", TextFormattingUtil.formatNumbers((liquidWaterAmount)),TextFormattingUtil.formatNumbers((liquidPCBAmount))
             //       ,TextFormattingUtil.formatNumbers((liquidNITAmount))
            ));
        }
        textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.gpu", GPU1,GPU2,GPU3,GPU4));
        textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.cpu", CPU1,CPU2,CPU3,CPU4));
        textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.ram", RAM1,RAM2,RAM3,RAM4));
        if(thresholdPercentage==0) textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.cwtt", returncwt(),0));
        if(thresholdPercentage==1) textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.cwtt", returncwt(),HEAT()));
        if(thresholdPercentage==2) textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.cwtt", returncwt(),HEAT()*4));
    }

    int CWTT()
    {
        if((RAM1+RAM2+RAM3+RAM4)>=(GPU1+GPU2+GPU3+GPU4)&&(RAM1+RAM2+RAM3+RAM4)>=(CPU1+CPU2+CPU3+CPU4)) return (GPU1+GPU2+GPU3+GPU4)+(CPU1+CPU2+CPU3+CPU4);
        else return (RAM1+RAM2+RAM3+RAM4);
    };
    int HEAT()
    {
        return (GPU1+GPU2+GPU3+GPU4)*(CPU1+CPU2+CPU3+CPU4);
    };
    int returncwt()
    {
        if(isWorkingEnabled) {

            if(thresholdPercentage==2) HOT=HOT+HEAT()*4;
            if(thresholdPercentage==1)  HOT=HOT+HEAT();
            if(HOT<30000) return CWTT()*(thresholdPercentage);
            else return CWTT()*thresholdPercentage/2;
        }
        else return 0;
    }
    @Override
    protected void addWarningText(List<ITextComponent> textList) {
        super.addWarningText(textList);
        if (isStructureFormed()) {
            if (HOT>=30000) {
                textList.add(new TextComponentTranslation("wfcore.multiblock.computingserver.hot"));
            }
        }
    }
    @Override
    public int requestCWUt(int cwut, boolean simulate, Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return returncwt();
    }

    @Override
    public int getMaxCWUt(Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return (GPU1+GPU2+GPU3+GPU4+CPU1+CPU2+CPU3+CPU4)*thresholdPercentage;
    }

    @Override
    public boolean canBridge(Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return true;
    }
    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityComputingServer(metaTileEntityId);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        this.getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(), true,
                isStructureFormed());
    }
    @SideOnly(Side.CLIENT)
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.RESEARCH_STATION_OVERLAY;
    }
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
                return Textures.INERT_PTFE_CASING;
    }
    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.energyContainer = new EnergyContainerList(getAbilities(MultiblockAbility.INPUT_ENERGY));
        this.coolantHandler = new FluidTankList(false, getAbilities(MultiblockAbility.IMPORT_FLUIDS));


        Object CPU1 = context.get("CPU1TieredStats");
        Object GPU1 = context.get("GPU1TieredStats");
        Object RAM1 = context.get("RAM1TieredStats");
        this.CPU1 = GTQTUtil.getOrDefault(() -> CPU1 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)CPU1).getIntTier(),
                0);

        this.GPU1 = GTQTUtil.getOrDefault(() -> GPU1 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)GPU1).getIntTier(),
                0);

        this.RAM1 = GTQTUtil.getOrDefault(() -> RAM1 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)RAM1).getIntTier(),
                0);

        Object CPU2 = context.get("CPU2TieredStats");
        Object GPU2 = context.get("GPU2TieredStats");
        Object RAM2 = context.get("RAM2TieredStats");
        this.CPU2 = GTQTUtil.getOrDefault(() -> CPU2 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)CPU2).getIntTier(),
                0);

        this.GPU2 = GTQTUtil.getOrDefault(() -> GPU2 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)GPU2).getIntTier(),
                0);

        this.RAM2 = GTQTUtil.getOrDefault(() -> RAM2 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)RAM2).getIntTier(),
                0);

        Object CPU3 = context.get("CPU3TieredStats");
        Object GPU3 = context.get("GPU3TieredStats");
        Object RAM3 = context.get("RAM3TieredStats");
        this.CPU3 = GTQTUtil.getOrDefault(() -> CPU3 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)CPU3).getIntTier(),
                0);

        this.GPU3 = GTQTUtil.getOrDefault(() -> GPU3 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)GPU3).getIntTier(),
                0);

        this.RAM3 = GTQTUtil.getOrDefault(() -> RAM3 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)RAM3).getIntTier(),
                0);

        Object CPU4 = context.get("CPU4TieredStats");
        Object GPU4 = context.get("GPU4TieredStats");
        Object RAM4 = context.get("RAM4TieredStats");
        this.CPU4 = GTQTUtil.getOrDefault(() -> CPU4 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)CPU4).getIntTier(),
                0);

        this.GPU4 = GTQTUtil.getOrDefault(() -> GPU4 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)GPU4).getIntTier(),
                0);

        this.RAM4 = GTQTUtil.getOrDefault(() -> RAM4 instanceof WrappedIntTired,
                () -> ((WrappedIntTired)RAM4).getIntTier(),
                0);
    }
    private TextFormatting getDisplayTemperatureColor() {
        if (HOT < 25000) {
            return TextFormatting.GREEN;
        } else if (HOT < 45000) {
            return TextFormatting.YELLOW;
        }
        return TextFormatting.RED;
    }

    @Override
    public int getNumProgressBars() {
        return 2;
    }

    @Override
    public double getFillPercentage(int index) {
        return index == 0 ? 1.0 * returncwt() / ((GPU1+GPU2+GPU3+GPU4)+(CPU1+CPU2+CPU3+CPU4)) :
                Math.min(1.0, HOT / 50000);
    }

    @Override
    public TextureArea getProgressBarTexture(int index) {
        return index == 0 ? GuiTextures.PROGRESS_BAR_HPCA_COMPUTATION : GuiTextures.PROGRESS_BAR_FUSION_HEAT;
    }

    @Override
    public void addBarHoverText(List<ITextComponent> hoverList, int index) {
        if (index == 0) {
            ITextComponent cwutInfo = TextComponentUtil.stringWithColor(
                    TextFormatting.AQUA,
                    returncwt()+ " / " + ((GPU1+GPU2+GPU3+GPU4)+(CPU1+CPU2+CPU3+CPU4)) + " CWU/t");
            hoverList.add(TextComponentUtil.translationWithColor(
                    TextFormatting.GRAY,
                    "gregtech.multiblock.hpca.computation",
                    cwutInfo));
        } else {
            ITextComponent tempInfo = TextComponentUtil.stringWithColor(
                    getDisplayTemperatureColor(),
                    Math.round(HOT / 100.0D) + "°C");
            hoverList.add(TextComponentUtil.translationWithColor(
                    TextFormatting.GRAY,
                    "gregtech.multiblock.hpca.temperature",
                    tempInfo));
        }
    }

    @Override
    protected ModularUI.Builder createUITemplate(EntityPlayer entityPlayer) {
        ModularUI.Builder builder;
        label38: {
            builder = ModularUI.builder(GuiTextures.BACKGROUND, 198, 258);
            if (this instanceof IProgressBarMultiblock) {
                IProgressBarMultiblock progressMulti = (IProgressBarMultiblock)this;
                if (progressMulti.showProgressBar()) {
                    builder.image(4, 4, 190, 149, GuiTextures.DISPLAY);
                    ProgressWidget progressBar;
                    if (progressMulti.getNumProgressBars() == 3) {
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(0);
                        }, 4, 135, 62, 7, progressMulti.getProgressBarTexture(0), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 0);
                        });
                        builder.widget(progressBar);
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(1);
                        }, 68, 135, 62, 7, progressMulti.getProgressBarTexture(1), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 1);
                        });
                        builder.widget(progressBar);
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(2);
                        }, 132, 155, 62, 7, progressMulti.getProgressBarTexture(2), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 2);
                        });
                        builder.widget(progressBar);
                    } else if (progressMulti.getNumProgressBars() == 2) {
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(0);
                        }, 4, 155, 94, 7, progressMulti.getProgressBarTexture(0), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 0);
                        });
                        builder.widget(progressBar);
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(1);
                        }, 100, 155, 94, 7, progressMulti.getProgressBarTexture(1), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 1);
                        });
                        builder.widget(progressBar);
                    } else {
                        progressBar = (new ProgressWidget(() -> {
                            return progressMulti.getFillPercentage(0);
                        }, 4, 115, 190, 7, progressMulti.getProgressBarTexture(0), ProgressWidget.MoveType.HORIZONTAL)).setHoverTextConsumer((list) -> {
                            progressMulti.addBarHoverText(list, 0);
                        });
                        builder.widget(progressBar);
                    }

                    builder.widget((new IndicatorImageWidget(174, 93, 17, 17, this.getLogo())).setWarningStatus(this.getWarningLogo(), this::addWarningText).setErrorStatus(this.getErrorLogo(), this::addErrorText));
                    break label38;
                }
            }

            builder.image(4, 4, 190, 117, GuiTextures.DISPLAY);
            builder.widget((new IndicatorImageWidget(174, 101, 17, 17, this.getLogo())).setWarningStatus(this.getWarningLogo(), this::addWarningText).setErrorStatus(this.getErrorLogo(), this::addErrorText));
        }

        builder.label(9, 9, this.getMetaFullName(), 16777215);
        builder.widget((new AdvancedTextWidget(9, 20, this::addDisplayText, 16777215)).setMaxWidthLimit(181).setClickHandler(this::handleDisplayClick));
        IControllable controllable = (IControllable)this.getCapability(GregtechTileCapabilities.CAPABILITY_CONTROLLABLE, (EnumFacing)null);
        TextureArea var10007;
        BooleanSupplier var10008;
        if (controllable != null) {
            var10007 = GuiTextures.BUTTON_POWER;
            Objects.requireNonNull(controllable);
            var10008 = controllable::isWorkingEnabled;
            Objects.requireNonNull(controllable);
            builder.widget(new ImageCycleButtonWidget(173, 183, 18, 18, var10007, var10008, controllable::setWorkingEnabled));
            builder.widget(new ImageWidget(173, 221, 18, 6, GuiTextures.BUTTON_POWER_DETAIL));
        }

        if (this.shouldShowVoidingModeButton()) {
            builder.widget((new ImageCycleButtonWidget(173, 161, 18, 18, GuiTextures.BUTTON_VOID_MULTIBLOCK, 4, this::getVoidingMode, this::setVoidingMode)).setTooltipHoverString(MultiblockWithDisplayBase::getVoidingModeTooltip));
        } else {
            builder.widget((new ImageWidget(173, 201, 18, 18, GuiTextures.BUTTON_VOID_NONE)).setTooltip("gregtech.gui.multiblock_voiding_not_supported"));
        }

        label30: {
            if (this instanceof IDistinctBusController) {
                IDistinctBusController distinct = (IDistinctBusController)this;
                if (distinct.canBeDistinct()) {
                    var10007 = GuiTextures.BUTTON_DISTINCT_BUSES;
                    Objects.requireNonNull(distinct);
                    var10008 = distinct::isDistinct;
                    Objects.requireNonNull(distinct);
                    builder.widget((new ImageCycleButtonWidget(173, 223, 18, 18, var10007, var10008, distinct::setDistinct)).setTooltipHoverString((i) -> {
                        return "gregtech.multiblock.universal.distinct_" + (i == 0 ? "disabled" : "enabled");
                    }));
                    break label30;
                }
            }

            builder.widget((new ImageWidget(173, 183, 18, 18, GuiTextures.BUTTON_NO_DISTINCT_BUSES)).setTooltip("gregtech.multiblock.universal.distinct_not_supported"));
        }

        builder.widget(this.getFlexButton(173, 165, 18, 18));
        builder.bindPlayerInventory(entityPlayer.inventory, 165);
        return builder;
    }
}