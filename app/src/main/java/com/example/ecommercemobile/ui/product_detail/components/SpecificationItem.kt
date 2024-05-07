package com.example.ecommercemobile.ui.product_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpecificationItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    fontSizeTextFirst: TextUnit = 14.sp,
    fontSizeTextSecond: TextUnit = 14.sp,
    fontWeightTextFirst: FontWeight = FontWeight.Light,
    fontWeightTextSecond: FontWeight = FontWeight.Light
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = fontSizeTextFirst,
            fontWeight = fontWeightTextFirst,
            modifier = Modifier.width(200.dp)
        )
        Text(
            text = value ?: "",
            fontSize = fontSizeTextSecond,
            fontWeight = fontWeightTextSecond,
            modifier = Modifier.weight(1f)
        )
    }
}