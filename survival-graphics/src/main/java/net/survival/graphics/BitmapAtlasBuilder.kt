package net.survival.graphics

import java.util.*

class BitmapAtlasBuilder(
    val width: Int,
    val height: Int
) {
    // TODO: Use custom balanced BST structure for best-match lookup optimization.

    private val allocatedRegions: ArrayList<BitmapRegion> = ArrayList()
    private val freeRegions: ArrayList<BitmapRegion> = ArrayList()

    init {
        freeRegions.add(BitmapRegion(0, 0, width, height, null))
    }

    fun build(): Bitmap {
        val atlas = Bitmap(width, height)
        for (region in allocatedRegions) {
            val bitmap = region.bitmap
            val left = region.left
            val top = region.top
            Bitmap.blit(bitmap!!, 0, 0, bitmap.width, bitmap.height, atlas, left, top)
        }
        return atlas
    }

    fun addBitmap(bitmap: Bitmap): BitmapRegion? {
        var selectedRegion = null as BitmapRegion?
        for (region in freeRegions) {
            if (canFit(bitmap, region) && (selectedRegion == null || region.area < selectedRegion.area)) {
                selectedRegion = region
            }
        }

        if (selectedRegion == null) {
            return null
        }

        return if (fitsPerfectly(bitmap, selectedRegion)) {
            selectedRegion.bitmap = bitmap
            freeRegions.remove(selectedRegion)
            allocatedRegions.add(selectedRegion)
            selectedRegion
        }
        else if (bitmap.width == selectedRegion.width) {
            val topRegion = BitmapRegion(
                selectedRegion.left,
                selectedRegion.top,
                selectedRegion.right,
                selectedRegion.top + bitmap.height,
                bitmap
            )
            val bottomRegion = BitmapRegion(
                selectedRegion.left,
                selectedRegion.top + bitmap.height,
                selectedRegion.right,
                selectedRegion.bottom,
                null
            )
            freeRegions.remove(selectedRegion)
            freeRegions.add(bottomRegion)
            allocatedRegions.add(topRegion)
            topRegion
        }
        else if (bitmap.height == selectedRegion.height) {
            val leftRegion = BitmapRegion(
                selectedRegion.left,
                selectedRegion.top,
                selectedRegion.left + bitmap.width,
                selectedRegion.bottom,
                bitmap
            )
            val rightRegion = BitmapRegion(
                selectedRegion.left + bitmap.width,
                selectedRegion.top,
                selectedRegion.right,
                selectedRegion.bottom,
                null
            )
            freeRegions.remove(selectedRegion)
            freeRegions.add(rightRegion)
            allocatedRegions.add(leftRegion)
            leftRegion
        }
        else {
            val filledRegion = BitmapRegion(
                selectedRegion.left,
                selectedRegion.top,
                selectedRegion.left + bitmap.width,
                selectedRegion.top + bitmap.height,
                bitmap
            )
            val topRegion = BitmapRegion(
                selectedRegion.left + bitmap.width,
                selectedRegion.top,
                selectedRegion.right,
                selectedRegion.top + bitmap.height,
                null
            )
            val bottomRegion = BitmapRegion(
                selectedRegion.left,
                selectedRegion.top + bitmap.height,
                selectedRegion.right,
                selectedRegion.bottom,
                null
            )
            freeRegions.remove(selectedRegion)
            freeRegions.add(topRegion)
            freeRegions.add(bottomRegion)
            allocatedRegions.add(filledRegion)
            filledRegion
        }
    }
}

private fun canFit(bitmap: Bitmap, region: BitmapRegion): Boolean =
    bitmap.width <= region.width && bitmap.height <= region.height

private fun fitsPerfectly(bitmap: Bitmap, region: BitmapRegion): Boolean =
    bitmap.width == region.width && bitmap.height == region.height
