package com.capstonech2.fitfans.ui.home.progressreport

import com.capstonech2.fitfans.R

object ReportDataSource {
    val report = listOf(
        Report(
            icon = R.drawable.ic_fire,
            name = "Calories burn",
            unit = "Cal"
        ),
        Report(
            icon = R.drawable.ic_bmi,
            name = "BMI",
            unit = "Kg/m2"
        )
    )
}

data class Report(
    val icon: Int,
    val name: String,
    val value: Double? = 000.0,
    val unit: String
)