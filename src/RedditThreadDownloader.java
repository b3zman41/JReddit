import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * Created by Terence on 2/3/14.
 */
public class RedditThreadDownloader extends Image implements Runnable{

    String link;
    Image imageToReturn;

    public RedditThreadDownloader(String link){
        this.link = link;
    }

    @Override
    public void run() {
        imageToReturn = RedditMain.getImageFromURL(link);
    }

    @Override
    public int getWidth(ImageObserver observer) {
        return imageToReturn.getWidth(observer);
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return imageToReturn.getHeight(observer);
    }

    @Override
    public ImageProducer getSource() {
        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }
}
