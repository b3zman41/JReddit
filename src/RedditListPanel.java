import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Terence on 1/29/14.
 */
public class RedditListPanel extends JPanel implements ActionListener {

    JButton leftButton, rightButton;

    JPanel listingPanel = new JPanel();

    int currentPage = 0;

    String link = "", lastId = "";

    public RedditListPanel(String link){

        this.link = link;

        ArrayList<RedditPost> redditPosts = RedditMain.makeRedditPosts(link, null, null);

        setLayout(new BorderLayout());
        listingPanel.setLayout(null);

        int currentY = 0;

        for (RedditPost redditPost : redditPosts){

            RedditPostPanel postPanel = new RedditPostPanel(redditPost);
            postPanel.setLocation(0, currentY);

            listingPanel.add(postPanel);

            lastId = "t3_" + redditPost.id;

            currentY += postPanel.getHeight();
        }

        add(listingPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLocation(0, currentY);

        buttonsPanel.setLayout(new GridLayout(1, 2));

        leftButton = new JButton("Back");
        rightButton = new JButton("Right");

        leftButton.addActionListener(this);
        rightButton.addActionListener(this);

        buttonsPanel.add(leftButton);
        buttonsPanel.add(rightButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, currentY));

        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("Next Page" + lastId);

        if(e.getSource().equals(leftButton) || e.getSource().equals(rightButton))
            for (Component component : listingPanel.getComponents())
                listingPanel.remove(component);

        this.updateUI();

        ArrayList<RedditPost> newPosts = RedditMain.makeRedditPosts(link + "?limit=100&after=" + lastId, null, null);

        int currentY = 0;

        for (RedditPost redditPost : newPosts){

            RedditPostPanel postPanel = new RedditPostPanel(redditPost);
            postPanel.setLocation(0, currentY);

            listingPanel.add(postPanel);

            lastId = "t3_" + redditPost.id;

            currentY += postPanel.getHeight();
        }

        this.updateUI();
    }

    public void updatePosts(String link){

        for (Component component : listingPanel.getComponents())
            listingPanel.remove(component);

        listingPanel.updateUI();

        ArrayList<RedditPost> newPosts = RedditMain.makeRedditPosts(link + "&after=" + lastId, null, null);

        int currentY = 0;

        for (RedditPost redditPost : newPosts){

            RedditPostPanel postPanel = new RedditPostPanel(redditPost);
            postPanel.setLocation(0, currentY);

            postPanel.setVisible(true);

            listingPanel.add(postPanel);

            lastId = "t3_" + redditPost.id;

            currentY += postPanel.getHeight();
        }

        listingPanel.updateUI();
    }

}
