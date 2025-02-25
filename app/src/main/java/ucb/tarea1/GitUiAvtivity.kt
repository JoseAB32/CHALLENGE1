package ucb.tarea1
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.calyr.network.GithubRemoteDataSource
import com.calyr.network.RetrofitBuilder
import ucb.tarea1.ui.theme.Tarea1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitUiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GitUi(Modifier.padding(innerPadding), applicationContext)
                }
            }
        }
    }
}

@Composable
fun GitUi(modifier: Modifier = Modifier, context: Context) {
    val dataSource = GithubRemoteDataSource(RetrofitBuilder)

    var urlImage by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.github_user_prompt))

        TextField(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            value = userId,
            onValueChange = { it: String -> userId = it },
            label = { Text("Ingrese el usuario") }
        )

        Button(onClick = {
            val show =Toast.makeText(context, userId, Toast.LENGTH_LONG).show()
            CoroutineScope(Dispatchers.IO).launch {
                val response = dataSource.getAvatarInfo(githubLogin = userId)
                urlImage = response.url
            }
        }) {
            Text(text = stringResource(id=R.string.search))
        }

        AsyncImage(
            model = urlImage,
            contentDescription = "Avatar de GitHub",
            modifier = Modifier.size(120.dp)
        )
    }
}
