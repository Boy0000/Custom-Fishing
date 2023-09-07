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

package net.momirealms.customfishing.mechanic.loot;

import net.momirealms.customfishing.api.CustomFishingPlugin;
import net.momirealms.customfishing.api.manager.LootManager;
import net.momirealms.customfishing.api.mechanic.action.Action;
import net.momirealms.customfishing.api.mechanic.action.ActionTrigger;
import net.momirealms.customfishing.api.mechanic.loot.CFLoot;
import net.momirealms.customfishing.api.mechanic.loot.Loot;
import net.momirealms.customfishing.api.mechanic.loot.LootType;
import net.momirealms.customfishing.api.util.LogUtils;
import net.momirealms.customfishing.util.ConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class LootManagerImpl implements LootManager {

    private final CustomFishingPlugin plugin;
    private final HashMap<String, Loot> lootMap;
    private final HashMap<String, List<String>> lootGroupMap;

    public static class GlobalSetting {
        public static CFLoot globalLootProperties;
        public static boolean disableStats;
        public static boolean disableGames;
        public static boolean instantGame;
        public static boolean showInFinder;
        public static String gameGroup;
        public static String[] lootGroup;
    }

    public LootManagerImpl(CustomFishingPlugin plugin) {
        this.plugin = plugin;
        this.lootMap = new HashMap<>();
        this.lootGroupMap = new HashMap<>();
    }

    public void load() {
        this.loadGlobalLootProperties();
        this.loadLootsFromPluginFolder();
    }

    public void unload() {
        this.lootMap.clear();
        this.lootGroupMap.clear();
    }

    public void disable() {
        unload();
    }

    @SuppressWarnings("DuplicatedCode")
    public void loadLootsFromPluginFolder() {
        Deque<File> fileDeque = new ArrayDeque<>();
        for (String type : List.of("loots", "mobs", "blocks")) {
            File typeFolder = new File(plugin.getDataFolder() + File.separator + "contents" + File.separator + type);
            if (!typeFolder.exists()) {
                if (!typeFolder.mkdirs()) return;
                plugin.saveResource("contents" + File.separator + type + File.separator + "default.yml", false);
            }
            fileDeque.push(typeFolder);
            while (!fileDeque.isEmpty()) {
                File file = fileDeque.pop();
                File[] files = file.listFiles();
                if (files == null) continue;
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        fileDeque.push(subFile);
                    } else if (subFile.isFile() && subFile.getName().endsWith(".yml")) {
                        loadSingleFile(subFile, StringUtils.chop(type));
                    }
                }
            }
        }
    }

    @Override
    public Loot getGlobalLootProperties() {
        return GlobalSetting.globalLootProperties;
    }

    @Nullable
    @Override
    public List<String> getLootGroup(String key) {
        return lootGroupMap.get(key);
    }

    @Nullable
    @Override
    public Loot getLoot(String key) {
        return lootMap.get(key);
    }

    private void loadGlobalLootProperties() {
        YamlConfiguration config = plugin.getConfig("config.yml");
        GlobalSetting.globalLootProperties = getSingleSectionItem(
                Objects.requireNonNull(config.getConfigurationSection("mechanics.global-loot-properties")),
                "GLOBAL",
                "global"
        );
        GlobalSetting.disableStats = GlobalSetting.globalLootProperties.disableStats();
        GlobalSetting.disableGames = GlobalSetting.globalLootProperties.disableGame();
        GlobalSetting.instantGame = GlobalSetting.globalLootProperties.instanceGame();
        GlobalSetting.showInFinder = GlobalSetting.globalLootProperties.showInFinder();
        GlobalSetting.lootGroup = GlobalSetting.globalLootProperties.getLootGroup();
        GlobalSetting.gameGroup = GlobalSetting.globalLootProperties.getGameConfigKey();
    }

    private void loadSingleFile(File file, String namespace) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Object> entry : yaml.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection section) {
                var loot = getSingleSectionItem(
                        section,
                        namespace,
                        entry.getKey()
                );
                if (lootMap.containsKey(entry.getKey())) {
                    LogUtils.severe("Duplicated loot found: " + entry.getKey() + ".");
                } else {
                    lootMap.put(entry.getKey(), loot);
                }
                String[] group = loot.getLootGroup();
                if (group != null) {
                    for (String g : group) {
                        List<String> groupMembers = lootGroupMap.computeIfAbsent(g, k -> new ArrayList<>());
                        groupMembers.add(loot.getID());
                    }
                }
            }
        }
    }

    private CFLoot getSingleSectionItem(ConfigurationSection section, String namespace, String key) {
        return new CFLoot.Builder(key, LootType.valueOf(namespace.toUpperCase(Locale.ENGLISH)))
                .disableStats(section.getBoolean("disable-stat", GlobalSetting.disableStats))
                .disableGames(section.getBoolean("disable-game", GlobalSetting.disableGames))
                .instantGame(section.getBoolean("instant-game", GlobalSetting.instantGame))
                .showInFinder(section.getBoolean("show-in-fishfinder", GlobalSetting.showInFinder))
                .gameConfig(section.getString("game-group", GlobalSetting.gameGroup))
                .lootGroup(ConfigUtils.stringListArgs(Optional.ofNullable(section.get("loot-group")).orElse(GlobalSetting.lootGroup)).toArray(new String[0]))
                .nick(section.getString("nick", section.getString("display.name", key)))
                .addActions(getActionMap(section.getConfigurationSection("events")))
                .addTimesActions(getTimesActionMap(section.getConfigurationSection("events.success-times")))
                .build();
    }


    private HashMap<ActionTrigger, Action[]> getActionMap(ConfigurationSection section) {
        HashMap<ActionTrigger, Action[]> actionMap = new HashMap<>();
        if (section == null) return actionMap;
        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection innerSection) {
                actionMap.put(
                        ActionTrigger.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH)),
                        plugin.getActionManager().getActions(innerSection)
                );
            }
        }
        return actionMap;
    }

    private HashMap<Integer, Action[]> getTimesActionMap(ConfigurationSection section) {
        HashMap<Integer, Action[]> actionMap = new HashMap<>();
        if (section == null) return actionMap;
        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection innerSection) {
                actionMap.put(Integer.parseInt(entry.getKey()), plugin.getActionManager().getActions(innerSection));
            }
        }
        return actionMap;
    }
}
