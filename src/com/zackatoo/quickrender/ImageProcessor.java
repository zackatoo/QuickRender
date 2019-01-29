package com.zackatoo.quickrender;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;

import static marvin.MarvinPluginCollection.*;

import java.io.File;

public class ImageProcessor
{
    private static final String MOUTH_FILE = "C:\\Users\\Zack\\Desktop\\Programming\\Java\\LipSink\\resources\\mouths\\mouth-7.png";
    private static final int PASTEPOINT_X = 1375;
    private static final int PASTEPOINT_Y = 631;
    private static final int BOUNCE_DELTA = 20; // measured in pixels

    private MarvinImage mouth;

    private String renderDirectoryPath;
    private File renderDirectory;

    public ImageProcessor()
    {
        mouth = MarvinImageIO.loadImage(MOUTH_FILE);

        renderDirectoryPath = "render-" + System.currentTimeMillis() + "/";
        renderDirectory = new File(renderDirectoryPath);
        renderDirectory.mkdir();
    }

    public String processImage(MarvinImage background, int bounceIndex, int currentFrame)
    {
        MarvinImage image = overlayMouth(background);
        String path = renderDirectoryPath + "rendered-" + currentFrame + ".jpg";

        if (bounceIndex != 0)
        {
             image = shift(image, BOUNCE_DELTA * bounceIndex);
        }

        MarvinImageIO.saveImage(image, path);

        return path;
    }

    private MarvinImage shift(MarvinImage image, int displacement)
    {
        MarvinImage blank = new MarvinImage(image.getWidth(), image.getHeight());
        blank.clear(0xFFFFFF);

        for (int x = 0; x < blank.getWidth(); x++)
        {
            for (int y = displacement; y < blank.getHeight(); y++)
            {
                blank.setIntColor(x, y, image.getIntColor(x, y - displacement));
            }
        }

        return blank;
    }

    public void deleteDirectory()
    {
        deleteDirectory(renderDirectory);
    }

    private MarvinImage overlayMouth(MarvinImage image)
    {
        int startX = PASTEPOINT_X - mouth.getWidth() / 2;
        int startY = PASTEPOINT_Y - mouth.getHeight() / 2;

        for (int x = 0; x < mouth.getWidth(); x++)
        {
            for (int y = 0; y < mouth.getHeight(); y++)
            {
                image.setIntColor(startX + x, startY + y, mouth.getIntColor(x, y));
            }
        }

        return image;
    }

    private static void deleteDirectory(File directory)
    {
        File[] files = directory.listFiles();

        if (files != null)
        {
            for (File i : files)
            {
                if (i.isDirectory())
                {
                    deleteDirectory(i);
                }
                else
                {
                    i.delete();
                }
            }
        }

        directory.delete();
    }
}
