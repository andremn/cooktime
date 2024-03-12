package com.nicoapps.cooktime.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.lerp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTabIndicator(
    pagerState: PagerState,
    tabPositions: List<TabPosition>
) {
    val currentPageOffsetFraction by remember {
        derivedStateOf {
            pagerState.currentPageOffsetFraction
        }
    }

    val previousTabPosition = tabPositions.getOrNull(pagerState.currentPage.dec())
    val currentTabPosition = tabPositions[pagerState.currentPage]
    val nextTabPosition = tabPositions.getOrNull(pagerState.currentPage.inc())
    val indicatorOffset = if (currentPageOffsetFraction > 0 && nextTabPosition != null) {
        lerp(currentTabPosition.left, nextTabPosition.left, currentPageOffsetFraction)
    } else if (currentPageOffsetFraction < 0 && previousTabPosition != null) {
        lerp(currentTabPosition.left, previousTabPosition.left, -currentPageOffsetFraction)
    } else {
        currentTabPosition.left
    }

    SecondaryIndicator(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(currentTabPosition.width)
    )
}