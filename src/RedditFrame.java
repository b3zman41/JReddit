import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Terence on 1/29/14.
 */
public class RedditFrame extends JFrame{

    public RedditFrame(String link){
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        setLayout(new BorderLayout());

        final JTabbedPane tabbedPane = new JTabbedPane();


        RedditListPanel subredditPanel = new RedditListPanel(link);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2));

        JButton  newTab = new JButton("New Subreddit");
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subreddit = JOptionPane.showInputDialog("Subreddit");

                JScrollPane scrollPane = new JScrollPane(new RedditListPanel("http://www.reddit.com/r/" + subreddit + "/hot.json?count=100"));
                scrollPane.getVerticalScrollBar().setUnitIncrement(15);
                tabbedPane.add(scrollPane, subreddit);
            }
        });

        JButton closeCurrentTab = new JButton("Close Current Tab");
        closeCurrentTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.remove(tabbedPane.getSelectedComponent());
            }
        });

        buttonsPanel.add(newTab);
        buttonsPanel.add(closeCurrentTab);


        JScrollPane scrollPane = new JScrollPane(subredditPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);

        tabbedPane.add(scrollPane, "Reddit");

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}
