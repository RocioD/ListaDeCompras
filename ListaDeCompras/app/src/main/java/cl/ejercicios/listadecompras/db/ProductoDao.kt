package cl.ejercicios.listadecompras.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductoDao {

    @Query("Select * From producto Order By comprado")
    fun findAll(): List<Producto>

    @Insert
    fun insertar(producto:Producto):Long

    @Update
    fun actualizar(producto:Producto)

    @Delete
    fun eliminar(producto:Producto)
}