package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.backend.resources.ItemResource
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.TextureResource

class ResourceGroup(
    val root: RootDirectory,
    val key: Key
) {
    val textures = TextureResource.gather(root, key).toMutableList()
    val models   = ModelResource  .gather(root, key).toMutableList()
    val items    = ItemResource   .gather(root, key).toMutableList()
    
    override fun toString(): String = buildString {
        
        fun StringBuilder.drawSection(name: String, resources: List<Resource<*, *>>) {
            appendLine("> $name:")
            resources.forEach {
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