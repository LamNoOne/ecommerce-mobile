package com.selegend.ecommercemobile.ui.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.samples.pay.util.PaymentsUtil
import com.google.pay.button.ButtonTheme
import com.google.pay.button.ButtonType
import com.google.pay.button.PayButton
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun PaymentScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    onGooglePayButtonClick: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val payState: PaymentUiState by viewModel.paymentUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            androidx.compose.material.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                onPopBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Payment"
                            )
                        }
                        Text(
                            text = "Payment Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }, bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Max),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    HorizontalDivider(
                        thickness = 8.dp,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    )
                    // Settle google pay here
                    if (payState !is PaymentUiState.NotStarted) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = "Total payment: ",
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = "$350",
                                color = Color.Red,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        PayButton(
                            modifier = Modifier
                                .testTag("payButton")
                                .fillMaxSize()
                                .padding(bottom = 6.dp),
                            onClick = onGooglePayButtonClick,
                            allowedPaymentMethods = PaymentsUtil.allowedPaymentMethods.toString(),
                            radius = 0.dp,
                            type = ButtonType.Pay,
                            theme = ButtonTheme.Dark
                        )
                    }
                }
            }
        }
    ) {

    }
}