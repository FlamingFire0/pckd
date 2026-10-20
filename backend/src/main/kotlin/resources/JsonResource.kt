package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key

abstract class JsonResource<S: JsonResource<S, I>, I: ResourceInfo<I, S>>(
    key: Key,
    rootDirectory: RootDirectory,
    resourceDirectory: I,
) : Resource<S, I>(key, rootDirectory, resourceDirectory)