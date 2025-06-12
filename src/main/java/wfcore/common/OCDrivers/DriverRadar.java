package wfcore.common.OCDrivers;

import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.integration.opencomputers.drivers.EnvironmentMetaTileEntity;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wfcore.common.metatileentities.multi.MetaTileEntityRadar;

import java.util.Iterator;

public class DriverRadar extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return MetaTileEntityRadar.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof IGregTechTileEntity) {
            return new EnvironmentRadar((IGregTechTileEntity) tileEntity,
                    (MetaTileEntityRadar) ((IGregTechTileEntity) tileEntity).getMetaTileEntity());
        }
        return null;
    }

    @Override
    public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IGregTechTileEntity) {
            return ((IGregTechTileEntity) tileEntity).getMetaTileEntity() instanceof MetaTileEntityRadar;
        }
        return false;
    }

    public final static class EnvironmentRadar extends
            EnvironmentMetaTileEntity<MetaTileEntityRadar> {

        public EnvironmentRadar(IGregTechTileEntity holder, MetaTileEntityRadar tileEntity) {
            super(holder, tileEntity, "radar");
        }

        @Callback(doc = "setLevel(x, z, level): --  sets activity at chunk. (dev command)")
        public Object[] setLevel(final Context context, final Arguments args) {
            return new Object[] { tileEntity.addActivity(args.checkInteger(0), args.checkInteger(1), args.checkInteger(2))};
        }

        @Callback(doc = "getLevel(x, z): --  gets activity at chunk.")
        public Object[] getLevel(final Context context, final Arguments args) {
            return new Object[] { tileEntity.scanChunk(args.checkInteger(0), args.checkInteger(1))};
        }

        @Callback(doc = "testFunct(): --  adds 2 numbers")
        public Object[] testFunct(final Context context, final Arguments args) {
            return new Object[] { args.checkInteger(0) + args.checkInteger(1)};
        }
    }
}
