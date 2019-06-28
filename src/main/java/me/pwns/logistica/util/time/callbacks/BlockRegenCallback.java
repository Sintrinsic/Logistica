package me.pwns.logistica.util.time.callbacks;

import me.pwns.logistica.world.regen.BlockGroup;
import me.pwns.logistica.world.regen.BlockGroupContainer;

public class BlockRegenCallback implements CallbackContainer {
    private BlockGroupContainer target;

    public BlockRegenCallback(BlockGroupContainer target) {
        this.target = target;
    }

    @Override
    public void doCallback() {
        // TODO causes exception when not checking for null childgroup; better way to handle this?
        if (target.getChildGroup() != null) {
            target.restore();
        }
    }
}
