package com.example.ecommercemobile.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.ecommercemobile.store.domain.model.core.Product
import com.example.ecommercemobile.ui.components.RatingBar

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product
) {
    fun String.floatToInt(): Int {
        return this.toFloat().toInt()
    }

    val discount: Float = 100 * (1 - product.price / (product.price + 50).toFloat())
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .background(color = Color.Red.copy(alpha = 0.8f))
                .padding(horizontal = 5.dp)
                .zIndex(1f)
        ) {
            Text(
                text = "-${discount.toInt()}%",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.8f
                ),
            )
        }
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${product.price + 50}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize * 0.8f
                        ),
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                    RatingBar(
                        rating = 4.0,
                        starsColor = Color.Yellow.copy(red = 0.97f, green = 0.78f, blue = 0.05f),
                        modifierStar = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}