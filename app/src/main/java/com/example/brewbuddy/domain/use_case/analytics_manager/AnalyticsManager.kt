package com.example.brewbuddy.domain.use_case.analytics_manager

import android.os.Bundle
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

val LocalAnalytics = staticCompositionLocalOf<AnalyticsManager?> { null }

@Singleton
class AnalyticsManager @Inject constructor() {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    fun logEvent(eventName: String) {
        firebaseAnalytics.logEvent(eventName, null)
    }

    fun logEventWithParams(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    fun logRecipeId(recipeId: String, params: Bundle) {
        firebaseAnalytics.logEvent(recipeId, params)
    }
}