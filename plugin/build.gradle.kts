dependencies {
    // server
    compileOnly("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")

    // command
    compileOnly("dev.jorel:commandapi-bukkit-core:9.2.0")

    // packet
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")

    // papi
    compileOnly("me.clip:placeholderapi:2.11.3")

    // config
    compileOnly("dev.dejvokep:boosted-yaml:1.3.1")

    // mythic
    compileOnly("io.lumine:Mythic-Dist:5.3.5")
    compileOnly("net.Indyuce:MMOItems-API:6.9.2-SNAPSHOT")
    compileOnly("io.lumine:MythicLib-dist:1.6-SNAPSHOT")
    compileOnly("net.Indyuce:MMOCore-API:1.12-SNAPSHOT")

    // Gson
    compileOnly("com.google.code.gson:gson:2.10.1")

    // eco
    compileOnly("com.willfp:eco:6.65.5")
    compileOnly("com.willfp:EcoJobs:3.32.3")
    compileOnly("com.willfp:EcoSkills:3.21.0")
    compileOnly("com.willfp:libreforge:4.34.0")

    // database
    compileOnly("org.xerial:sqlite-jdbc:3.42.0.0")
    compileOnly("com.h2database:h2:2.2.220")
    compileOnly("org.mongodb:mongodb-driver-sync:4.10.2")
    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("redis.clients:jedis:5.0.0")

    // others
    compileOnly("com.github.LoneDev6:api-itemsadder:3.5.0c-r5")
    compileOnly("com.github.oraxen:oraxen:1.161.0")
    compileOnly("pers.neige.neigeitems:NeigeItems:1.15.24")
    compileOnly("com.github.Zrips:Jobs:4.17.2")
    compileOnly("com.github.Archy-X:AureliumSkills:Beta1.3.21")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.betonquest:betonquest:2.0.0-SNAPSHOT")

    // local jars
    compileOnly(files("libs/AdvancedEnchantments-api.jar"))
    compileOnly(files("libs/BattlePass-4.0.6-api.jar"))
    compileOnly(files("libs/RealisticSeasons-api.jar"))
    compileOnly(files("libs/CustomCrops-api.jar"))
    compileOnly(files("libs/mcMMO-api.jar"))
    compileOnly(files("libs/ClueScrolls-4.8.7-api.jar"))
    compileOnly(files("libs/notquests-5.17.1.jar"))
    compileOnly(files("libs/zaphkiel-2.0.24.jar"))

    // api module
    implementation(project(":api"))

    // adventure
    implementation("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")

    // nbt
    implementation("de.tr7zw:item-nbt-api:2.12.0")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.1")

    // local lib
    implementation(files("libs/BiomeAPI.jar"))

    // anvil
    implementation("net.wesjd:anvilgui:1.9.0-SNAPSHOT")
    implementation("de.themoep:inventorygui:1.6.1-SNAPSHOT")
}

tasks {
    shadowJar {
        relocate ("de.tr7zw.changeme", "net.momirealms.customfishing.libraries")
        relocate ("de.tr7zw.annotations", "net.momirealms.customfishing.libraries.annotations")
        relocate ("net.kyori", "net.momirealms.customfishing.libraries")
        relocate ("net.wesjd", "net.momirealms.customfishing.libraries")
        relocate ("org.bstats", "net.momirealms.customfishing.libraries.bstats")
        relocate ("net.momirealms.biomeapi", "net.momirealms.customfishing.libraries.biomeapi")
    }
}
