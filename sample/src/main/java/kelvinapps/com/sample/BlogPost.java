package kelvinapps.com.sample;

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
public class BlogPost {

    String title;
    String author;

    public BlogPost() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
