package ru.mattgroy.dota2itemdata.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <A, B> Iterable<A>.pmap(transform: suspend (A) -> B): List<B> = coroutineScope {
    map { async { transform(it) } }.awaitAll()
}

suspend fun <K, V, R> Map<out K, V>.pmap(transform: suspend (Map.Entry<K, V>) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.awaitAll()
}

fun <T> Collection<T>.equalsIgnoreOrder(collection: Collection<T>): Boolean {
    return this.toHashSet().let { it.size == collection.size && it.containsAll(collection) }
}
