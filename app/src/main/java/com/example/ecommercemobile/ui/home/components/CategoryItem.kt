package com.example.ecommercemobile.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommercemobile.store.domain.model.core.Category
import com.example.ecommercemobile.ui.home.events.CategoryEvent
import com.example.ecommercemobile.ui.theme.BlueGray

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onEvent: (CategoryEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(width = 2.dp, color = BlueGray, shape = CircleShape)
                .background(Color.White, CircleShape)
                .clip(CircleShape)
                .clickable { onEvent(CategoryEvent.OnCategoryClick(categoryId = category.id)) },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(70.dp)
                    .aspectRatio(1f)
                    .background(Color.White, CircleShape),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            text = category.name,
            modifier = Modifier.padding(8.dp),
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.DarkGray
        )
    }
}