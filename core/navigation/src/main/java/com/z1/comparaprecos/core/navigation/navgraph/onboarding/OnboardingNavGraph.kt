package com.z1.comparaprecos.core.navigation.navgraph.onboarding

import com.z1.comparaprecos.core.navigation.RegisterNavGraph

interface OnboardingNavGraph: RegisterNavGraph {
    fun route(): String
}