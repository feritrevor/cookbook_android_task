package cz.ackee.cookbook.util.api

import java.io.IOException
import kotlin.jvm.Throws

interface SupplierWithException<T> {
    @Throws(IOException::class)
    fun get(): T
}