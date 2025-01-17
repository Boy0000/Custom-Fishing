/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customfishing.compatibility.entity;

import dev.lone.itemsadder.api.CustomEntity;
import net.momirealms.customfishing.api.mechanic.entity.EntityLibrary;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Map;

public class ItemsAdderEntityImpl implements EntityLibrary {

    @Override
    public String identification() {
        return "vanilla";
    }

    @Override
    public Entity spawn(Location location, String id, Map<String, Object> propertyMap) {
        CustomEntity customEntity = CustomEntity.spawn(
                id,
                location,
                (Boolean) propertyMap.get("frustumCulling"),
                (Boolean) propertyMap.get("noBase"),
                (Boolean) propertyMap.get("noHitbox")
        );
        return customEntity.getEntity();
    }
}
