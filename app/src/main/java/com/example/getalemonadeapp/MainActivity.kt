package com.example.getalemonadeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getalemonadeapp.ui.theme.GetALemonadeAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var clickCount by remember { mutableIntStateOf(0) }
            GetALemonadeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GetLemonadeApp(
                        modifier = Modifier.padding(innerPadding), clickCount = clickCount
                    )
                }
            }
        }
    }
}

@Composable
fun GetLemonadeApp(
    clickCount: Int = 0, modifier: Modifier = Modifier
) {
    var currentState by remember { mutableStateOf(LemonadeStates.TREE) }

    CoreView(
        clickCount = clickCount,
        imagePainter = currentState.imageResource,
        imageDescription = currentState.descriptionResource,
        textContent = currentState.textResource,
        onImageClick = {
            currentState = when (currentState) {
                LemonadeStates.TREE -> LemonadeStates.LEMON
                LemonadeStates.LEMON -> LemonadeStates.LEMONADE
                LemonadeStates.LEMONADE -> LemonadeStates.GLASS
                LemonadeStates.GLASS -> LemonadeStates.TREE
            }
        },
        modifier = modifier
    )
}

@Composable
fun CoreView(
    clickCount: Int,
    @DrawableRes imagePainter: Int,
    @StringRes imageDescription: Int,
    @StringRes textContent: Int,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var clicks by remember { mutableIntStateOf(clickCount) }
        var timerActive by remember { mutableStateOf(false) }
        var randomClickGoal = remember { (2..4).random() }
        Log.d("ClickGoal", "CoreView randomClickGoal: $randomClickGoal")

        LaunchedEffect(timerActive) {
            if (timerActive && imageDescription == R.string.lemon) {
                delay(10_000)
                if (clicks < randomClickGoal) {
                    clicks = 0
                    Log.d("ZERANDO", "Zerando os clicks (clicks < randomClickGoal)")
                }
                timerActive = false
                Log.d("TIMER", "timer coroutine: timer OFF")
            }
        }

        Surface(
            modifier = modifier
                .padding(bottom = 16.dp)
                .clickable {
                    Log.d("CLICKABLE", "VALOR DOS CLICKS ASSIM QUE CLICKA: $clicks ")
                    clicks++
                    Log.d("CLICKS", "Clicks count: $clicks")

                    if (imageDescription == R.string.lemon) {

                        if (clicks == 1) {
                            timerActive = true
                            Log.d("TIMER", "timer onClick: timer ON")
                        }

                        if (clicks >= randomClickGoal) {
                            onImageClick()
                            clicks = 0
                            timerActive = false
                            Log.d("TIMER", "timer onClick: timer OFF")
                        }
                    } else {
                        Log.d("NO LEMON", "Fora da LEMON VIEW")
                        onImageClick()
                        clicks = 0
                    }

                },
            shape = RoundedCornerShape(16.dp),
            color = colorResource(R.color.button_background),
        ) {
            Image(
                painter = painterResource(imagePainter),
                contentDescription = stringResource(imageDescription),
            )
        }
        Text(
            text = if (imageDescription == R.string.lemon) {
                stringResource(textContent) + " (${clicks}/${randomClickGoal})"
            } else {
                stringResource(textContent)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GetALemonadeAppTheme {
        GetLemonadeApp()
    }
}