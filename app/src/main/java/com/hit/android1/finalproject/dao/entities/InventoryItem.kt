package com.hit.android1.finalproject.dao.entities
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hit.android1.finalproject.app.Extensions.capitalize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName="inventoryItem")
data class InventoryItem @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "unlocked")
    var unlocked: Boolean = false,
) {
    companion object {
        @Ignore
        // For caching already grabbed resource id's (getting the id takes time)
        var resourceIds = mutableMapOf<String, Int>()

        @Ignore
        fun InventoryItem.name() = id.capitalize()

        @Ignore
        fun InventoryItem.drawableResourceId(context: Context): Int = resourceIds[id] ?: run {
            resourceIds[id] = context.resources.getIdentifier(
                "inventory_item_${id.replace(' ', '_')}",
                "drawable",
                context.packageName
            )
            resourceIds[id]!!
        }

        @Ignore
        fun InventoryItem.drawableResourceId(resources: Resources, packageName: String): Int = resourceIds[id] ?: run {
            resourceIds[id] = resources.getIdentifier(
                "inventory_item_${id.replace(' ', '_')}",
                "drawable",
                packageName
            )
            resourceIds[id]!!
        }

        @Ignore
        fun InventoryItem.drawable(context: Context): Drawable? {
            val rId = resourceIds[id] ?: run {
                resourceIds[id] = context.resources.getIdentifier(
                    "inventory_item_${id.replace(' ', '_')}",
                    "drawable",
                    context.packageName
                )
                resourceIds[id]!!
            }
            return AppCompatResources.getDrawable(context, rId)
        }
    }
}