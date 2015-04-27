import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Terence on 1/26/14.
 */

public class RedditMain {

    boolean isSignedIn = false;

    public static HashMap<String, RedditUser> usersCached = new HashMap<String, RedditUser>();

    static int currentImage = 0;

    public static String doGetHTTP(String url, String[] keys, String[] values){

        String builder = "";
        String currentLine = "";
        String parameters = "";

        try {

            try {
                for(int i = 0; i < keys.length; i++)
                    parameters += keys[i] + "=" + values[i] + "&";


                url += "?" + parameters;
            }catch (NullPointerException e){}

            URL theURL = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) theURL.openConnection();

            urlConnection.setRequestMethod("GET");

            //ForImgur
            urlConnection.setRequestProperty("Authorization", "Client-ID 7fbdb925d66da03");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((currentLine = bufferedReader.readLine()) != null){
                builder+=currentLine;
            }

        } catch (Exception e){e.printStackTrace();}

        return builder;
    }

    public static String doPostHTTP(String url, String parameters){

        String builder = "";
        String currentLine = "";

        try {
            URL theURL = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) theURL.openConnection();

            urlConnection.setRequestProperty("Content-Length", String.valueOf(parameters.getBytes().length));

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(parameters);

            outputStream.flush();
            outputStream.close();

            urlConnection.disconnect();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((currentLine = bufferedReader.readLine()) != null){
                builder+=currentLine;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder;
    }

    public static String getAboutUser(String user){
        return doGetHTTP("http://www.reddit.com/user/" + user + "/about.json", new String[]{"username"}, new String[]{user});
    }

    public static String getNestedData(String key, JSONObject jsonObject){

        String[] nests = key.split(":");

        if (nests.length < 2)
            return getJsonData(nests[0], jsonObject);

        try {
            for (int i = 0; i < nests.length; i++){
                jsonObject = jsonObject.getJSONObject(nests[i]);
            }
        } catch (JSONException e) {
            try {
                return String.valueOf(jsonObject.get(nests[nests.length-1]));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }

    public static String getJsonData(String key, JSONObject jsonObject){
        try {
            return String.valueOf(jsonObject.get(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<RedditPost> makeRedditPosts(String url, String[] keys, String[] values){
        RedditPost currentPost = null;
        JSONArray arrayOfPosts = null;

        ArrayList<RedditPost> redditPosts = new ArrayList<RedditPost>();

        String linksJson = doGetHTTP(url, keys, values);

        try {
            arrayOfPosts = new JSONObject(linksJson).getJSONObject("data").getJSONArray("children");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < arrayOfPosts.length(); i++){
            try {

                System.out.println(i);

                currentPost = new RedditPost();

                JSONObject currentObject = arrayOfPosts.getJSONObject(i);

                currentPost.author = new RedditUser(getNestedData("data:author", currentObject));
                currentPost.commentCount = getNestedData("data:num_comments", currentObject);
                currentPost.createdUTC = getNestedData("data:created_utc", currentObject);
                currentPost.domain = getNestedData("data:domain",  currentObject);
                currentPost.id = getNestedData("data:id", currentObject);
                currentPost.name = getNestedData("data:name", currentObject);
                currentPost.selfText = getNestedData("data:selftext", currentObject);
                currentPost.subreddit = new RedditSubreddit(getNestedData("data:subreddit", currentObject));
                currentPost.thumbnailLink = getNestedData("data:thumbnail", currentObject);
                currentPost.title = getNestedData("data:title", currentObject);
                currentPost.upVotes = getNestedData("data:ups", currentObject);
                currentPost.url = getNestedData("data:url", currentObject);
                currentPost.isNSFW = getNestedData("data:over_18", currentObject);

                redditPosts.add(currentPost);
                currentPost = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return redditPosts;
    }

    public static void showLink(String link){
        try {
            Desktop.getDesktop().browse(URI.create(link));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Image getImageFromURL(String url){
        Image image = null;

        try{
            image = ImageIO.read(new URL(url));
        }catch (Exception e){e.printStackTrace();}

        return image;
    }

    public static void showImages(final Image[] notScaled, Container panel){

        final Image[] images = new Image[notScaled.length];

        currentImage = 0;

        for (int i = 0; i < images.length; i++){
            int length = notScaled[i].getWidth(null), height = notScaled[i].getHeight(null);

            while(length >= panel.getWidth() || height >= panel.getHeight()){
                length -= Toolkit.getDefaultToolkit().getScreenSize().getWidth() / leastCommonMultiple(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                height -= Toolkit.getDefaultToolkit().getScreenSize().getHeight() / leastCommonMultiple(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
            }

            images[i] = notScaled[i].getScaledInstance(length, height, 5);
        }

        final JLabel currentLabel = new JLabel(new ImageIcon(images[0]));
        final JLabel currentImageNum = new JLabel("1/" + images.length);

        JFrame frame = new JFrame("Images");

        frame.setLayout(new BorderLayout());
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

        JButton leftButton = new JButton("Back");
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    currentLabel.setIcon(new ImageIcon(images[currentImage - 1]));
                    currentImage -= 1;
                    currentImageNum.setText(String.valueOf(currentImage+1) + "/" + images.length);
                }catch (ArrayIndexOutOfBoundsException ex){}
            }
        });

        JButton rightButton = new JButton("Right");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    currentLabel.setIcon(new ImageIcon(images[currentImage + 1]));
                    currentImage+=1;
                    currentImageNum.setText(String.valueOf(currentImage+1) + "/" + images.length);
                }catch (ArrayIndexOutOfBoundsException ex){}
            }
        });

        JPanel imagesPanel = new JPanel();

        imagesPanel.setLayout(new GridLayout(1, 1));

        imagesPanel.add(currentLabel);

        JScrollPane scrollPaneForImage = new JScrollPane(imagesPanel);

        frame.add(scrollPaneForImage, BorderLayout.CENTER);

        buttonsPanel.add(leftButton, BorderLayout.WEST);
        buttonsPanel.add(rightButton, BorderLayout.EAST);
        buttonsPanel.add(currentImageNum, BorderLayout.SOUTH);

        frame.add(buttonsPanel, BorderLayout.NORTH);
        frame.add(scrollPaneForImage);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static Image[] getImagesFromImgurAlbum(String albumID){
        System.out.println(albumID);
        try {
            JSONObject jsonObject = new JSONObject(doGetHTTP("https://api.imgur.com/3/gallery/album/" + albumID + "/images", null, null));

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            Image[] images = new Image[jsonArray.length()];

            for(int i = 0; i < images.length; i++){
                images[i] = getImageFromURL(getNestedData("link", jsonArray.getJSONObject(i)));
            }

            return images;
        } catch (JSONException e) {
            return getImageFromImgurAlbum(albumID);}
    }

    public static Image[] getImageFromImgurAlbum(String albumID){
        Image[] images = new Image[1];
        try {
            JSONObject jsonObject = new JSONObject(doGetHTTP("https://api.imgur.com/3/image/" + albumID, null, null));

            return new Image[]{RedditMain.getImageFromURL(getNestedData("data:link", jsonObject))};
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double leastCommonMultiple(double first, double second){
        double lcm = first;

        while (first % lcm != 0 && second % lcm != 0)
            lcm--;

        return lcm;
    }
}
