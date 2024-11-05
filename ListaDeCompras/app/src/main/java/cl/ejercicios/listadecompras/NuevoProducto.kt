package cl.ejercicios.listadecompras

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.ejercicios.listadecompras.db.DataBase
import cl.ejercicios.listadecompras.db.Producto
import cl.ejercicios.listadecompras.db.ProductoDao
import cl.ejercicios.listadecompras.ui.theme.ListaDeComprasTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NuevoProducto : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NuevoProductoUI()
        }
    }
}



@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun NuevoProductoUI() {
    var (nombreProducto, setNombreProducto) = remember { mutableStateOf("") }
    val contexto = LocalContext.current
    val coroutineScope  = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(all = 20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.pendiente),
            contentDescription = "Producto Nuevo",
            modifier = Modifier.height(100.dp)
                .padding(vertical = 10.dp)
        )
        TextField(
            value = nombreProducto,
            onValueChange = { setNombreProducto(it) },
            label = { Text(text = stringResource(R.string.label_producto)) },
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 20.dp)
        )
        Button(onClick = {
            val intent = Intent(contexto, MainActivity::class.java)

            coroutineScope.launch(Dispatchers.IO) {
                val dao = DataBase.getInstance( contexto ).productoDao()
                val producto = Producto(producto = nombreProducto)
                dao.insertar(producto)
                contexto.startActivity(intent)
            }

        }) {
            Text(text = stringResource(R.string.btn_crear))
        }
    }
}