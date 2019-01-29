package com.zackatoo.quickrender;

import com.zackatoo.quickrender.exceptions.RenderSettingsException;

public class Main {

    public static void main(String[] args) {
        Render render = new Render();

        try
        {
            render.setFrameRate(30);
            render.setLengthInFrames(7545);
            render.setMinSegmentLength(75);
            render.render("rendered/compiled.mov");
        }
        catch (RenderSettingsException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
