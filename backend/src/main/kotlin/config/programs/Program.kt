package me.flamingo.pckd.backend.config.programs

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.Placeholder
import me.flamingo.pckd.backend.resources.Resource
import java.awt.Desktop
import java.nio.file.Path

@Serializable
open class Program(
    @Contextual val file: Path,
    val arguments: List<String> = listOf("{path}")
) {
    override fun toString(): String = file.toString()
    
    open fun run(path: Path, rootDirectory: RootDirectory) {
        val args = arguments.map {
            Placeholder.applyAll(it, path, rootDirectory)
        }

        ProcessBuilder(file.toString(), *args.toTypedArray()).start()
    }
    
    fun run(resource: Resource<*, *>) = run(resource.getFile().path, resource.rootDirectory)
    
    object DEFAULT : Program(Path.of(""), emptyList()) {
        override fun run(path: Path, rootDirectory: RootDirectory) {
            Desktop.getDesktop().open(path.toFile())
        }
    }
}