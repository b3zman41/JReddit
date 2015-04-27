import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Terence on 1/28/14.
 */
public class RedditUser {

    JSONObject jsonObject;

    String name, id, dateCreated;

    int linkKarma = 0, commentKarma = 0;

    boolean over18;

    ArrayList<RedditPost> submitted = new ArrayList<RedditPost>();
    ArrayList<RedditComment> comments = new ArrayList<RedditComment>();

    public RedditUser(String name){
        this.name = name;
    }

    public void getSubmitted(){
        submitted = RedditMain.makeRedditPosts("http://www.reddit.com/r/all/hot.json", new String[]{}, new String[]{});
    }

    public void getAboutThisUser(){
        RedditMain.usersCached.put(name, this);

        try {
            jsonObject = new JSONObject(RedditMain.getAboutUser(name));

            linkKarma = Integer.parseInt(RedditMain.getNestedData("data:link_karma", jsonObject));
            commentKarma = Integer.parseInt(RedditMain.getNestedData("data:comment_karma", jsonObject));

            over18 = Boolean.valueOf(RedditMain.getNestedData("data:over_18", jsonObject));

            id = RedditMain.getNestedData("data:id", jsonObject);

            dateCreated = (RedditMain.getNestedData("data:created_utc", jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
