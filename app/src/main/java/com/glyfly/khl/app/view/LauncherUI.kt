package com.glyfly.khl.app.view

import android.view.View
import com.glyfly.khl.R
import com.glyfly.khl.app.activity.LauncherActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.relativeLayout

class LauncherUI : AnkoComponent<LauncherActivity>{

    override fun createView(ui: AnkoContext<LauncherActivity>): View {
        return with(ui) {
            relativeLayout {
                backgroundResource = R.drawable.launcher
            }
        }
    }
}