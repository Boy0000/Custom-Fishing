package net.momirealms.customfishing.compatibility.item;

import com.mineinabyss.geary.papermc.tracking.items.GearyItemProvider;
import com.mineinabyss.geary.papermc.tracking.items.ItemTrackingKt;
import com.mineinabyss.geary.prefabs.PrefabKey;
import com.mineinabyss.geary.prefabs.PrefabsKt;
import com.mineinabyss.geary.prefabs.helpers.PrefabHelpersKt;
import com.mineinabyss.geary.prefabs.serializers.PrefabKeySerializer;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import net.momirealms.customfishing.api.mechanic.item.ItemLibrary;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GearyItemImpl implements ItemLibrary {

    @Override
    public String identification() {
        return "Geary";
    }

    @Override
    public ItemStack buildItem(Player player, String id) {
        PrefabKey prefabKey = PrefabKey.Companion.ofOrNull(id);
        if (prefabKey == null) return null;
        ItemStack itemStack = ItemTrackingKt.getGearyItems().createItem(prefabKey, null);
        return itemStack != null ? itemStack : new ItemStack(Material.AIR);
    }

    @Override
    public String getItemID(ItemStack itemStack) {
        return OraxenItems.getIdByItem(itemStack);
    }
}
