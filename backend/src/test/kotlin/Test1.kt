package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.logger
import org.junit.jupiter.api.Test

class Test1 {
    
    val inferno = RootDirectory("/home/flamingo/projects/minecraft/inferno/resourcepacks/resourcepacks/main/")
    val minecraft = RootDirectory("/home/flamingo/projects/minecraft/devtools/packed/backend/src/test/resources/test")
    
    @Test
    fun test1() {
        val stardust = ResourceGroup(inferno, Key("inferno:item/materials/common/stardust"))
        logger.info { stardust }
    }
    
    @Test
    fun test2() {
        val engine = ResourceGroup(inferno, Key("inferno:item/materials/limbo/forge/components/drill_engine"))
        logger.info { engine }
    }
    
    @Test
    fun test3() {
        val groups = inferno.groups.value.groups.map { ResourceGroup(inferno, it) }
        groups.forEach { logger.info { it } }
    }
}