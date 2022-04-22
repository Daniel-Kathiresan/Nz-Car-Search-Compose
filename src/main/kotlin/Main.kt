// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Desktop
import java.net.URI
import java.util.*



fun openInBrowser(uri: URI) {
    val osName by lazy(LazyThreadSafetyMode.NONE) { System.getProperty("os.name").lowercase(Locale.getDefault()) }
    val desktop = Desktop.getDesktop()
    when {
        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> desktop.browse(uri)
        "mac" in osName -> Runtime.getRuntime().exec("open $uri")
        "nix" in osName || "nux" in osName -> Runtime.getRuntime().exec("xdg-open $uri")
        else -> throw RuntimeException("cannot open $uri")
    }
}

@Composable
fun textFieldDemo() {
    Column(Modifier.padding(6.dp)) {

        //Minimum Value
        (Text("Minimum Value"))
        val minimumValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = minimumValue.value,
            onValueChange = { minimumValue.value = it }
        )
        (Text("Maximum Value"))
        val maximumValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = maximumValue.value,
            onValueChange = { maximumValue.value = it }
        )
        (Text("Odometer Max:"))
        val maximumOdo = remember {mutableStateOf(TextFieldValue())}
        TextField(
            value = maximumOdo.value,
            onValueChange = {maximumOdo.value = it}
        )

        //Submit Button

        (Text("Search On:"))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            (Text("Trademe:"))
            val tradeMeState = remember { mutableStateOf(true) }
            Checkbox(
                checked = tradeMeState.value,
                onCheckedChange = { tradeMeState.value = it }
            )
            (Text("Facebook:"))
            val facebookState = remember { mutableStateOf(true) }
            Checkbox(
                checked = facebookState.value,
                onCheckedChange = { facebookState.value = it }
            )
            (Text("Turners:"))
            val turnersState = remember { mutableStateOf(true) }
            Checkbox(
                checked = turnersState.value,
                onCheckedChange = { turnersState.value = it }
            )
            (Text("Auto Trader:"))
            val autoTraderState = remember { mutableStateOf(true) }
            Checkbox(
                checked = autoTraderState.value,
                onCheckedChange = { autoTraderState.value = it }
            )
        }
        MaterialTheme {
            Button(onClick = { openInBrowser(URI("https://domain.tld/page")) }) {
                Text(
                    text = "Search With These Settings",
                    color = Color.Black
                )
            }

        }
    }

}

fun processSites(){

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        textFieldDemo()
    }
}
