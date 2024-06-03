package com.selegend.ecommercemobile.ui.auth.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.selegend.ecommercemobile.R
import com.stevdzasan.onetap.OneTapSignInState

@Composable
fun SocialMediaSection(oneTapSignInState: OneTapSignInState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Or continue with",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth().height(40.dp),
            onClick = {
                oneTapSignInState.open()
                Log.d("OneTapSignIn", "One Tap opened")
                      },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            SocialMediaLogin(
                modifier = Modifier.weight(1f),
                icon = R.drawable.google,
                text = "Google"
            )
        }
    }
}