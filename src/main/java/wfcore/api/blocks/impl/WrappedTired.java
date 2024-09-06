package wfcore.api.blocks.impl;

import net.minecraft.util.IStringSerializable;
import wfcore.api.blocks.IBlockTier;

public class WrappedTired implements IBlockTier {

    private final IStringSerializable inner;

    public WrappedTired(IStringSerializable inner) {
        this.inner = inner;
    }

    @Override
    public String getName() {
        return inner.getName();
    }
}