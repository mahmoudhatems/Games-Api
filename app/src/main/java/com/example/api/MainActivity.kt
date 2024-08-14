package com.example.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.api.Data.Model.Game
import com.example.api.ui.theme.ApiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApiTheme {

                val viewModel: MainViewModel by viewModels()


                val games by viewModel.games.collectAsState()
                val error by viewModel.error.collectAsState()


                LaunchedEffect(Unit) {
                    viewModel.fetchGames("action")
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black), horizontalAlignment = Alignment.CenterHorizontally ) {
                    Text(text = "Games List", fontSize = 28.sp, fontWeight = FontWeight.Bold , color =Color(0xffFFAFC7),modifier = Modifier.padding(16.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                    }

                    LazyColumn {
                        items(games) { game ->
                            GameCard(game)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GameCard(game: Game) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(0.2.dp,color =Color(0xffFFAFC7) ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row (horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)

            ) {

                Text(
                    text = game.title,

                    color = Color(0xffFFAFC7),
                    modifier = Modifier.padding(bottom = 8.dp), fontSize = 18.sp , fontWeight = FontWeight.Medium
                )


                Text(
                    text = game.genre,

                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )


                AsyncImage(
                    model = game.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Platform: ${game.platform}",

                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
