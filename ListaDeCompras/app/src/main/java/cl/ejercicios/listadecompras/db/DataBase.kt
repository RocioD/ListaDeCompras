package cl.ejercicios.listadecompras.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Producto::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun productoDao():ProductoDao
    companion object {
        // Volatile asegura que sea actualizada la propiedad atómicamente
        @Volatile
        private var BASE_DATOS : DataBase? = null

        fun getInstance(contexto: Context):DataBase {
            // synchronized previene el acceso de múltiples threads de manera simultánea
                    return BASE_DATOS ?: synchronized(this) {
                        Room.databaseBuilder(
                            contexto.applicationContext,
                            DataBase::class.java,
                            "Productos.bd"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                            .also { BASE_DATOS = it }
                    }
        }
    }
}