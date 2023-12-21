package com.z1.comparaprecos.common.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsBlurEffect() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S