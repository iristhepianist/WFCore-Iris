package wfcore.api.blocks;

import net.minecraft.util.IStringSerializable;

public interface IBlockTier extends IStringSerializable {

    default Object getInfo() {
        return null;
    }

    default Object getTier() {
        return 0;
    }
}
