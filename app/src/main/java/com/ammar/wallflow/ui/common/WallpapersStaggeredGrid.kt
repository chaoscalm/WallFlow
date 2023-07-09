package com.ammar.wallflow.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ammar.wallflow.data.preferences.GridType
import com.ammar.wallflow.model.Purity
import com.ammar.wallflow.model.Wallpaper

@Composable
fun WallpaperStaggeredGrid(
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    wallpapers: LazyPagingItems<Wallpaper>,
    header: (LazyStaggeredGridScope.() -> Unit)? = null,
    blurSketchy: Boolean = false,
    blurNsfw: Boolean = false,
    selectedWallpaper: Wallpaper? = null,
    showSelection: Boolean = false,
    gridType: GridType = GridType.STAGGERED,
    gridColCount: Int = 2,
    roundedCorners: Boolean = true,
    onWallpaperClick: (wallpaper: Wallpaper) -> Unit = {},
) {
    val isRefreshing = wallpapers.loadState.refresh == LoadState.Loading

    LazyVerticalStaggeredGrid(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        columns = StaggeredGridCells.Fixed(count = gridColCount),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        header?.invoke(this)
        if (isRefreshing) {
            items(9) {
                PlaceholderWallpaperCard()
            }
            return@LazyVerticalStaggeredGrid
        }
        items(
            count = wallpapers.itemCount,
            key = wallpapers.itemKey { it.id },
            contentType = wallpapers.itemContentType { "wallpaper" },
        ) { index ->
            val wallpaper = wallpapers[index]
            wallpaper?.let {
                WallpaperCard(
                    wallpaper = it,
                    blur = when (it.purity) {
                        Purity.SFW -> false
                        Purity.SKETCHY -> blurSketchy
                        Purity.NSFW -> blurNsfw
                    },
                    isSelected = showSelection && selectedWallpaper?.id == it.id,
                    fixedHeight = gridType == GridType.FIXED_SIZE,
                    roundedCorners = roundedCorners,
                    onClick = { onWallpaperClick(it) }
                )
            } ?: PlaceholderWallpaperCard()
        }
    }
}