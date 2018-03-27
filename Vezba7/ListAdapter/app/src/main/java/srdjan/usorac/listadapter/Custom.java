package srdjan.usorac.listadapter;

import android.graphics.drawable.Drawable;

/**
 * Created by student on 27.3.2018.
 */

public class Custom {

    private String name;
    private Drawable image;

    public Custom(String name, Drawable image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
