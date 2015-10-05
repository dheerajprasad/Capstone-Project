package sample.com.horoscope;

import android.content.Context;

import java.util.List;

/**
 * Created by dheerajprasad on 1/10/2015.
 */
public class ZodiacSign {
    String name;
    String duration;
    String fordate;
    int image;
    Context ctx;

        ZodiacSign(String name, String duration,String fordate, int image) {
        this.name = name;
        this.duration = duration;
        this.fordate=fordate;
        this.image = image;
    }

    ZodiacSign(){


    }

    private List<ZodiacSign> ZodiacSign;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.


}
