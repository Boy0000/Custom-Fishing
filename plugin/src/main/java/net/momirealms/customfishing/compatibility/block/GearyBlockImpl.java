package net.momirealms.customfishing.compatibility.block;

import com.mineinabyss.geary.papermc.tracking.blocks.BlockTrackingKt;
import com.mineinabyss.geary.prefabs.PrefabKey;
import dev.lone.itemsadder.api.CustomBlock;
import net.momirealms.customfishing.api.mechanic.block.BlockDataModifier;
import net.momirealms.customfishing.api.mechanic.block.BlockLibrary;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GearyBlockImpl implements BlockLibrary {

    @Override
    public String identification() {
        return "Geary";
    }

    @Override
    public BlockData getBlockData(Player player, String id, List<BlockDataModifier> modifiers) {
        PrefabKey prefabKey = PrefabKey.Companion.ofOrNull(id);
        if (prefabKey == null) return null;
        BlockData blockData = BlockTrackingKt.getGearyBlocks().createBlockData(prefabKey);
        for (BlockDataModifier modifier : modifiers) {
            modifier.apply(player, blockData);
        }
        return blockData;
    }

    @Override
    public @Nullable String getBlockID(Block block) {
        PrefabKey prefabKey = BlockTrackingKt.getGearyBlocks().getBlock2Prefab().get(block.getBlockData());
        return prefabKey != null ? prefabKey.getFull() : null;
    }
}
