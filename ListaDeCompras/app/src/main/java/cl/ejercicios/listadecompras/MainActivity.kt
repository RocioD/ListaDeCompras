package cl.ejercicios.listadecompras

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.lifecycleScope
import cl.ejercicios.listadecompras.db.DataBase
import cl.ejercicios.listadecompras.db.Producto
import cl.ejercicios.listadecompras.db.ProductoDao
import cl.ejercicios.listadecompras.ui.theme.ListaDeComprasTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    lateinit var productoDao:ProductoDao

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeComprasUI()
        }
    }
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun ListaDeComprasUI() {

    val contexto = LocalContext.current
    val (productos, setProductos) = remember { mutableStateOf(emptyList<Producto>())}
    val coroutineScope  = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val dao = DataBase.getInstance( contexto ).productoDao()
            setProductos ( dao.findAll())
        }
    }
    Column (modifier = Modifier.fillMaxSize()){
    if( productos.size < 1 ) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.9f))
            {
            Text(
                text = stringResource(R.string.mensaje_sin_productos),
                fontSize = 5.em,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(all = 20.dp).fillMaxHeight(0.9f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            items(productos) { producto ->
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(all = 20.dp)
                ) {
                    if (producto.comprado) {
                        Image(
                            painter = painterResource(id = R.drawable.comprado),
                            contentDescription = "Comprado",
                            modifier = Modifier.width(50.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.pendiente),
                            contentDescription = "Pendiente",
                            modifier = Modifier.width(50.dp)
                                .clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val dao = DataBase.getInstance( contexto ).productoDao()
                                    producto.comprado = true
                                    dao.actualizar(producto)
                                    setProductos(dao.findAll())
                                }
                            }
                        )
                    }
                    Text(producto.producto)
                    Image(
                        painter = painterResource(id = R.drawable.borrar),
                        contentDescription = "borrar",
                        modifier = Modifier.width(50.dp)
                            .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                val dao = DataBase.getInstance( contexto ).productoDao()
                                dao.eliminar(producto)
                                setProductos(dao.findAll())
                            }
                        }
                    )
                }
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ){
        Button(onClick = {
            val intent = Intent(contexto, NuevoProducto()::class.java)
                contexto.startActivity(intent)
        }) {
            Text(text = stringResource(R.string.btn_crear))
        }
    }
        }
}