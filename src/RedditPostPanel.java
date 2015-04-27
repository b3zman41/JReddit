import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Terence on 1/29/14.
 */
public class RedditPostPanel extends JPanel implements MouseListener{

    RedditPost redditPost;

    JTextArea title, domain, time, user, subreddit;

    int lastX, lastY;

    public RedditPostPanel(RedditPost redditPost){

        this.redditPost = redditPost;

        setLayout(null);

        addMouseListener(this);

        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 100);
        setBackground(Color.white);

        setBorder(BorderFactory.createLineBorder(Color.black));

        if (redditPost.isNSFW.equals("true"))
            setBackground(new Color(200, 116, 116));

        if (redditPost.thumbnailLink.endsWith("jpg")){
            ImageIcon image = new ImageIcon(RedditMain.getImageFromURL(redditPost.thumbnailLink));

            JLabel label = new JLabel(image);

            label.setLocation(10, (int) ((100-label.getPreferredSize().getHeight()) / 2));
            label.setSize(label.getPreferredSize());
            add(label);
        }

        setUpText(title, 80, 10, 0, 30, redditPost.title, new Font("Courier", Font.PLAIN, 20), this);
        setUpText(domain, lastX, 10, 0, 30, "(" + redditPost.domain + ")", new Font("Courier", Font.PLAIN, 10), new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        setUpText(time, 80, 40, 0, 30,  "submitted at " + redditPost.createdUTC, new Font("Courier", Font.PLAIN, 15), this);
        setUpText(user, lastX, 40, 0, 30, " by " + redditPost.author.name, new Font("Courier", Font.BOLD, 15), new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        setUpText(subreddit, lastX, 40, 0, 30, " to " + redditPost.subreddit.name, new Font("Courier", Font.ITALIC, 15), new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    public void setUpText(JTextArea textArea, int x, int y, int length, int height, String text, Font font, MouseListener mouseListener){
        textArea = new JTextArea(text);
        textArea.setFont(font);
        textArea.setEditable(false);
        textArea.setLocation(x, y);
        textArea.setSize(textArea.getPreferredSize());
        textArea.setText(text);
        textArea.addMouseListener(mouseListener);
        add(textArea);

        lastX = (int) (textArea.getPreferredSize().getWidth() + x);
        lastY = (int) textArea.getPreferredSize().getHeight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        if (redditPost.url.contains(".png") || redditPost.url.contains(".jpg")|| redditPost.url.contains(".jpeg")|| redditPost.url.contains(".tif")|| redditPost.url.contains(".gif")|| redditPost.url.contains(".jpg")){
            RedditMain.showImages(new Image[]{RedditMain.getImageFromURL(redditPost.url)}, this.getParent());
        }else if (redditPost.domain.equals("imgur.com")){
            RedditMain.showImages(RedditMain.getImagesFromImgurAlbum(redditPost.url.split("/")[redditPost.url.split("/").length-1]), this.getParent().getParent());
        }else RedditMain.showLink(redditPost.url);
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}

