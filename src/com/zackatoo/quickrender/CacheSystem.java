package com.zackatoo.quickrender;

import java.util.ArrayList;

public class CacheSystem
{
    private static final String DELIM = "_";

    private ArrayList<Entry> caches = new ArrayList<>();
    private int numKeys;

    public CacheSystem(int numKeys)
    {
        this.numKeys = numKeys;
    }

    public <T> String getEntry(T ... keys)
    {
        if (keys.length != numKeys) return null;

        String key = makeKey(keys);

        for (Entry i : caches)
        {
            if (i.key.equals(key))
            {
                return i.value;
            }
        }

        return null;
    }

    public <T> void setEntry(String value, T ... keys)
    {
        if (keys.length != numKeys) return;

        String key = makeKey(keys);

        caches.add(new Entry(key, value));
    }

    private <T> String makeKey(T[] keys)
    {
        StringBuilder builder = new StringBuilder();

        for (T i : keys)
        {
            builder.append(i.toString());
            builder.append(DELIM);
        }

        return builder.toString();
    }

    private class Entry
    {
        String key = "";
        String value = "";

        Entry()
        {
        }

        Entry(String key, String value)
        {
            this.key = key;
            this.value = value;
        }
    }
}
