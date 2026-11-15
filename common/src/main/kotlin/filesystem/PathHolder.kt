package me.flamingo.pckd.common.filesystem

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.nio.charset.Charset
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileAttribute
import java.nio.file.attribute.FileTime
import java.nio.file.attribute.UserPrincipal
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream
import kotlin.io.path.createParentDirectories
import kotlin.io.path.moveTo
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension


@Suppress("unused")
abstract class PathHolder(val path: Path) {

    abstract fun ensure()

    // Path properties

    val fileSystem: FileSystem
        get() = path.fileSystem

    val isAbsolute: Boolean
        get() = path.isAbsolute

    val root: Path?
        get() = path.root

    val fileName: Path?
        get() = path.fileName

    val name: String get() = path.name
    
    val nameWithoutExtension: String get() = path.nameWithoutExtension

    val parent: Path?
        get() = path.parent

    val nameCount: Int
        get() = path.nameCount

    // Operators

    operator fun div(other: Path): Path =
        path.resolve(other)

    operator fun div(other: PathHolder): Path =
        path.resolve(other.path)

    operator fun div(other: String): Path =
        path.resolve(other)

    // Path components

    fun getName(index: Int): Path =
        path.getName(index)

    fun subpath(beginIndex: Int, endIndex: Int): Path =
        path.subpath(beginIndex, endIndex)

    // Path comparison

    fun startsWith(other: Path): Boolean =
        path.startsWith(other)

    fun startsWith(other: PathHolder): Boolean =
        path.startsWith(other.path)

    fun startsWith(other: String): Boolean =
        path.startsWith(other)

    fun endsWith(other: Path): Boolean =
        path.endsWith(other)

    fun endsWith(other: PathHolder): Boolean =
        path.endsWith(other.path)

    fun endsWith(other: String): Boolean =
        path.endsWith(other)

    fun compareTo(other: Path): Int =
        path.compareTo(other)

    fun compareTo(other: PathHolder): Int =
        path.compareTo(other.path)

    // Path resolution

    fun resolve(other: Path): Path =
        path.resolve(other)

    fun resolve(other: PathHolder): Path =
        path.resolve(other.path)

    fun resolve(other: String): Path =
        path.resolve(other)

    fun resolve(first: Path, vararg more: Path): Path =
        path.resolve(first, *more)

    fun resolve(first: String, vararg more: String): Path =
        path.resolve(first, *more)

    fun resolveSibling(other: Path): Path =
        path.resolveSibling(other)

    fun resolveSibling(other: PathHolder): Path =
        path.resolveSibling(other.path)

    fun resolveSibling(other: String): Path =
        path.resolveSibling(other)

    fun relativize(other: Path): Path =
        path.relativize(other)

    fun relativize(other: PathHolder): Path =
        path.relativize(other.path)

    // Path transformations

    fun normalize(): Path =
        path.normalize()

    fun toAbsolutePath(): Path =
        path.toAbsolutePath()

    fun toRealPath(vararg options: LinkOption): Path =
        path.toRealPath(*options)

    fun toUri(): URI =
        path.toUri()

    fun toFile(): java.io.File =
        path.toFile()

    override fun toString(): String =
        path.toString()

    // Directory watching

    fun register(
        watcher: WatchService,
        vararg events: WatchEvent.Kind<*>
    ): WatchKey =
        path.register(watcher, *events)

    fun register(
        watcher: WatchService,
        events: Array<out WatchEvent.Kind<*>>,
        vararg modifiers: WatchEvent.Modifier
    ): WatchKey =
        path.register(watcher, events, *modifiers)

    // Iteration

    fun iterator(): Iterator<Path> =
        path.iterator()

    fun forEach(action: Consumer<in Path>) =
        path.forEach(action)

    fun spliterator(): Spliterator<Path> =
        path.spliterator()

    // File attributes

    fun size(): Long =
        Files.size(path)

    fun lastModifiedTime(vararg options: LinkOption): FileTime =
        Files.getLastModifiedTime(path, *options)

    fun setLastModifiedTime(time: FileTime): Path =
        Files.setLastModifiedTime(path, time)

    fun fileStore(): FileStore =
        Files.getFileStore(path)

    fun owner(vararg options: LinkOption): UserPrincipal =
        Files.getOwner(path, *options)

    fun setOwner(owner: UserPrincipal): Path =
        Files.setOwner(path, owner)

    // File checks

    fun exists(vararg options: LinkOption): Boolean =
        Files.exists(path, *options)

    fun notExists(vararg options: LinkOption): Boolean =
        Files.notExists(path, *options)

    fun isRegularFile(vararg options: LinkOption): Boolean =
        Files.isRegularFile(path, *options)

    fun isDirectory(vararg options: LinkOption): Boolean =
        Files.isDirectory(path, *options)

    fun isSymbolicLink(): Boolean =
        Files.isSymbolicLink(path)

    fun isOther(vararg options: LinkOption): Boolean =
        Files.readAttributes(
            path,
            BasicFileAttributes::class.java,
            *options
        ).isOther

