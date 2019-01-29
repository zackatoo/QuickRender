package com.zackatoo.quickrender;

import com.zackatoo.quickrender.Utils.RandomGen;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import java.io.File;
import java.util.ArrayList;

public class BackgroundManager
{
    private static final String BACKGROUND_IMAGE_DIRECTORY = "C:\\Users\\Zack\\Desktop\\Zackatoo\\images\\png\\normal";
    private static final int MAX_BOUNCE = 2;

    private RandomGen random;

    private ArrayList<File> backgroundFiles;

    private int[] segmentBreaks;
    private int currentBreak = 0;

    private MarvinImage[] backgrounds;
    private int currentBackgroundIndex;

    private int currentBounceIndex = 0;

    public BackgroundManager(int[] segmentBreaks, RandomGen random)
    {
        this.segmentBreaks = segmentBreaks;
        this.random = random;

        backgroundFiles = getBackgroundFiles(BACKGROUND_IMAGE_DIRECTORY);

        backgrounds = new MarvinImage[backgroundFiles.size()];
        currentBackgroundIndex = random.rndInRange(backgrounds.length);
        backgrounds[currentBackgroundIndex] = MarvinImageIO.loadImage(backgroundFiles.get(currentBackgroundIndex).getAbsolutePath());
    }

    public MarvinImage getCurrentBackground(int currentFrame)
    {
        return backgrounds[currentBackgroundIndex];
    }

    public int getCurrentBackgroundIndex(int currentFrame)
    {
        updateCurrentBackground(currentFrame);

        return currentBackgroundIndex;
    }

    public int getCurrentBounceIndex(int currentFrame)
    {
        updateCurrentBounce(currentFrame);

        return Math.abs(currentBounceIndex);
    }

    private void updateCurrentBounce(int currentFrame)
    {
        if (currentBounceIndex < 0)
        {
            currentBounceIndex++;
        }
        else if (currentBounceIndex > 0)
        {
            if (currentBounceIndex != MAX_BOUNCE)
            {
                currentBounceIndex++;
            }
            else
            {
                currentBounceIndex = -MAX_BOUNCE;
            }
        }
        else if (currentBreak < segmentBreaks.length && currentFrame == segmentBreaks[currentBreak] - MAX_BOUNCE)
        {
            currentBounceIndex = 1;
        }
    }

    private void updateCurrentBackground(int currentFrameIndex)
    {
        if (currentBreak < segmentBreaks.length && currentFrameIndex == segmentBreaks[currentBreak])
        {
            currentBreak++;

            int newIndex = random.rndInRange(backgroundFiles.size());

            while (newIndex == currentBackgroundIndex)
            {
                newIndex = random.rndInRange(backgroundFiles.size());
            }

            currentBackgroundIndex = newIndex;

            if (backgrounds[currentBackgroundIndex] == null)
            {
                backgrounds[currentBackgroundIndex] = MarvinImageIO.loadImage(backgroundFiles.get(currentBackgroundIndex).getAbsolutePath());
            }
        }
    }

    private ArrayList<File> getBackgroundFiles(String directoryPath)
    {
        ArrayList<File> imageFiles = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        if (allFiles != null && allFiles.length != 0)
        {
            for (File i : allFiles)
            {
                if (i.getAbsolutePath().toLowerCase().endsWith(".png"))
                {
                    imageFiles.add(i);
                }
            }
        }

        return imageFiles;
    }
}
