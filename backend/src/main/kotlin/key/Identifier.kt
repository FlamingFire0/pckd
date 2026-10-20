package me.flamingo.pckd.backend.key

import me.flamingo.pckd.backend.resources.ResourceInfo
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.resources.Resource
import java.nio.file.Path

data class Identifier<I: ResourceInfo<I, R>, R: Resource<R, I>>(
    val key: Key,
    val resource: I,
) {
    override fun toString(): String = "(${resource}) $key"
    fun toPath(root: RootDirectory): Path = key.toPath(root, resource)
}