package net.survival.graphics;

import java.util.ArrayList;

public class BitmapAtlasBuilder
{
    // TODO: Use custom balanced BST structure for best-match lookup optimization.

    private final ArrayList<BitmapRegion> allocatedRegions;
    private final ArrayList<BitmapRegion> freeRegions;
    private final int atlasWidth;
    private final int atlasHeight;

    public BitmapAtlasBuilder(int width, int height) {
        allocatedRegions = new ArrayList<>();
        freeRegions = new ArrayList<>();
        atlasWidth = width;
        atlasHeight = height;

        freeRegions.add(new BitmapRegion(0, 0, width, height, null));
    }

    public Bitmap build() {
        var atlas = new Bitmap(atlasWidth, atlasHeight);

        for (var region : allocatedRegions) {
            var bitmap = region.getBitmap();
            var left = region.getLeft();
            var top = region.getTop();
            Bitmap.blit(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), atlas, left, top);
        }

        return atlas;
    }

    public BitmapRegion addBitmap(Bitmap bitmap) {
        var selectedRegion = (BitmapRegion) null;

        for (var region : freeRegions) {
            if (canFit(bitmap, region)
                    && (selectedRegion == null || region.getArea() < selectedRegion.getArea()))
            {
                selectedRegion = region;
            }
        }

        if (selectedRegion == null) {
            return null;
        }

        if (fitsPerfectly(bitmap, selectedRegion)) {
            selectedRegion.setBitmap(bitmap);

            freeRegions.remove(selectedRegion);
            allocatedRegions.add(selectedRegion);

            return selectedRegion;
        }
        else if (bitmap.getWidth() == selectedRegion.getWidth()) {
            var topRegion = new BitmapRegion(
                    selectedRegion.getLeft(),
                    selectedRegion.getTop(),
                    selectedRegion.getRight(),
                    selectedRegion.getTop() + bitmap.getHeight(),
                    bitmap);
            var bottomRegion = new BitmapRegion(
                    selectedRegion.getLeft(),
                    selectedRegion.getTop() + bitmap.getHeight(),
                    selectedRegion.getRight(),
                    selectedRegion.getBottom(),
                    null);

            freeRegions.remove(selectedRegion);
            freeRegions.add(bottomRegion);
            allocatedRegions.add(topRegion);

            return topRegion;
        }
        else if (bitmap.getHeight() == selectedRegion.getHeight()) {
            var leftRegion = new BitmapRegion(
                    selectedRegion.getLeft(),
                    selectedRegion.getTop(),
                    selectedRegion.getLeft() + bitmap.getWidth(),
                    selectedRegion.getBottom(),
                    bitmap);
            var rightRegion = new BitmapRegion(
                    selectedRegion.getLeft() + bitmap.getWidth(),
                    selectedRegion.getTop(),
                    selectedRegion.getRight(),
                    selectedRegion.getBottom(),
                    null);

            freeRegions.remove(selectedRegion);
            freeRegions.add(rightRegion);
            allocatedRegions.add(leftRegion);

            return leftRegion;
        }
        else {
            var filledRegion = new BitmapRegion(
                    selectedRegion.getLeft(),
                    selectedRegion.getTop(),
                    selectedRegion.getLeft() + bitmap.getWidth(),
                    selectedRegion.getTop() + bitmap.getHeight(),
                    bitmap);
            var topRegion = new BitmapRegion(
                    selectedRegion.getLeft() + bitmap.getWidth(),
                    selectedRegion.getTop(),
                    selectedRegion.getRight(),
                    selectedRegion.getTop() + bitmap.getHeight(),
                    null);
            var bottomRegion = new BitmapRegion(
                    selectedRegion.getLeft(),
                    selectedRegion.getTop() + bitmap.getHeight(),
                    selectedRegion.getRight(),
                    selectedRegion.getBottom(),
                    null);

            freeRegions.remove(selectedRegion);
            freeRegions.add(topRegion);
            freeRegions.add(bottomRegion);
            allocatedRegions.add(filledRegion);

            return filledRegion;
        }
    }

    private static boolean canFit(Bitmap bitmap, BitmapRegion region) {
        return bitmap.getWidth() <= region.getWidth()
                && bitmap.getHeight() <= region.getHeight();
    }

    private static boolean fitsPerfectly(Bitmap bitmap, BitmapRegion region) {
        return bitmap.getWidth() == region.getWidth()
                && bitmap.getHeight() == region.getHeight();
    }
}