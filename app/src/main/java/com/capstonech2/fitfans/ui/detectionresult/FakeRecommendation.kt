package com.capstonech2.fitfans.ui.detectionresult

import com.capstonech2.fitfans.data.model.Recommendation

object FakeRecommendation {
    val recommendation = listOf(
        Recommendation(
            level = "Beginner",
            time = "15 Minutes"
        ),
        Recommendation(
            level = "Ideal",
            time = "20 Minutes"
        ),
        Recommendation(
            level = "Expert",
            time = "25 Minutes"
        ),
    )
}