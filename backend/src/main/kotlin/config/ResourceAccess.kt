package me.flamingo.pckd.backend.config

import me.flamingo.pckd.backend.RootDirectory

fun getResource(path: String) = RootDirectory::class.java.getResource(path)