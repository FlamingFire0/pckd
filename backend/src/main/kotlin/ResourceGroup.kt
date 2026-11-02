package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.backend.resources.ItemResource
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.TextureResource

class ResourceGroup(
    val root: RootDirectory,
    val key: Key
) {
    val textures = ResourceSlot(key, root, TextureResource)
    val models   = ResourceSlot(key, root, ModelResource  )
    val items    = ResourceSlot(key, root, ItemResource   )
    
    override fun toString(): String = buildString {
        
        fun StringBuilder.drawSection(name: String, resources: ResourceSlot<*, *>) {
            appendLine("> $name:")
            resources.resources.forEach {
                append("  - ")
                if(it.getFile().notExists()) {
                    append("[MISSING] ")
                }

                appendLine("$it")
                if(it is TextureResource && it.getMeta().exists()) {
                    appendLine("    + meta") 
                }
            }
        }
        
        appendLine()
        appendLine("ResourceGroup [$key]:")
        drawSection("Textures", textures)
        drawSection("Models"  , models  )
        drawSection("Items"   , items   )
    }
}