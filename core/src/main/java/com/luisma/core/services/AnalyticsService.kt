package com.luisma.core.services

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsService {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun onCreate() {
        firebaseAnalytics = Firebase.analytics
    }

}