    fun isHidden(): Boolean =
        Files.isHidden(path)

    fun isReadable(): Boolean =
        Files.isReadable(path)

    fun isWritable(): Boolean =
        Files.isWritable(path)

    fun isExecutable(): Boolean =
        Files.isExecutable(path)

    fun isSameFile(other: Path): Boolean =
        Files.isSameFile(path, other)

    fun isSameFile(other: PathHolder): Boolean =
        isSameFile(other.path)

    // File creation

    fun createFile(vararg attrs: FileAttribute<*>): Path =
        Files.createFile(path, *attrs)

    fun createDirectory(vararg attrs: FileAttribute<*>): Path =
        Files.createDirectory(path, *attrs)

    fun createDirectories(vararg attrs: FileAttribute<*>): Path =
        Files.createDirectories(path, *attrs)

    fun createParentDirectories(vararg attrs: FileAttribute<*>): Path =
        path.createParentDirectories(*attrs)

    fun createSymbolicLink(
        target: Path,
        vararg attrs: FileAttribute<*>
    ): Path =
        Files.createSymbolicLink(path, target, *attrs)

    fun createSymbolicLink(
        target: PathHolder,
        vararg attrs: FileAttribute<*>
    ): Path =
        createSymbolicLink(target.path, *attrs)

    fun createLink(existing: Path): Path =
        Files.createLink(path, existing)

    fun createLink(existing: PathHolder): Path =
        createLink(existing.path)

    // File deletion

    fun delete() =
        Files.delete(path)

    fun deleteIfExists(): Boolean =
        Files.deleteIfExists(path)

    // File copying and moving

    fun copyTo(
        target: Path,
        vararg options: CopyOption
    ): Path =
        Files.copy(path, target, *options)

    fun copyTo(
        target: PathHolder,
        vararg options: CopyOption
    ): Path =
        copyTo(target.path, *options)

    fun move(
        target: Path,
        vararg options: CopyOption
    ): Path =
        Files.move(path, target, *options)

    fun move(
        target: PathHolder,
        vararg options: CopyOption
    ): Path =
        move(target.path, *options)
    
    fun moveTo(target: Path, overwrite: Boolean = false) = path.moveTo(path, overwrite)
    fun moveTo(target: Path, vararg options: CopyOption) = path.moveTo(path, *options)

    // File reading

    fun readBytes(): ByteArray =
        Files.readAllBytes(path)

    fun readText(charset: Charset = Charsets.UTF_8): String =
        Files.readString(path, charset)

    fun readLines(charset: Charset = Charsets.UTF_8): List<String> =
        Files.readAllLines(path, charset)

    fun readSymbolicLink(): Path =
        Files.readSymbolicLink(path)

    // File writing

    fun writeBytes(
        bytes: ByteArray,
        vararg options: OpenOption
    ): Path =
        Files.write(path, bytes, *options)

    fun writeText(
        text: CharSequence,
        charset: Charset = Charsets.UTF_8,
        vararg options: OpenOption
    ): Path =
        Files.writeString(path, text, charset, *options)

    fun appendBytes(bytes: ByteArray): Path =
        Files.write(
            path,
            bytes,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
        )

    fun appendText(
        text: CharSequence,
        charset: Charset = Charsets.UTF_8
    ): Path =
        Files.writeString(
            path,
            text,
            charset,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
        )

    // Streams

    fun list(): Stream<Path> =
        Files.list(path)

    fun walk(vararg options: FileVisitOption): Stream<Path> =
        Files.walk(path, *options)

    fun walk(
        maxDepth: Int,
        vararg options: FileVisitOption
    ): Stream<Path> =
        Files.walk(path, maxDepth, *options)

    // Input / output streams

    fun newInputStream(vararg options: OpenOption): InputStream =
        Files.newInputStream(path, *options)

    fun newOutputStream(vararg options: OpenOption): OutputStream =
        Files.newOutputStream(path, *options)

    fun newBufferedReader(): BufferedReader =
        Files.newBufferedReader(path)

    fun newBufferedReader(charset: Charset): BufferedReader =
        Files.newBufferedReader(path, charset)

    fun newBufferedWriter(vararg options: OpenOption): BufferedWriter =
        Files.newBufferedWriter(path, *options)

    fun newBufferedWriter(
        charset: Charset,
        vararg options: OpenOption
    ): BufferedWriter =
        Files.newBufferedWriter(path, charset, *options)

    // Generic file attributes

    fun <A : BasicFileAttributes> readAttributes(
        type: Class<A>,
        vararg options: LinkOption
    ): A =
        Files.readAttributes(path, type, *options)

    fun readAttributes(
        attributes: String,
        vararg options: LinkOption
    ): Map<String, Any> =
        Files.readAttributes(path, attributes, *options)

    fun getAttribute(
        attribute: String,
        vararg options: LinkOption
    ): Any? =
        Files.getAttribute(path, attribute, *options)

    fun setAttribute(
        attribute: String,
        value: Any,
        vararg options: LinkOption
    ): Path =
        Files.setAttribute(path, attribute, value, *options)
}