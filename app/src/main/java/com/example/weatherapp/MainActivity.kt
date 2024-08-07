package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.weatherapp.ui.theme.WeatherAppTheme


class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    WeatherScreen(weatherViewModel)
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel) {
    var cityName by remember { mutableStateOf("") }
    val weather by weatherViewModel.weather.collectAsState()

    val infiniteTransition = rememberInfiniteTransition()
    val colorAnim by infiniteTransition.animateColor(
        initialValue = Color(0xFF81D4FA),
        targetValue = Color(0xFF0288D1),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorAnim.copy(alpha = 0.6f), Color(0xFF00796B).copy(alpha = 0.6f))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = cityName,
            onValueChange = { cityName = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { weatherViewModel.fetchWeather(cityName) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF673AB7).copy(alpha = 0.8f)),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .shadow(8.dp)
        ) {
            Text(text = "Get Weather", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        weather?.let {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row {
                    WeatherInfoCard(
                        title = "City",
                        value = it.name,
                        icon = painterResource(id = R.drawable.ic_location_city),
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        title = "Temperature",
                        value = "${it.main.temp} °C",
                        icon = painterResource(id = R.drawable.ic_thermometer),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    WeatherInfoCard(
                        title = "Feels Like",
                        value = "${it.main.feels_like} °C",
                        icon = painterResource(id = R.drawable.ic_thermometer),
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        title = "Weather",
                        value = it.weather[0].description,
                        icon = rememberImagePainter(data = "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png"),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    WeatherInfoCard(
                        title = "Wind Speed",
                        value = "${it.wind.speed} m/s",
                        icon = painterResource(id = R.drawable.ic_wind),
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        title = "Humidity",
                        value = "${it.main.humidity}%",
                        icon = painterResource(id = R.drawable.ic_humidity),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    WeatherInfoCard(
                        title = "Pressure",
                        value = "${it.main.pressure} hPa",
                        icon = painterResource(id = R.drawable.ic_pressure),
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        title = "Sunrise",
                        value = java.text.SimpleDateFormat("hh:mm a").format(java.util.Date(it.sys.sunrise * 1000)),
                        icon = painterResource(id = R.drawable.ic_sunrise),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    WeatherInfoCard(
                        title = "Sunset",
                        value = java.text.SimpleDateFormat("hh:mm a").format(java.util.Date(it.sys.sunset * 1000)),
                        icon = painterResource(id = R.drawable.ic_sunset),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherInfoCard(title: String, value: String, icon: Painter, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(8.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.7f), Color(0xFFB2EBF2).copy(alpha = 0.7f))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .background(Transparent)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
