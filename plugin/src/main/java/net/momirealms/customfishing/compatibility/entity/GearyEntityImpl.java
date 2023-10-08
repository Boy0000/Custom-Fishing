package net.momirealms.customfishing.compatibility.entity;

import com.mineinabyss.geary.papermc.configlang.helpers.EntitySerializerKt;
import com.mineinabyss.geary.papermc.tracking.entities.BukkitEntity2Geary;
import com.mineinabyss.geary.papermc.tracking.entities.EntityTrackingKt;
import com.mineinabyss.geary.papermc.tracking.entities.helpers.HelpersKt;
import com.mineinabyss.geary.prefabs.PrefabKey;
import com.mineinabyss.geary.prefabs.helpers.PrefabHelpersKt;
import com.mineinabyss.mobzy.injection.helpers.EntityHelpersKt;
import kotlin.Result;
import net.momirealms.customfishing.api.mechanic.entity.EntityLibrary;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Map;

public class GearyEntityImpl implements EntityLibrary {
    @Override
    public String identification() {
        return "Geary";
    }

    @Override
    public Entity spawn(Location location, String id, Map<String, Object> propertyMap) {
        PrefabKey prefabKey = PrefabKey.Companion.ofOrNull(id);
        if (prefabKey == null) return null;
        return (Entity) HelpersKt.spawnFromPrefab(location, prefabKey);
    }
}
