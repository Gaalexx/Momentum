package com.project.momentum.features.contentcreation.state

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class CameraLifecycleOwner : LifecycleOwner {

    private val registry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = registry

    init {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun start() {
        if (registry.currentState == Lifecycle.State.DESTROYED) return

        if (registry.currentState < Lifecycle.State.STARTED) {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }
    }

    fun pause() {
        if (registry.currentState == Lifecycle.State.DESTROYED) return

        if (registry.currentState >= Lifecycle.State.STARTED) {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    fun destroy() {
        if (registry.currentState == Lifecycle.State.DESTROYED) return

        if (registry.currentState >= Lifecycle.State.STARTED) {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }

        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
