package com.zackatoo.quickrender.Utils;

import java.util.ArrayList;
import java.util.Random;

public class RandomGen
{
    private Random random;

    public RandomGen(long seed)
    {
        random = new Random(seed);
    }

    public RandomGen()
    {
        random = new Random(System.currentTimeMillis());
    }

    public <T> T choose(T ... parameters)
    {
        return parameters[rndInRange(parameters.length)];
    }

    public <T> int chooseIndex(T value, T ... parameters)
    {
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++)
        {
            if (parameters[i] == value)
            {
                indexes.add(i);
            }
        }

        return indexes.get(rndInRange(indexes.size()));
    }

    public <T> int chooseIndexFromArray(T value, T[] parameters)
    {
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++)
        {
            if (parameters[i] == value)
            {
                indexes.add(i);
            }
        }

        return indexes.get(rndInRange(indexes.size()));
    }

    public boolean rndBoolean()
    {
        return rndInRange(2) == 0;
    }

    public int rndInRange(int lowerBound, int upperBound)
    {
        int rdmn = random.nextInt() % (upperBound - lowerBound);
        return (rdmn < 0 ? rdmn + upperBound - lowerBound : rdmn) + lowerBound;
    }

    public int rndInRange(int upperBound)
    {
        return rndInRange(0, upperBound);
    }

    // Generates a series of segments of random length
    // Returns the lengths of those segments
    public int[] randomSegmentDurations(int totalDuration, int minSegmentDuration, int numSegments)
    {
        if (minSegmentDuration * numSegments >= totalDuration || numSegments <= 0) return new int[0];

        int leftover = totalDuration - minSegmentDuration * numSegments;

        int durations[] = new int[numSegments];

        for (int i = 0; i < durations.length; i++)
        {
            durations[i] = minSegmentDuration;
        }

        while (leftover > 0)
        {
            int add = 0;
            if (leftover < 5)
            {
                add = leftover;
            }
            else
            {
                add = rndInRange((int)Math.floor(leftover * 0.1d) + 2);
            }
            durations[rndInRange(durations.length)] += add;
            leftover -= add;
        }

        return durations;
    }

    public int[] randomSegmentDurations(int totalDuration, int minSegmentDuration)
    {
        return randomSegmentDurations(totalDuration, minSegmentDuration, rndInRange(1, totalDuration / minSegmentDuration + 1));
    }

    public int[] randomSegmentBreaks(int totalDuration, int minSegmentDuration)
    {
        int[] durations = randomSegmentDurations(totalDuration, minSegmentDuration);
        int[] segments = new int[durations.length - 1];

        int cumulative = 0;

        for (int i = 0; i < durations.length - 1; i++)
        {
            cumulative += durations[i];
            segments[i] = cumulative;
        }

        return segments;
    }

}

