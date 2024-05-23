package com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.ui.components.ButtonView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FawryBottomSheet(
    code: String,
    onPressedButton: () -> Unit,
    onDismiss: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val modalBottomSheetState =
        rememberModalBottomSheetState(confirmValueChange = { false }, skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },

        sheetState = modalBottomSheetState,
        dragHandle = { },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .safeContentPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(code))
                        Toast.makeText(context, R.string.copied , Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                        .padding(15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                ) {
                   Row (verticalAlignment = Alignment.CenterVertically){
                       Text(
                           modifier = Modifier
                               .padding(15.dp),
                           text = code,
                           style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
                       )
                       Image(
                           painterResource(R.drawable.copy_icon),
                           contentDescription = "",
                           modifier = Modifier
                               .padding(end=15.dp).size(30.dp),
                       )
                   }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.use_this_code_to_complete_your_payment_with_Fawry),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),

                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ButtonView(
                            onClick = {
                                onPressedButton()
                            },
                            title = stringResource(id = R.string.close),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
