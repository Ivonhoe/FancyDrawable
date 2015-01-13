package com.support.drawable.style;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.*;
import com.support.animation.FreePathInterpolator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by ivonhoe on 15-1-13.
 */
public class AtomUtils {

    public static Interpolator loadInterpolator(Context context, int id) throws
            Resources.NotFoundException {

        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            Interpolator interpolator = createInterpolatorFromXml(context, parser);

            return interpolator;
        } catch (XmlPullParserException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException(
                    "Can't load animation resource ID #0x" +
                            Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException(
                    "Can't load animation resource ID #0x" +
                            Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();
        }
    }

    private static Interpolator createInterpolatorFromXml(Context c, XmlPullParser parser)
            throws XmlPullParserException, IOException {

        Interpolator interpolator = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            AttributeSet attrs = Xml.asAttributeSet(parser);

            String name = parser.getName();

            if (name.equals("linearInterpolator")) {
                interpolator = new LinearInterpolator(c, attrs);
            } else if (name.equals("accelerateInterpolator")) {
                interpolator = new AccelerateInterpolator(c, attrs);
            } else if (name.equals("decelerateInterpolator")) {
                interpolator = new DecelerateInterpolator(c, attrs);
            } else if (name.equals("accelerateDecelerateInterpolator")) {
                interpolator = new AccelerateDecelerateInterpolator(c, attrs);
            } else if (name.equals("cycleInterpolator")) {
                interpolator = new CycleInterpolator(c, attrs);
            } else if (name.equals("anticipateInterpolator")) {
                interpolator = new AnticipateInterpolator(c, attrs);
            } else if (name.equals("overshootInterpolator")) {
                interpolator = new OvershootInterpolator(c, attrs);
            } else if (name.equals("anticipateOvershootInterpolator")) {
                interpolator = new AnticipateOvershootInterpolator(c, attrs);
            } else if (name.equals("bounceInterpolator")) {
                interpolator = new BounceInterpolator(c, attrs);
            } else if (name.equals("freePathInterpolator")) {
                interpolator = new FreePathInterpolator(c, parser, attrs);
            } else {
                interpolator = new AccelerateDecelerateInterpolator(c, attrs);
            }
        }

        return interpolator;
    }
}
