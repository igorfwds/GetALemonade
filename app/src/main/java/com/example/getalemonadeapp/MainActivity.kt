package com.example.getalemonadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getalemonadeapp.ui.theme.GetALemonadeAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val clickCount by remember { mutableIntStateOf(0) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetLemonadeApp(
    modifier: Modifier = Modifier, clickCount: Int = 0
) {
    var currentState by remember { mutableStateOf(LemonadeStates.TREE) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Lemonade", fontWeight = FontWeight.Bold
                )
            }, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = colorResource(R.color.button_background)
            )
        )
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var clicks by remember { mutableIntStateOf(clickCount) }
        var timerActive by remember { mutableStateOf(false) }
        var randomClickGoal by remember { mutableIntStateOf((2..4).random()) }

        LaunchedEffect(timerActive) {
            if (timerActive && imageDescription == R.string.lemon) {
                delay(3_000)
                if (clicks < randomClickGoal) {
                    clicks = 0
                }
                timerActive = false
            }
        }

        Image(
            painter = painterResource(imagePainter),
            contentDescription = stringResource(imageDescription),
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .padding(bottom = 16.dp)
                .border(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(2.dp, colorResource(R.color.button_stroke)),
                )
                .background(colorResource(R.color.button_background))
                .combinedClickable(onClick = {
                    clicks++

                    if (imageDescription == R.string.lemon) {

                        if (clicks == 1) {
                            timerActive = true
                        }

                        if (clicks == randomClickGoal) {
                            onImageClick()
                            clicks = 0
                            timerActive = false
                        }
                    } else if (imageDescription == R.string.empty_glass) {
                        randomClickGoal = (2..4).random()
                        onImageClick()
                        clicks = 0

                    } else {
                        onImageClick()
                        clicks = 0
                    }

                }, onLongClick = {
                    if (imageDescription == R.string.lemon) {
                        onImageClick()
                        clicks = 0
                    }
                })
        )
        Text(
            text = if (imageDescription == R.string.lemon) {
                stringResource(textContent) + " (${clicks}/${randomClickGoal})"
            } else {
                stringResource(textContent)
            }, style = MaterialTheme.typography.bodyLarge
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