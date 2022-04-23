// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.MutableStateFlow
import java.awt.Desktop
import java.net.URI
import java.util.*

const val trademePre: String = "https://www.trademe.co.nz/a/motors/cars/search?bof=PucvXiAC&user_region=2"
const val facebookPre: String = "https://www.facebook.com/marketplace/category/vehicles"
const val carjamPre: String = "https://www.carjam.co.nz/car/?plate="
const val turnersPre1: String = "https://www.turners.co.nz/Cars/Used-Cars-for-Sale/"
const val turnersPre2: String =
    "&locations=botany,manukau,otahuhu,north%20shore,panmure,penrose%20-%20great%20south%20road,westgate&pageno=1&sortorder=2&pagesize=120"
const val autotraderPre1: String = "https://www.autotrader.co.nz/used-cars-for-sale/%23!%230=0&1=0&2=0&3"
const val autotraderPre2: String =
    "&8=1&9=10&10=0&11=48&12=&13=&14=&15=&16=&17=&18=&19=0&20=0&21=0&22=0&23=0&24=0&25=&26=&27=&28=&29=&30=&31=&32=&33=&34=&35=&36=&37=&38=&39=0&40=20&41=&42=&43=top&44=&45=0&46=&a0=%5B%5D&a1=%5B%5D&a2=%5B%5D&a3=%5B%5D&a4=%5B%5D&a5=%5B%5D&a6=%5B2433%5D&a7=%5B%5D&a8=%5B%5D&a9=%5B%5D&a10=%5B%5D&a11=%5B%5D&d="

val myText = mutableStateOf("text")
private val showIntDialog = mutableStateOf(false)


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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun intAlert() {
    AlertDialog(
        title = {
            Text(text = "Please Only enter Numbers or Nothing")
        },
        text = {
            Text("Please re enter only numbers or leave the field blank")
        },
        onDismissRequest = {

        },
        modifier = Modifier
            .padding(28.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        buttons = {
            Button(onClick = { showIntDialog.value = false }) {
                Text("test")
            }
        }

    )
}

@Composable
fun textFieldDemo() {
    Column(Modifier.padding(6.dp)) {

        val tradeMeState = remember { mutableStateOf(true) }
        val facebookState = remember { mutableStateOf(true) }
        val turnersState = remember { mutableStateOf(true) }
        val autoTraderState = remember { mutableStateOf(true) }
        //Minimum Value
        (Text(text = "Minimum Value"
            ,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
                .padding(top=16.dp))
        )
        val minimumValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(value = minimumValue.value, onValueChange = { minimumValue.value = it })
        (Text(text = "Maximum Value",
                style = TextStyle(
                color = Color.Black,
            fontWeight = FontWeight.Bold),
        modifier = Modifier.fillMaxWidth()
            .padding(top=16.dp)))
        val maximumValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(value = maximumValue.value, onValueChange = { maximumValue.value = it })
        (Text(text = "Odometer Max:",
        style = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight.Bold),
        modifier = Modifier.fillMaxWidth()
            .padding(top=16.dp)))
        val maximumOdo = remember { mutableStateOf(TextFieldValue()) }
        TextField(value = maximumOdo.value, onValueChange = { maximumOdo.value = it })

        val keyword = remember { mutableStateOf(TextFieldValue()) }
        (Text(text = "Search Keywords:",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
                .padding(top=16.dp)))
        TextField(value = keyword.value, onValueChange = { keyword.value = it })

        //Submit Button
        Text(
            text = "Search For:",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
                .padding(top=16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
        ) {
            (Text("Trademe:"))

            Checkbox(checked = tradeMeState.value, onCheckedChange = { tradeMeState.value = it })
            (Text("Facebook:"))

            Checkbox(checked = facebookState.value, onCheckedChange = { facebookState.value = it })
            (Text("Turners:"))

            Checkbox(checked = turnersState.value, onCheckedChange = { turnersState.value = it })
            (Text("Auto Trader:"))

            Checkbox(checked = autoTraderState.value, onCheckedChange = { autoTraderState.value = it })
        }

        MaterialTheme {
            Button(onClick = {
                if (isNumeric(minimumValue.value.text) && (isNumeric(maximumValue.value.text)) && (isNumeric(
                        maximumOdo.value.text
                    ))
                ) {
                    processSites(
                      minimumValue.value.text,
                        maximumValue.value.text,
                        maximumOdo.value.text,
                        keyword.value.text,
                        tradeMeState.value,
                        facebookState.value,
                        turnersState.value,
                        autoTraderState.value
                    )
                } else {
                    showIntDialog.value = true
                }
            }) {
                Text(
                    text = "Search With These Settings", color = Color.White
                )
            }

        }
    }

}



fun isNumeric(str: String) = str.all { it in '0'..'9' }

fun processSites(
    min: String,
    max: String,
    odo: String,
    keyword: String,
    trademestate: Boolean,
    facebookstate: Boolean,
    turnersstate: Boolean,
    autotraderstate: Boolean
) {
    println("$min,$max,$odo,$trademestate,$facebookstate,$turnersstate,$autotraderstate")

    if(trademestate){
        openInBrowser(URI("$trademePre&odometer_max=$odo&price_max=$max&price_min=$min&sort_order=motorsbunowasc&search_string=$keyword"))
    }
    if(facebookstate){
        openInBrowser(URI("$facebookPre?minPrice=$min&maxPrice=$max&query=$keyword&exact=false"))
    }
    if(turnersstate){
        openInBrowser(URI("$turnersPre1?searchfor=$keyword&odoto=$odo&pricefrom=$min&priceto=$max$turnersPre2"))
    }
    if(autotraderstate){
        openInBrowser((URI("$autotraderPre1=$odo&4=0&5=0&6=$min&7=$max$autotraderPre2$keyword")))
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        textFieldDemo()
        if(showIntDialog.value) {
            intAlert()
        }
    }
}
