package com.project.momentum.client

import io.ktor.client.*
import io.ktor.client.engine.android.Android
import java.net.InetSocketAddress
import java.net.Proxy

val client = HttpClient(Android) {
    engine {
        connectTimeout = 100_000
        socketTimeout = 100_000
        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
    }
}
