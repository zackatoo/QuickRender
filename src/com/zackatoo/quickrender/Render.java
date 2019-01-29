package com.zackatoo.quickrender;

import com.zackatoo.quickrender.Utils.RandomGen;
import com.zackatoo.quickrender.exceptions.MovieCompileException;
import com.zackatoo.quickrender.exceptions.RenderSettingsException;
import marvin.image.MarvinImage;

import java.net.MalformedURLException;
import java.util.Vector;

public class Render
{
    private RandomGen random;

    private int frameRate = -1;
    private int length = 30; // measured in frames
    private int minSegmentLength = 30;

    private Vector<String> images = new Vector<>();
    
    private CacheSystem cacheSystem = new CacheSystem(2);
    private int[] segmentBreaks;
    private BackgroundManager bgManager;
    private ImageProcessor imageProcessor;

    public Render(int seed)
    {
        random = new RandomGen(seed);
    }

    public Render()
    {
        random = new RandomGen();
    }

    public void initalize()
    {
        segmentBreaks = random.randomSegmentBreaks(length, minSegmentLength);

        bgManager = new BackgroundManager(segmentBreaks, random);

        imageProcessor = new ImageProcessor();
    }

    public void render(String outputPath)
            throws RenderSettingsException
    {
        if (frameRate == -1) frameRate = 30;

        initalize();

        for (int i = 0; i < length; i++)
        {
            if (i % 100 == 0) System.out.println("CURRENTLY AT " + i);

            int currentBackgroundIndex = bgManager.getCurrentBackgroundIndex(i);
            int currentBounceIndex = bgManager.getCurrentBounceIndex(i);

            String currentFileName = cacheSystem.getEntry(currentBackgroundIndex, currentBounceIndex);
            if (currentFileName != null)
            {
                images.add(currentFileName);
            }
            else
            {
                MarvinImage currentBackground = bgManager.getCurrentBackground(i);
                String path = imageProcessor.processImage(currentBackground, currentBounceIndex, i);
                images.add(path);
                cacheSystem.setEntry(path, currentBackgroundIndex, currentBounceIndex);
            }
        }

        try
        {
            new JpegImagesToMovie(1920, 1080, frameRate, outputPath, images);
        }
        catch (MalformedURLException | MovieCompileException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        imageProcessor.deleteDirectory();

        System.out.println("RENDER SUCCESSFUL");
    }

    public void setFrameRate(int frameRate)
    {
        this.frameRate = frameRate;
    }

    public void setLengthInFrames(int length)
    {
        this.length = length;
    }

    public void setLengthInSeconds(int seconds)
            throws RenderSettingsException
    {
        if (frameRate == -1) throw new RenderSettingsException("Framerate must be set before setting length in minutes/seconds");

        this.length = seconds * frameRate;
    }

    public void setLengthInMinutes(int minutes, int seconds)
            throws RenderSettingsException
    {
        setLengthInSeconds(minutes * 60 + seconds);
    }

    public void setMinSegmentLength(int minSegmentLength)
    {
        this.minSegmentLength = minSegmentLength;
    }
}
