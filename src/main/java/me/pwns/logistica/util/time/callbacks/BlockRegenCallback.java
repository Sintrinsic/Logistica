package me.pwns.logistica.util.time.callbacks;

import me.pwns.logistica.world.regen.BlockGroup;

public class BlockRegenCallback implements CallbackContainer {
    private BlockGroup target;

    public BlockRegenCallback(BlockGroup target) {
        this.target = target;
    }

    @Override
    public void doCallback() {
        target.restore();
    }
}
